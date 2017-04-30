package org.hpin.webservice.service;/**
 * Created by admin on 2016/11/29.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.jz.ErpReportOrgJY;
import org.hpin.webservice.bean.jz.ErpReportScheduleJY;
import org.hpin.webservice.bean.jz.ErpReportUrlJY;
import org.hpin.webservice.dao.ErpReportScheduleJYDao;
import org.hpin.webservice.util.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 定时获取报告任务表(金域)Service
 *
 * @author YoumingDeng
 * @create 2016-11-29 21:08
 */
@Service("org.hpin.webservice.service.ErpReportScheduleJYService ")
@Transactional
public class ErpReportScheduleJYService extends BaseService{

    Logger log = Logger.getLogger("ErpReportScheduleJY");

    @Autowired
    private ErpReportScheduleJYDao dao;

    @Autowired
    private ErpReportUrlJYService urlJYService;

    @Autowired

    private GeneCustomerService customerService;

    @Autowired
    private ErpEventsService eventsService;

    @Autowired
    private ErpReportOrgJYService orgJYService;
    /**
     * 根据条件精确查询
     * @param params 查询条件
     * @return List
     * @throws Exception
     */
    public List<ErpReportScheduleJY> listByProps(Map<String, String> params) throws Exception{
        return dao.listScheduleJobByProps(params, true);
    }

    /**
     * 转换原始数据到定时任务表和URL信息表
     * @param entity 原始数据表
     */
    public void transferOrgData(ErpReportOrgJY entity){
        //原始数据转换为定时任务
        log.info("开始转换原始数据:  "+entity.toString());
        ErpReportScheduleJY scheduleJY = this.orgDataToSechedule(entity);

        if (scheduleJY!=null) {
            log.info("原始数据转换完成，定时任务：  "+scheduleJY.toString());
            //清除旧的定时任务数据
            boolean flag = dao.cleanOld(scheduleJY.getCode(), scheduleJY.getName());

            log.info("清除旧的定时任务：" + flag);
            if(flag) {
                //保存
                log.info("开始保存定时任务：" + scheduleJY.toString());
                dao.save(scheduleJY);
                log.info("定时任务：" + scheduleJY.toString() + " 保存成功!!!");

                //清除旧的URL数据
                boolean urlFlag = urlJYService.cleanOldUrl(scheduleJY.getCode(), scheduleJY.getName());
                log.info("清除旧的URL：" + urlFlag);
                if (urlFlag) {
                    //PDF报告的URL
                    String pdfUrl = entity.getPdfUrl();
                    log.info("获取PDF报告地址：" + pdfUrl);
                    //报告的URL信息
                    ErpReportUrlJY urlJYPdf;
                    //定时任务转换成报告地址表
                    log.info("[" + pdfUrl + "] 转换成地址表... ");
                    urlJYPdf = trasferToUrlInfo(scheduleJY, pdfUrl);
                    log.info("转换地址表完成: " + urlJYPdf.toString());
                    //保存
                    if (urlJYPdf != null) {
                        log.info("开始保存地址表...");
                        urlJYService.save(urlJYPdf);
                        log.info("保存地址表完成!!!");
                    }
                    try {

                        //报告单
                        ErpReportUrlJY urlJYPic;
                        //报告单URL
                        String rawResults = entity.getRawResults();
                        JSONArray rawArr = new JSONArray(rawResults);
                        String urlStr;
                        for (int i = 0; i < rawArr.length(); i++) {
                            //URL
                            urlStr = rawArr.getString(i);
                            //定时任务转换成报告地址表
                            log.info("[" + urlStr + "] 转换成地址表... ");
                            urlJYPic = trasferToUrlInfo(scheduleJY, urlStr);
                            log.info("转换地址表完成: " + urlJYPdf.toString());
                            if (urlJYPic != null) {
                                log.info("开始保存地址表...");
                                urlJYService.save(urlJYPic);
                                log.info("保存地址表完成: " + urlJYPic.toString());
                            }
                        }
                    } catch (JSONException e) {
                        log.info(e);
                    } catch (Exception e) {
                        log.info(e);
                    }

                    //更新原始数据信息
                    Integer count = entity.getCounter() + 1;

                    entity.setCounter(count);
                    entity.setUpdateTime(Calendar.getInstance().getTime());
                    entity.setUpdateBy("websGene");
                    entity.setUpdateUserId("0");
                    entity.setStatus(1);//已处理状态
                    orgJYService.update(entity);
                    log.info("更新原始数据信息：" + entity.toString());
                }
            }

        }else {
            log.info("原始数据转换错误: "+entity.toString());
        }
    }

