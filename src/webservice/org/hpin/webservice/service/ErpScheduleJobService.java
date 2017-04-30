/**
 * @author DengYouming
 * @since 2016-10-31 下午4:02:15
 */
package org.hpin.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.XmlUtils;
import org.hpin.webservice.bean.*;
import org.hpin.webservice.bean.jz.ErpReportOrgJY;
import org.hpin.webservice.bean.jz.ErpReportScheduleJY;
import org.hpin.webservice.bean.jz.ErpReportUrlJY;
import org.hpin.webservice.dao.ErpScheduleJobDao;
import org.hpin.webservice.foreign.DownloadThread;
import org.hpin.webservice.util.HttpUtils;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author DengYouming
 * @since 2016-10-31 下午4:02:15
 */
@Service(value = "org.hpin.webservice.service.ErpScheduleJobService")
@Transactional
public class ErpScheduleJobService extends BaseService {

    Logger log = Logger.getLogger("ErpScheduleJobService");
    Logger dealLog = Logger.getLogger("dealReport");
    Logger jyErrLog = Logger.getLogger("jyErrorInfo"); //1）金域返回的会员信息与数据库中的会员不一致，2）会员报告无法下载
    Logger jyNoReportLog = Logger.getLogger("jyNoReport"); //数据库中有该会员，但金域没有报告的

    @Autowired
    private ErpScheduleJobDao dao;

    @Autowired
    private ErpReportOrgJYService orgJYService;

    @Autowired
    private ErpReportScheduleJYService scheduleJYService;

    @Autowired
    private ErpReportUrlJYService urlJYService;

    @Autowired
    private GeneCustomerService customerService;

    @Autowired
    private ErpEventsService eventsService;

    @Autowired
    private ErpReportdetailPDFContentService pdfContentService;

    @Autowired
    private ErpReportDetailService reportDetailService;

    @Autowired
    private ErpMessagePushService messagePushService;

    @Autowired
    private YmGeneReportServiceImpl ymGeneReportService;

    public List<ErpScheduleJob> listScheduleJobByProps(Map<String, String> params) {
        return dao.listScheduleJobByProps(params);
    }

    /**
     * 转换原始数据
     */
    public synchronized void transferOrgData() {
        log.info("开始执行transferOrgData方法...");
        //1. 查询数据库中没有转化的原始数据
        Map<String, String> orgParams = new HashMap<String, String>();
        //未处理状态(未添加到定时任务状态)
        orgParams.put(ErpReportOrgJY.F_STATUS, "0");
        try {
            log.info("开始查询未转换的原始数据...");
            List<ErpReportOrgJY> orgList = orgJYService.listByProps(orgParams);
            log.info("查询未转换的原始数据数量： " + orgList.size());
            ErpReportOrgJY orgJYObj;
            if (orgList != null && orgList.size() > 0) {
                for (int i = 0; i < orgList.size(); i++) {
                    orgJYObj = orgList.get(i);
                    //2.原始数据转化为定时任务数据
                    scheduleJYService.transferOrgData(orgJYObj);
                    //设置为已处理状态(已添加到定时任务的状态)
                    orgJYObj.setStatus(1);
                    //更新状态
                    orgJYService.update(orgJYObj);
                }
            }
        } catch (Exception e) {
            log.info(e);
        }
        log.info("transferOrgData方法执行完毕...");
    }

