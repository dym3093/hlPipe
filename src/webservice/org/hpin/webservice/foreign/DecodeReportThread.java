package org.hpin.webservice.foreign;/**
 * Created by admin on 2016/11/28.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.ErpReportOrgJYService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 解析金域的报告信息
 *
 * @author YoumingDeng
 * @create 2016-11-28 14:44
 */
public class DecodeReportThread implements Callable<Map<String,String>>{

    //日志
    Logger log = Logger.getLogger(DecodeReportThread.class);

    private String report;

    public DecodeReportThread(String report) {
        setReport(report);
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public Map<String, String> call() throws Exception {
        Map<String,String> map = null;
        if (StringUtils.isNotEmpty(getReport())){
            log.info("接收到的报告数据："+getReport());
            map = this.dealReport(getReport());
        }
        return map;
    }

    private Map<String,String> dealReport(String report){
        Map<String,String> map = new HashMap<String, String>();
        ErpReportOrgJYService jyService = (ErpReportOrgJYService) SpringTool.getBean(ErpReportOrgJYService.class);
        boolean flag = false;
        try {
            log.info("开始保存金域传送的原始报告数据...");
            flag = jyService.saveFromJY(report);
            if(flag) {
                log.info("原始报告数据保存成功...");
            }else {
                log.info("原始报告数据保存失败...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag){
            map.put("code","200");
            map.put("msg","保存成功!");
        }else{
            map.put("code","500");
            map.put("msg","保存失败!");
        }
        //1. 校验报告数据
        //2. 是否匹配报告
        //3. 不管是否匹配，都保存到定时任务表
        return map;
    }

}