    /**
     * 转换原始数据到定时任务表和URL信息表
     * @param entity 原始数据表
     */
    /**
    public void transferOrgData_old(ErpReportOrgJY entity){
        //原始数据转换为定时任务
        log.info("开始转换原始数据:  "+entity.toString());
        ErpReportScheduleJY scheduleJY = this.orgDataToSechedule(entity);
        if (scheduleJY!=null) {
            log.info("原始数据转换完成，定时任务：  "+scheduleJY.toString());
            Map<String,String> scheduleMap = new HashMap<String, String>();
            scheduleMap.put(ErpReportScheduleJY.F_CODE, scheduleJY.getCode());
            scheduleMap.put(ErpReportScheduleJY.F_NAME, scheduleJY.getName());
            List<ErpReportScheduleJY> scheduleJYList = null;
            try {
                log.info("根据[code="+scheduleJY.getCode()+",name="+scheduleJY.getName()+"]查询定时任务...");
                scheduleJYList = dao.listByProps(scheduleMap);
                log.info("根据[code="+scheduleJY.getCode()+",name="+scheduleJY.getName()+"]查询定时任务完成!!!");
            } catch (Exception e) {
                log.info(e);
            }
            log.info("scheduleJYList size: "+scheduleJYList.size());
            //定时任务中没有，则保存
            if(CollectionUtils.isEmpty(scheduleJYList)){
                //保存
                log.info("开始保存定时任务："+scheduleJY.toString());
                dao.save(scheduleJY);
                log.info("定时任务："+scheduleJY.toString()+" 保存成功!!!");

                //更新原始数据信息
                Integer count = entity.getCounter()+1;

                entity.setCounter(count);
                entity.setUpdateTime(Calendar.getInstance().getTime());
                entity.setUpdateBy("websGene");
                entity.setUpdateUserId("0");
                entity.setStatus(1);//已处理状态
                orgJYService.update(entity);
                log.info("更新原始数据信息："+entity.toString());

            }else {
                scheduleJY = scheduleJYList.get(0);
            }
            //PDF报告的URL
            String pdfUrl = entity.getPdfUrl();
            log.info("获取PDF报告地址："+pdfUrl);
            //报告的URL信息
            ErpReportUrlJY urlJYPdf = null;
            Map<String,String> urlParams = new HashMap<String, String>();
            urlParams.put(ErpReportUrlJY.F_CODE, entity.getBarcode());
            urlParams.put(ErpReportUrlJY.F_NAME, entity.getUserName()); // add by Damian 2016-12-29
            urlParams.put(ErpReportUrlJY.F_URL, pdfUrl);
           //根据URL和条码查找URL表中是否已存在数据
            try {
                List<ErpReportUrlJY> urlPdfList = urlJYService.listByProps(urlParams);
                if(CollectionUtils.isEmpty(urlPdfList)) {
                    //定时任务转换成报告地址表
                    log.info("["+pdfUrl+"] 转换成地址表... ");
                    urlJYPdf = trasferToUrlInfo(scheduleJY, pdfUrl);
                    log.info("转换地址表完成: "+urlJYPdf.toString());
                   //保存
                    if(urlJYPdf!=null) {
                        log.info("开始保存地址表...");
                        urlJYService.save(urlJYPdf);
                        log.info("保存地址表完成!!!");
                    }
                }

                //报告单
                ErpReportUrlJY urlJYPic;
                List<ErpReportUrlJY> existPicList;
                //报告单URL
                String rawResults = entity.getRawResults();
                JSONArray rawArr = new JSONArray(rawResults);
                String urlStr;
                for (int i = 0; i < rawArr.length(); i++) {
                    //移除之前的URL
                    urlParams.remove(ErpReportUrlJY.F_URL);
                    //URL
                    urlStr = rawArr.getString(i);
                    urlParams.put(ErpReportUrlJY.F_URL, urlStr);
                    //查询
                    existPicList = urlJYService.listByProps(urlParams);
                    //不存在则保存
                    if(CollectionUtils.isEmpty(existPicList)){
                        //定时任务转换成报告地址表
                        log.info("["+urlStr+"] 转换成地址表... ");
                        urlJYPic = trasferToUrlInfo(scheduleJY, urlStr);
                        log.info("转换地址表完成: "+urlJYPdf.toString());
                        if(urlJYPic!=null) {
                            log.info("开始保存地址表...");
                            urlJYService.save(urlJYPic);
                            log.info("保存地址表完成: "+urlJYPic.toString());
                        }
                    }
                }
            } catch (JSONException e) {
                log.info(e);
            } catch (Exception e) {
                log.info(e);
            }
        }else {
            log.info("原始数据转换错误: "+entity.toString());
        }
    }*/

