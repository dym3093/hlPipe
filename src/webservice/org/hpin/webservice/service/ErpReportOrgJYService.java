package org.hpin.webservice.service;/**
 * Created by admin on 2016/11/28.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.jz.ErpReportOrgJY;
import org.hpin.webservice.bean.jz.ErpReportScheduleJY;
import org.hpin.webservice.dao.ErpReportOrgJYDao;
import org.hpin.webservice.foreign.DecodeReportThread;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 原始报告信息（金域）Service
 *
 * @author YoumingDeng
 * @since 2016-11-28 14:17
 */
@Service("org.hpin.webservice.service.ErpReportOrgJYService")
@Transactional
public class ErpReportOrgJYService extends BaseService{

    Logger log = Logger.getLogger(DecodeReportThread.class);

    @Autowired
    private ErpReportOrgJYDao dao;

    @Autowired
    private ErpReportScheduleJYService scheduleJYService;
    /**
     * 保存来自金域接口的报告信息
     * @param report JSON字符串的报告信息
     * @return boolean true:保存成功，false:保存失败
     * @throws Exception
     */
    public boolean saveFromJY(String report) throws Exception{
        boolean flag = false;
        ErpReportOrgJY obj = null;
        System.out.println("report: "+report);
        if(StringUtils.isNotEmpty(report)){
           obj = convert2ReportObj(report);
           if(obj!=null) {

               Map<String,String> params = new HashMap<String, String>();
               params.put(ErpReportOrgJY.F_ISDELETED, "0");
               params.put(ErpReportOrgJY.F_BARCODE, obj.getBarcode());
               params.put(ErpReportOrgJY.F_USERNAME, obj.getUserName());
               //
               List<ErpReportOrgJY> orgJYList = dao.listByProps(params);
               if(!CollectionUtils.isEmpty(orgJYList)){
                   //原先的数据作废
                   ErpReportOrgJY existOrgJy = orgJYList.get(0);

                   Integer count = existOrgJy.getCounter()==null?0:existOrgJy.getCounter()+1;
                   existOrgJy.setIsDeleted(1);//设置删除状态
                   existOrgJy.setStatus(-1);//设置删除状态
                   existOrgJy.setCounter(count);
                   existOrgJy.setUpdateTime(Calendar.getInstance().getTime());
                   existOrgJy.setUpdateUserId("0");
                   existOrgJy.setUpdateBy("websGene");
                   log.info("修改表 ERP_REPORT_ORG_JY 数据： ["+ existOrgJy.getBarcode()+","+existOrgJy.getUserName()+"]");
                   dao.update(existOrgJy);
                   log.info("ERP_REPORT_ORG_JY 数据修改成功： ["+ existOrgJy.getBarcode()+","+existOrgJy.getUserName()+"]");

               }
               params.clear();
               params.put(ErpReportScheduleJY.F_NAME, obj.getBarcode());
               params.put(ErpReportScheduleJY.F_CODE, obj.getUserName());
               params.put(ErpReportScheduleJY.F_ISDELETED, "0");
               List<ErpReportScheduleJY> scheduleJYList = scheduleJYService.listByProps(params);
               if(!CollectionUtils.isEmpty(scheduleJYList)){
                   ErpReportScheduleJY scheduleJY = scheduleJYList.get(0);
                   scheduleJY.setIsDeleted(1);
                   scheduleJY.setUpdateTime(Calendar.getInstance().getTime());
                   scheduleJY.setUpdateUserId("0");
                   scheduleJY.setCreateUserName("websGene");
                   scheduleJY.setStatus(-1);
                   log.info("修改表 ERP_REPORT_SCHEDULE_JY数据： ["+ scheduleJY.getCode()+","+scheduleJY.getName()+"]");
                   scheduleJYService.update(scheduleJY);
                   log.info("ERP_REPORT_SCHEDULE_JY数据修改成功： ["+ scheduleJY.getCode()+","+scheduleJY.getName()+"]");
               }

               //设置默认值
               obj.setCreateId("0");
               obj.setCreateBy("金域");
               obj.setCreateTime(Calendar.getInstance().getTime());
               obj.setIsDeleted(0);
               obj.setStatus(0);
               obj.setCounter(0);
               log.info("转化后的原始数据对象: " + obj.toString());
               dao.saveReportOrgJy(obj);
               log.info("原始数据对象保存成功...");
               flag = true;
           }
        }
        log.info("flag: "+flag);
        return flag;
    }

    /**
     * 报告信息字符串转换成实体类
     * @param report 字符串报告信息
     * @return ErpReportOrgJY
     */
    private ErpReportOrgJY convert2ReportObj(String report){
        ErpReportOrgJY obj = null;
        try {
            JSONObject json = new JSONObject(report);
            if(json!=null){
                obj = new ErpReportOrgJY();

                obj.setReportId(json.getString("reportId"));
                obj.setReportName(json.getString("name"));
                obj.setBarcode(json.getString("barcode"));
                obj.setServiceId(json.getString("serviceId"));

                if(json.has("corServiceId")) {
                    obj.setCorServiceId(json.getString("corServiceId"));
                }
                if(json.has("corOwnerId")){
                    obj.setCorOwnerId(json.getString("corOwnerId"));
                }
                obj.setUserName(json.getString("username"));
                obj.setPhone(json.getString("phone"));
                if(json.has("gender")) {
                    obj.setGender(json.getString("gender"));
                }

                if(json.has("birthday")) {
                    String birthday = json.getString("birthday");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = sdf.parse(birthday);
                    obj.setBirthday(birthDate);
                }
                if(json.has("age")) {
                    obj.setAge(json.getInt("age"));
                }
                obj.setAgeUnit(json.getString("ageUnit"));
                obj.setItemCodes(json.getString("itemCodes"));
                obj.setSamplingAt(json.getString("samplingAt"));

                obj.setState(json.getInt("state"));
                obj.setEntryAt(json.getString("entryAt"));
                obj.setPublishedAt(json.getString("publishedAt"));
                if(json.has("rawResults")) {
                    JSONArray rawArr = json.getJSONArray("rawResults");
                    obj.setRawResults(json.getString("rawResults"));
                }

                String resultsStr = json.getString("results");
                //Clob resultsClob = Tools.strToClob(resultsStr);
                obj.setResults(resultsStr);

                obj.setPdfUrl(json.getString("pdfUrl"));
                obj.setWapShowUrl(json.getString("wapShowUrl"));
                obj.setWebShowUrl(json.getString("webShowUrl"));

                if(json.has("healthAdvice")) {
                    String healthAdviceStr = json.getString("healthAdvice");
                    if (StringUtils.isNotEmpty(healthAdviceStr)) {
                        //obj.setHealthAdvice(Tools.strToClob(healthAdviceStr));
                        obj.setHealthAdvice(healthAdviceStr);
                    }
                }

                if(json.has("anomalyIndex")) {
                    String anomalyIndexStr = json.getString("anomalyIndex");
                    if (StringUtils.isNotEmpty(anomalyIndexStr)) {
                        //obj.setAnomalyIndex(Tools.strToClob(anomalyIndexStr));
                        obj.setAnomalyIndex(anomalyIndexStr);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 根据条件查询
     * @param params 条件
     * @return List
     * @throws Exception
     */
    public List<ErpReportOrgJY> listByProps(Map<String,String> params) throws Exception{
        List<ErpReportOrgJY> list = null;
        if (params!=null&&!params.isEmpty()){
            list = dao.listByProps(params);
        }
        return list;
    }
}