    /**
     * 下载报告/报告单
     */
    public synchronized void dealReport() {
        log.info("开始执行dealReport方法...");
        // 1. 找出未下载全的报告
        List<ErpReportScheduleJY> scheduleJYList = this.findUnDealReport();
        // 2. 根据URL地址下载报告
        if (!CollectionUtils.isEmpty(scheduleJYList)) {
            log.info("查询未执行的定时任务数量: " + scheduleJYList.size());
            String savePath;
            String fileName;
            String url;
            //获取当前日期
            String nowDate = Tools.getTimeStr(Tools.DATE_FORM_SIMPLE_NO_LINE);
            Map<String, String> urlParams = new HashMap<String, String>();
            //子表
            List<ErpReportUrlJY> urlJYList = null;
            //会员
            ErpCustomer customer = null;
            List<ErpCustomer> customerList;
            //场次
            ErpEvents events = null;
            List<ErpEvents> eventsList;
            //查询条件Map
            Map<String, String> params;
            //文件夹后缀名
            String suffix;
            for (ErpReportScheduleJY scheduleJY : scheduleJYList) {
                //下载成功的数量
                int okNum = 0;
                //HTTP状态码
                String httpCode = null;
                String tipMsg = null;
                //文件保存的物理地址
                String filePath;
                //下载结果
                Map<String, String> result;
                //获取URL信息
                //urlJYList = scheduleJY.getReportUrlList();
                urlParams.clear();
                urlParams.put(ErpReportUrlJY.F_IDRELATED, scheduleJY.getId());
                urlParams.put(ErpReportUrlJY.F_ISDELETED, "0");
                try {
                    urlJYList = urlJYService.listByProps(urlParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("定时任务[" + scheduleJY.getName() + "," + scheduleJY.getCode() + "]的链接地址数量：" + urlJYList.size());
                if (!CollectionUtils.isEmpty(urlJYList)) {
                    int numUrl = 0;
                    for (ErpReportUrlJY urlObj : urlJYList) {
                        if (urlObj.getStatus() == 0&&urlObj.getIsDeleted()==0) {
                            savePath = null;
                            url = urlObj.getUrl();
                            suffix = urlObj.getFileType();
                            //根据后缀名区分报告和报告单保存在不同的文件夹中
                            if ("pdf".equalsIgnoreCase(suffix)) {
                                savePath = PropertiesUtils.getString("foreign","disk.no") + File.separator
                                        + PropertiesUtils.getString("foreign","dir.jyRp") + File.separator
                                        + nowDate + File.separator + scheduleJY.getBatchNo();
                                //文件名
                                fileName = urlObj.getCode() + "." + suffix;
                            } else {
                                //报告单: 按批次+套餐名分配
                                savePath = PropertiesUtils.getString("foreign","disk.no") + File.separator
                                        + PropertiesUtils.getString("foreign","dir.jyRpDetail") + File.separator
                                        + nowDate + File.separator + scheduleJY.getBatchNo() + File.separator + scheduleJY.getCombo();
                                numUrl +=1;
                                //文件名
                                fileName = urlObj.getCode()+"_"+ numUrl + "." + suffix;
                            }
                            log.info("开始执行下载...");
                            //下载
                            if(StringUtils.isEmpty(url)||StringUtils.isEmpty(fileName)||StringUtils.isEmpty(savePath)){
                                tipMsg = "url/fileName/savePath不完整: "+"[url: "+url+"], [fileName: "+fileName+"], [savePath: "+savePath+"]";
                                log.info("url/fileName/savePath不完整: "+"[url: "+url+"], [fileName: "+fileName+"], [savePath: "+savePath+"]");
                                continue;
                            }
                            result = downloadByUrl(url, fileName, savePath);
                            log.info("下载执行完毕...");
                            if (!CollectionUtils.isEmpty(result)){
                                //获取提示信息
                                tipMsg = "处理结果:["+result.get("msg") + "], 保存路径:[" +result.get(fileName)+"]";
                                //获取状态码
                                httpCode = result.get(HttpUtils.CODE);
                            }
                            log.info("状态码：" + httpCode);
                            // 3. 根据下载结果更新 ERP_REPORT_SCHEDULE_JY 和 ERP_REPORT_URL_JY 表的信息
                            //修改 erp_report_url_jy表中的状态
                            if (httpCode != null) {
                                urlObj.setHttpCode(Integer.valueOf(httpCode));
                            }
                            //下载成功
                            if ("200".equals(httpCode)) {
                                //获取文件保存的物理地址
                                filePath = result.get(fileName);
                                //文件大小
                                Integer fileSize = Integer.valueOf(result.get("fileSize"));
                                //更新为已下载状态
                                urlObj.setStatus(1);

                                params = new HashMap<String, String>();
                                params.put(ErpCustomer.F_CODE, urlObj.getCode());
                                params.put(ErpCustomer.F_NAME, urlObj.getName());
                                params.put(ErpCustomer.F_ISDELETED, "0");

                                try {
                                    customerList = customerService.listCustomerByProps(params);
                                    if (!CollectionUtils.isEmpty(customerList)) {
                                        customer = customerList.get(0);
                                    }
                                    //清除
                                    params.remove(ErpCustomer.F_NAME);
                                    params.remove(ErpCustomer.F_CODE);
                                    //按照场次号查询
                                    params.put(ErpReportScheduleJY.F_EVENTSNO, scheduleJY.getEventsNo());
                                    eventsList = eventsService.listEventsByProps(params);
                                    if (!CollectionUtils.isEmpty(eventsList)) {
                                        events = eventsList.get(0);
                                    }
                                } catch (Exception e) {
                                    tipMsg = e.getMessage();
                                    log.info(e.getMessage());
                                }
                                if ("pdf".equalsIgnoreCase(suffix)) {
                                    //更新 ERP_CUSTOMER表的预览路径
                                    //更新预览地址
                                    if(StringUtils.isNotEmpty(filePath)) {
                                        String viewPath = filePath.replace(PropertiesUtils.getString("foreign","disk.no")
                                                                            ,PropertiesUtils.getString("foreign","viewPath_head"));
                                        log.info("["+customer.getCode()+" , "+ customer.getName()+"]的报告预览路径["+viewPath+"]");
                                        customer.setPdffilepath(viewPath);
                                        //把客户状态变更为 电子报告状态已出 add by Dayton 2016-12-21
                                        customer.setStatusYm(PropertiesUtils.getInt("status","statusYm.ycj"));
                                        //更新会员信息
                                        customerService.update(customer);
                                    }
                                } else {
                                    //添加到报告单明细表
                                    if (customer != null && events != null) {
                                        this.saveErpReportDetail(customer, events, fileName, filePath, scheduleJY.getReportId(), reportDetailService);
                                    }
                                }
                                //添加到打印任务中
                                //this.deal4PrintTask(filePath, fileSize, customer, events, pdfContentService, taskContentService);
                                boolean doneFlag = pdfContentService.deal4PrintTask(filePath, fileSize, customer, events); // modify 2017-01-01
                                if(doneFlag){
                                    log.info("["+customer.getCode()+" , "+ customer.getName()+"] 已添加到打印任务!!!");
                                }else{
                                    log.info("["+customer.getCode()+" , "+ customer.getName()+"] 添加到打印任务失败...");
                                }
                            }
                            urlObj.setRemark(tipMsg);
                            urlObj.setUpdateUserId("0");
                            urlObj.setUpdateUserName("websGene");
                            urlObj.setUpdateTime(Calendar.getInstance().getTime());

                            //计数
                            Integer urlCounter = 0;
                            if(urlObj.getCounter()==null){
                                urlCounter += 1;
                            }else{
                                urlCounter = urlObj.getCounter()+1;
                            }
                            urlObj.setCounter(urlCounter);

                            urlJYService.update(urlObj);

                        }else{
                            log.info("["+urlObj.getCode()+","+urlObj.getName()+"] 附件已下载，不再重复下载...");
                        }
                        //已下载的状态
                        if (urlObj.getStatus() == 1) {
                            okNum += 1;
                        }
                        //如果全部的报告和报告单都已下载,则修改 erp_report_schedule_jy 表中对应的状态
                        if (okNum == urlJYList.size()) {
                            //更新状态为已全部处理
                            scheduleJY.setStatus(1);
                        }
                    }
                }
                //更新定时任务表
                Integer times = scheduleJY.getCounter()==null?0:scheduleJY.getCounter()+1;

                scheduleJY.setCounter(times);
                scheduleJY.setUpdateUserName("websGene");
                scheduleJY.setUpdateUserId("0");
                scheduleJY.setUpdateTime(Calendar.getInstance().getTime());
                log.info("更新定时任务: "+scheduleJY.toString());
                scheduleJYService.update(scheduleJY);
                log.info("定时任务: "+scheduleJY.toString()+ " 更新成功!!!");
            }
        }else{
            log.info("定时任务!");
        }
        log.info("dealReport方法执行完毕...");
    }

    /**
     * @description 查找未处理的定时任务
     * @author YoumingDeng
     * @since: 2016/12/1 14:55
     */
    private List<ErpReportScheduleJY> findUnDealReport() {
        List<ErpReportScheduleJY> list = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put(ErpReportScheduleJY.F_STATUS, "0");
        try {
            list = scheduleJYService.listByProps(params);
            if(!CollectionUtils.isEmpty(list)){

            }
        } catch (Exception e) {
            log.info(e);
        }
        return list;
    }

    /**
     * @description 根据信息下载报告/报告单
     * @author YoumingDeng
     * @since: 2016/12/1 14:43
     */
    private Map<String, String> downloadByUrl(String url, String fileName, String savePath) {
        Map<String, String> result = null;
        if (StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(fileName) && StringUtils.isNotEmpty(savePath)) {
            DownloadThread downloadThread = new DownloadThread(url, fileName, savePath);
            ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) SpringTool.getBean("taskExecutor");
            Future<Map<String, String>> future = taskExecutor.submit(downloadThread);
            try {
                result = future.get();
            } catch (InterruptedException e) {
                log.info(e);
            } catch (ExecutionException e) {
                log.info(e);
            }
        }
        return result;
    }

    /**
     * @description 保存报告单明细
     * @author YoumingDeng
     * @since: 2016/12/1 19:53
     */
    private boolean saveErpReportDetail(ErpCustomer entity, ErpEvents events, String fileName, String reportDetailPath, String reportId,
                                        ErpReportDetailService reportDetailService) {
        boolean flag;
        Map<String, String> params = new HashMap<String, String>();
        ErpReportDetail reportDetailObj;
        List<ErpReportDetail> detailList;
        params.clear();
        params.put(ErpReportDetail.F_CODE, entity.getCode());
        params.put(ErpReportDetail.F_FILENAME, fileName);
        detailList = reportDetailService.listRerortDetailByProps(params);
        if (!CollectionUtils.isEmpty(detailList)) {
            reportDetailObj = detailList.get(0);

            reportDetailObj.setFilePath(reportDetailPath);//物理地址

            String suffix = Tools.getSuffix(reportDetailPath);
            reportDetailObj.setFileSuffix(suffix);//后缀名

            //预览地址
            String viewPathDetail = reportDetailPath.replace(PropertiesUtils.getString("foreign","disk.no")
                    ,PropertiesUtils.getString("foreign","viewPath_head"));
            log.info("["+entity.getCode()+" , "+ entity.getName()+"]的报告单预览路径["+viewPathDetail+"]");
            reportDetailObj.setViewPath(viewPathDetail);//预览地址

            reportDetailObj.setReportId(reportId);//金域报告ID，可用于重新获取报告
            reportDetailObj.setUpdateTime(Calendar.getInstance().getTime());
            //更新
            reportDetailService.update(reportDetailObj);
            dealLog.info("方法[saveReportDetail]，场次号[" + events.getEventsNo() + "]，批次号[" + events.getBatchNo() + "]，团单号[" + events.getGroupOrderNo() + "]，" +
                    " 姓名[" + entity.getName() + "]， 条码[" + entity.getCode() + "]，报告单[" + fileName + "]，保存位置[" + reportDetailPath + "]，已更新到报告单明细表!");
            flag = true;
        } else {
            //保存报告单信息
            reportDetailObj = new ErpReportDetail();

            reportDetailObj.setFileName(fileName);//文件名
            reportDetailObj.setFilePath(reportDetailPath);//物理地址
            String suffix = Tools.getSuffix(reportDetailPath);
            reportDetailObj.setFileSuffix(suffix);//后缀名

            //预览地址
            String viewPathDetail = reportDetailPath.replace(PropertiesUtils.getString("foreign","disk.no")
                    ,PropertiesUtils.getString("foreign","viewPath_head"));
            log.info("["+entity.getCode()+" , "+ entity.getName()+"]的报告单预览路径["+viewPathDetail+"]");

            reportDetailObj.setViewPath(viewPathDetail);//预览地址
            reportDetailObj.setCustomerId(entity.getId());

            reportDetailObj.setName(entity.getName());
            reportDetailObj.setCode(entity.getCode());
            reportDetailObj.setGender(entity.getSex());
            reportDetailObj.setEventsNo(events.getEventsNo());
            reportDetailObj.setBatchNo(events.getBatchNo());

            reportDetailObj.setGroupOrderNo(events.getGroupOrderNo());
            reportDetailObj.setCreateTime(Calendar.getInstance().getTime());
            reportDetailObj.setReportId(reportId);//金域报告ID，可用于重新获取报告
            reportDetailObj.setStatus(0);//初始状态默认为0

            //保存
            reportDetailService.saveReportDetail(reportDetailObj);
            dealLog.info("方法[saveReportDetail]，场次号[" + events.getEventsNo() + "]，批次号[" + events.getBatchNo() + "]，团单号[" + events.getGroupOrderNo() + "]，" +
                    " 姓名[" + entity.getName() + "]， 条码[" + entity.getCode() + "]，报告单[" + fileName + "]，保存位置[" + reportDetailPath + "]，已保存到报告单明细表!");
            flag = true;
        }
        return flag;
    }


    /**
     * @description 处理不匹配的定时任务,重新与会员表数据进行匹配
     * @author YoumingDeng
     * @since: 2016/12/8 12:20
     */
    public void dealUnmatchScheduleJY(String statusStr){
        try {
            Integer[] arr;
            if(StringUtils.isNotEmpty(statusStr)) {
                log.info("正常状态："+statusStr);
                if(statusStr.contains(",")){
                   String[] arrStr = statusStr.split(",");
                   arr = new Integer[arrStr.length];
                   for (int i=0; i<arrStr.length; i++){
                       arr[i] = Integer.valueOf(arrStr[i]);
                   }
                }else{
                    arr = new Integer[]{Integer.valueOf(statusStr)};
                }
                Integer updateNum = scheduleJYService.updateUnmatchSechedule(arr);
                log.info("本次处理的异常数据条数: " + updateNum);
            }
        } catch (Exception e) {
            log.info(e);
        }
    }

    /**
     * 给弘康推送客户状态变更
     */
    public void pushStatusToHK(){
        Logger log = Logger.getLogger("pushCustomerStatus");
        Map<String,String> props = new HashMap<String, String>();
        props.put(ErpMessagePush.F_STATUS, "0");
//        props.put(ErpMessagePush.F_KEYWORD, "HK");// 添加关键字查询，用于区分推送不同的方向 add Damian 2017-02-17
        try {
            List<ErpMessagePush> list = messagePushService.listByProps(props);
            if(!CollectionUtils.isEmpty(list)) {
                String xml;
                String respXml;
                log.info("本次要推送的消息数量： "+list.size());
                for (ErpMessagePush messagePush : list) {
                    xml = messagePush.getMessage();
                    log.info("推送表中取到的消息xml: "+xml);
                    respXml = ymGeneReportService.pushCustomerStatus(xml);
                    log.info("微服务返回的xml: "+respXml);
                    String status = this.fetchRespStatusHK(respXml);
                    //成功
                    if("1".equals(status)){
                        messagePush.setUpdateTime(Calendar.getInstance().getTime());
                        messagePush.setUpdateUserName("websGene");
                        messagePush.setStatus(1); // 1:已推送
                        //更新
                        messagePushService.update(messagePush);
                    }
                }
            }else {
                log.info("本次没有消息推送...");
            }
        } catch (Exception e) {
            log.info(e);
        }
    }

    private String fetchRespStatusHK(String xml){
        Logger log = Logger.getLogger("pushCustomerStatus");
        String result= "0";
        if(StringUtils.isNotEmpty(xml)){
            try {
                Map<String,String> respMap = XmlUtils.fetchXmlValue(xml, "respStatus", new String[]{"result","message"});
                result= respMap.get("result");
            } catch (Exception e) {
                log.info(e);
            }
        }
        return result;
    }
}