    /**
     * 原始报告单信息转换成定时任务信息表
     * @param orgObj 原始报告单信息表
     * @return ErpReportScheduleJY
     */
    private ErpReportScheduleJY orgDataToSechedule(ErpReportOrgJY orgObj){
        ErpReportScheduleJY scheduleJY = null;
        if(orgObj!=null) {
            String name = orgObj.getUserName();//会员姓名
            String code = orgObj.getBarcode();//会员条码
            //数据匹配情况标志
            Integer status = 9;//数据库会员信息不匹配
            //提示信息
            StringBuilder tipMsg = new StringBuilder("信息不匹配");
            //场次信息
            ErpEvents events = null;
            //数据库中的会员
            ErpCustomer customer = null;
            //添加查询条件
            Map<String,String> params = new HashMap<String, String>();
            params.put(ErpCustomer.F_NAME, name);
            params.put(ErpCustomer.F_CODE, code);
            params.put(ErpCustomer.F_ISDELETED, "0");//未删除的数据
            try {
                List<ErpCustomer> customerList = customerService.listCustomerByProps(params);
                if(!CollectionUtils.isEmpty(customerList)){
                    if(customerList.size()==1){
                        //根据条码姓名查到1条有效数据
                        customer = customerList.get(0);
                        if(customer!=null) {
                            //根据条码查询场次信息
                            List<ErpEvents> eventsList = eventsService.listEventsByInfo(code, "code");
                            if (!CollectionUtils.isEmpty(eventsList)) {
                                //且对应一个场次
                                if (eventsList.size() == 1) {
                                    events = eventsList.get(0);
                                    if(events!=null){
                                        tipMsg = new StringBuilder("信息匹配");
                                        status = 0; //信息匹配
                                    }
                                }
                            }
                        }
                    }
                }

                /**
                else {
                    //没有数据库中没有该会员
                    status = 5;
                    tipMsg = new StringBuilder("["+orgObj.getBarcode()+","+orgObj.getUserName()+" ] 该会员在数据库中不存在");
                }
                //根据条码查询场次信息
                List<ErpEvents> eventsList = eventsService.listEventsByInfo(code, "code");
                if (!CollectionUtils.isEmpty(eventsList)) {
                    if (eventsList.size() == 1) {
                        events = eventsList.get(0);
                    } else {
                        tipMsg.append("["+orgObj.getBarcode()+","+orgObj.getUserName()+" ] 该会员归属多个场次");
                        status = 7;//该会员归属多个场次
                    }
                } else {
                    tipMsg.append("["+orgObj.getBarcode()+","+orgObj.getUserName()+" ] 该会员没有所属场次");
                    status = 6; //该会员没有归属场次
                }
                 */

                scheduleJY = new ErpReportScheduleJY();

                scheduleJY.setIdRelated(orgObj.getId());
                scheduleJY.setCode(orgObj.getBarcode());
                scheduleJY.setName(orgObj.getUserName());

                scheduleJY.setGender(orgObj.getGender());
                scheduleJY.setBirthday(orgObj.getBirthday());
                scheduleJY.setPhone(orgObj.getPhone());

                if(customer!=null){
                    scheduleJY.setCombo(customer.getSetmealName());
                    scheduleJY.setEventsNo(customer.getEventsNo());
                    scheduleJY.setIdNo(customer.getIdno());
                }

                if(events!=null) {
                    scheduleJY.setBatchNo(events.getBatchNo());
                    scheduleJY.setGroupOrderNo(events.getGroupOrderNo());
                }

                scheduleJY.setReportId(orgObj.getReportId());
                scheduleJY.setReportName(orgObj.getReportName());

                String samplingAtStr = orgObj.getSamplingAt();
                if(StringUtils.isNotEmpty(samplingAtStr)) {
                    Date samplingDate = Tools.getDateFromStr(samplingAtStr, Tools.DATE_FORM_SIMPLE);
                    scheduleJY.setSamplingDate(samplingDate);
                }

                String entryDateStr = orgObj.getEntryAt();
                if(StringUtils.isNotEmpty(entryDateStr)) {
                    Date entryDate = Tools.getDateFromStr(entryDateStr, Tools.DATE_FORM_SIMPLE);
                    scheduleJY.setEntryDate(entryDate);
                }

                String publishedDateStr = orgObj.getPublishedAt();
                if(StringUtils.isNotEmpty(publishedDateStr)) {
                    Date publishedDate = Tools.getDateFromStr(publishedDateStr, Tools.DATE_FORM_SIMPLE);
                    scheduleJY.setPublishedDate(publishedDate);
                }

                scheduleJY.setIsDeleted(0);
                scheduleJY.setCreateTime(Calendar.getInstance().getTime());
                scheduleJY.setCreateUserId("0");
                scheduleJY.setCreateUserName("金域");

                //备注信息
                scheduleJY.setRemark(tipMsg.toString());
                scheduleJY.setStatus(status);
                scheduleJY.setCounter(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return scheduleJY;
    }

    /**
     * 根据定时任务表和URL转换成URL信息表
     * @param entity 定时任务表
     * @param url URL地址
     * @return ErpReportUrlJY
     */
    private static ErpReportUrlJY trasferToUrlInfo(ErpReportScheduleJY entity, String url){
        ErpReportUrlJY obj = null;
        if(entity!=null&&StringUtils.isNotEmpty(url)){
            obj = new ErpReportUrlJY();

            obj.setIdRelated(entity.getId());
            obj.setCode(entity.getCode());
            obj.setName(entity.getName());
            obj.setPhone(entity.getPhone());

            String fileType = Tools.getSuffix(url);
            obj.setFileType(fileType);
            obj.setUrl(url);

            obj.setIsDeleted(0);
            obj.setStatus(0);//初始状态为0
            obj.setCreateTime(Calendar.getInstance().getTime());
            obj.setCreateUserId(entity.getCreateUserId());
            obj.setCreateUserName(entity.getCreateUserName());

            obj.setCounter(0);
        }
        return obj;
    }

    /**
     * @description 定时修改异常的定时任务
     * @param arr 定时任务的状态
     * @Integer 修改的条数
     * @author YoumingDeng
     * @since: 2016/12/8 10:29
     */
    public Integer updateUnmatchSechedule(Integer[] arr)throws Exception{
        Integer updateNum = 0;
        Integer[] statusArr = null;
        if(arr!=null&&arr.length>0){
            statusArr = arr;
        }else {
            statusArr = new Integer[]{0, 1};
        }
        log.info("状态列表"+Arrays.toString(statusArr));
        log.info("根据状态列表查询不在状态列表内的定时任务...");
        log.info("开始查询金域定时任务...");
        //查询不匹配的定时任务
        List<ErpReportScheduleJY> list = dao.listByStatus(statusArr, "notIn");
        log.info("查询金域定时任务结束, 定时任务记录条数["+ list.size()+"]");
        if(!CollectionUtils.isEmpty(list)){
            //默认状态
            Integer status ;
            String remark ;
            for (ErpReportScheduleJY entiy: list) {
                status = 9;
                remark = "[name="+entiy.getName()+", code="+entiy.getCode()+"]定时任务信息已更新,信息不匹配!!!";
                //根据姓名和条码查询
                Map<String, String> params = new HashMap<String, String>();
                params.put(ErpCustomer.F_NAME, entiy.getName());
                params.put(ErpCustomer.F_CODE, entiy.getCode());
                params.put(ErpCustomer.F_ISDELETED, "0");//未删除的数据
                log.info("根据[name="+entiy.getName()+", code="+entiy.getCode()+"]查询会员信息...");
                List<ErpCustomer> customerList = customerService.listCustomerByProps(params);
                log.info("根据[name="+entiy.getName()+", code="+entiy.getCode()+"]查询完成！！！");
                ErpCustomer customer = null;
                ErpEvents events = null;
                if(!CollectionUtils.isEmpty(customerList)) {
                    if(customerList.size()==1){
                        customer = customerList.get(0);
                        Map<String,String> eventsQuery = new HashMap<String, String>();
                        eventsQuery.put(ErpEvents.F_EVENTSNO, customer.getEventsNo());
                        eventsQuery.put(ErpEvents.F_ISDELETED, "0");
                        List<ErpEvents> eventsList = eventsService.listEventsByProps(eventsQuery);
                        if(!CollectionUtils.isEmpty(eventsList)){
                            events = eventsList.get(0);
                        }
                        //满足该条件，则为匹配
                        status = 0;
                        remark = "[name="+entiy.getName()+", code="+entiy.getCode()+"]定时任务信息已更新,信息匹配";
                    }
                }
                //计数
                //Integer counter = entiy.getCounter()==null?0:entiy.getCounter() + 1;
                //更新信息
                if(events!=null) {
                    entiy.setBatchNo(events.getBatchNo());
                    entiy.setEventsNo(events.getEventsNo());
                    entiy.setGroupOrderNo(events.getGroupOrderNo());
                }
                if(customer!=null){
                    entiy.setIdNo(customer.getIdno());
                    entiy.setCombo(customer.getSetmealName());
                }
                //更新状态
                //entiy.setCounter(counter);
                entiy.setStatus(status);
                entiy.setRemark(remark);
                entiy.setUpdateTime(Calendar.getInstance().getTime());
                entiy.setUpdateUserId("0");
                entiy.setUpdateUserName("websGene");
                this.update(entiy);
                log.info(remark);
                updateNum += 1;
            }
        }
        log.info("本次执行的人数 ["+updateNum+"] 人");
        return updateNum;
    }
}








