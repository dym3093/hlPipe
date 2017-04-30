package org.hpin.webservice.service;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.dao.ErpEventsDao;
import org.hpin.webservice.foreign.DownloadThread;
import org.hpin.webservice.foreign.TesteesThread;
import org.hpin.webservice.util.GeneratorUtils;
import org.hpin.webservice.util.HttpUtils;
import org.hpin.webservice.util.PropertiesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.jws.WebService;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 金域接口
 * Created by admin on 2016/11/8.
 */
@Service(value = "org.hpin.webservice.service.GeneServiceImplJY")
@WebService
public class GeneServiceImplJY implements GeneServiceJY{

    private ThreadPoolTaskExecutor taskExecutor;

    static Logger logNoRpt = Logger.getLogger("gainReport_noReport");
    /**
     * 创建团单号
     * @param jsonStr
     * @return
     */
    @Override
    public String createGroupOrder(String jsonStr) {

        Logger log = Logger.getLogger("createGroupOrder");
        log.info("jsonStr: "+jsonStr);

        HttpClient client = null;
        HttpPost post = null;
        HttpResponse resp = null;
        String content = null;

        String appId = PropertiesUtils.getString("foreign","jz.appId");
        String key = PropertiesUtils.getString("foreign","jz.key");
        String url = PropertiesUtils.getString("foreign","createGroupOrder.url");
        String contentType = PropertiesUtils.getString("foreign","createGroupOrder.contentType");

        if(StringUtils.isNotEmpty(appId)&&StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(url)
                &&StringUtils.isNotEmpty(contentType)){
            try {
                log.info(url+" , "+contentType+" , "+jsonStr);
                //JSON字符串请求数据转换成JSONObject
                JSONObject reqJson = new JSONObject(jsonStr);
                log.info("reqJSON 1: "+reqJson.toString());
                if(reqJson!=null){
                    //金埻提供的appId

                    reqJson.put("appId", appId);
                    //随机字符串
                    String nonceStr = GeneratorUtils.randomUUID();
                    reqJson.put("nonceStr", nonceStr);
                    log.info("nonceStr :"+ nonceStr);
                    String url_save = PropertiesUtils.getString("foreign","url_save");
                    //放入保存会员信息的URL
                    reqJson.put("notifyUrl", url_save);
                    log.info("reqJson Str: "+reqJson.toString());
                    //创建签名
                    log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
                    String sign = GeneratorUtils.generateSign(reqJson, key);
                    log.info("sign :"+ sign);
                    reqJson.put("sign", sign);
                    log.info("提交请求前的reqJson.toString(): "+reqJson.toString());
                    //创建POST请求
                    post = HttpUtils.createHttpPost(url,contentType,reqJson.toString());
                    if(post!=null){
                        client = HttpUtils.createHttpClient();
                        if(client!=null){
                            resp = client.execute(post);
                            HttpEntity entity = resp.getEntity();
                            content = EntityUtils.toString(entity, "UTF-8");
                            log.info("返回结果 content:"+content);
                            if(content!=null&&content.length()>0){
                                JSONObject result = new JSONObject(content);
                                int code = result.getInt(HttpUtils.CODE);
                                if(code== HttpStatus.SC_OK){
                                    JSONObject dataJson = result.getJSONObject(HttpUtils.DATA);
                                    JSONObject groupService = dataJson.getJSONObject("groupService");
                                    //服务唯一标示
                                    String serviceId = groupService.getString("serviceId");
                                    if(serviceId!=null&&serviceId.length()>0){
                                        TesteesThread testeesThread = new TesteesThread(serviceId);
                                        taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
                                        taskExecutor.submit(testeesThread);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }else{
            log.info(new NullArgumentException("jsonStr 传入的参数不能为空!"));
        }
        return content;
    }


    @Override
    public String gainReport(String jsonStr) throws Exception{
        Logger log = Logger.getLogger("gainReport");

        log.info("传过来的jsonStr: "+jsonStr);

        JSONObject result = null;
        HttpResponse resp = null;
        String content = null;
        JSONArray reportArr = null;
        String savePath = null;
        String physicalPath = null;

        String appId = PropertiesUtils.getString("foreign","jz.appId");
        String key = PropertiesUtils.getString("foreign","jz.key");
        String url = PropertiesUtils.getString("foreign","gainReport.url");
        String contentType = PropertiesUtils.getString("foreign","gainReport.contentType");

        if(jsonStr!=null&&jsonStr.length()>0){
            try {
                JSONObject reqJson = new JSONObject(jsonStr);

                String serviceId = reqJson.getString("serviceId");//团单号
                String ymCode = reqJson.getString("barcode"); //条形码

                String combo = null;
                if(reqJson.has("combo")){
                    combo = reqJson.getString("combo"); // TODO
                    reqJson.remove("combo");// TODO
                }
                String batchNo = null;
                if(reqJson.has("batchNo")){
                    batchNo = reqJson.getString("batchNo"); // TODO
                    reqJson.remove("batchNo");// TODO
                }

                reqJson.put("appId", appId);
                String nonceStr = GeneratorUtils.randomUUID();
                reqJson.put("nonceStr", nonceStr);
                log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
                String sign = GeneratorUtils.generateSign(reqJson, key);
                reqJson.put("sign", sign);
                log.info("sign :"+ sign);
                if(StringUtils.containsIgnoreCase(jsonStr, "savePath")){
                    if(reqJson.get("savePath")!=null){
                        //默认文件夹
                        savePath = (String) reqJson.get("savePath");
                        reqJson.remove("savePath");
                    }
                }else{
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
                    Date now = Calendar.getInstance().getTime();
                    String nowDate = sdfDate.format(now);
                    //保存路径
                    if(StringUtils.isNotEmpty(combo)&&StringUtils.isNotEmpty(batchNo)){
                        savePath = PropertiesUtils.getString("foreign","disk.no")+ File.separator
                                +PropertiesUtils.getString("foreign","dir.jyRp")+File.separator+nowDate+File.separator+batchNo+File.separator+combo;
                    }else{
                        savePath = PropertiesUtils.getString("foreign","disk.no")+File.separator
                                +PropertiesUtils.getString("foreign","dir.jyRp")+File.separator+nowDate;
                    }
                }
                log.info("发送请求前  reqJson.toString(): "+reqJson.toString());
                HttpGet get = HttpUtils.createHttpGet(url,contentType,reqJson.toString());
                if(get!=null){
                    HttpClient client = HttpUtils.createHttpClient();
                    if(client!=null){
                        resp = client.execute(get);
                        HttpEntity entity = resp.getEntity();
                        content = EntityUtils.toString(entity, "UTF-8");
                        if(content!=null&&content.length()>0){
                            result = new JSONObject(content);
                            log.info("返回结果 content:"+content);
                            Integer code = result.getInt(HttpUtils.CODE);
                            if(code==HttpStatus.SC_OK){
                                JSONObject dataJson = result.getJSONObject(HttpUtils.DATA);
                                if(dataJson!=null){
                                    reportArr = dataJson.getJSONArray("reports");
                                    if(reportArr!=null&&reportArr.length()>0){

                                        JSONObject report = null;
                                        String showUrl = null;//报告地址
                                        String barcode = null;//条码
                                        String pdfUrl = null; //pdf文档地址
                                        String reportId = null;//报告单ID

                                        String detailSavePath = null;

                                        for (int i = 0; i < reportArr.length(); i++) {
                                            report = reportArr.getJSONObject(i);
                                            reportId = report.getString("reportId");
                                            //处理报告单
                                            JSONObject rpJson = new JSONObject();
                                            rpJson.put("reportId", reportId);
                                            rpJson.put("combo", combo); //TODO
                                            rpJson.put("batchNo", batchNo);//TODO
                                            detailSavePath = this.gainReportDetail(rpJson.toString());
                                            //处理基因报告
                                            showUrl = report.getString("showUrl");
                                            pdfUrl = report.getString("pdfUrl");
                                            log.info("showUrl: "+showUrl);
                                            log.info("pdfUrl: "+pdfUrl);
                                            barcode = report.getString("barcode");
                                            if(StringUtils.isNotEmpty(showUrl)){
                                                String fileName = barcode+".pdf";
                                                taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
                                                Future<Map<String, String>> futrue = taskExecutor.submit(new DownloadThread(pdfUrl, fileName, savePath));
                                                Map<String, String>respMap = futrue.get();
                                                physicalPath = respMap.get(fileName);
                                            }else{
                                                log.info("showUrl为空!");
                                            }
                                        }
                                    }else{
                                        logNoRpt.info("团单号["+serviceId+"]， 条码["+ymCode+"]，没有报告信息");

                                    }
                                }
                            }else{
                                log.info("状态码code: "+code);
                            }
                        }else{
                            log.info("msg: 返回内容为空！");
                        }
                    }else{
                        log.info("无法创建HttpClient对象！");
                    }
                }else{
                    log.info("无法创建GET请求！");
                }
            } catch (Exception e1) {
                log.info(e1.getMessage());
                throw e1;
            }
        }

        return physicalPath;
    }

    @Override
    public String gainReportDetail(String jsonStr) throws Exception{
        Logger log = Logger.getLogger("gainReportDetail");
        log.info("接收到的jsonStr: "+jsonStr);

        JSONObject result = null;
        HttpResponse resp = null;
        String content = null;
        JSONArray rawResults = null;
        String savePath = null;
        String physicalPath = null;
        //报告单ID
        String reportId = null;

        if(StringUtils.isEmpty(jsonStr)){
            log.info("接收到空数据！");
            return savePath;
        }
        try {
            String appId = PropertiesUtils.getString("foreign","jz.appId");
            String key = PropertiesUtils.getString("foreign","jz.key");
            String url = PropertiesUtils.getString("foreign","gainReport.url");
            String contentType = PropertiesUtils.getString("foreign","gainReport.contentType");

            JSONObject reqJson = new JSONObject(jsonStr);

            String combo = null;
            if(reqJson.has("combo")){
                combo = reqJson.getString("combo"); // TODO
                reqJson.remove("combo");// TODO
            }
            String batchNo = null;
            if(reqJson.has("batchNo")){
                batchNo = reqJson.getString("batchNo"); // TODO
                reqJson.remove("batchNo");// TODO
            }
            //提取报告单ID
            reportId = reqJson.getString("reportId");
            reqJson.remove("reportId");
            url = url+"/"+reportId;
            log.info("url: "+url);
            //请求数据
            reqJson.put("appId", appId);
            String nonceStr = GeneratorUtils.randomUUID();
            reqJson.put("nonceStr", nonceStr);
            log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
            String sign = GeneratorUtils.generateSign(reqJson, key);
            reqJson.put("sign", sign);
            log.info("sign :"+ sign);
            if(StringUtils.containsIgnoreCase(jsonStr, "savePath")){
                if(reqJson.get("savePath")!=null){
                    //设置保存文件夹
                    savePath = reqJson.getString("savePath");
                    reqJson.remove("savePath");
                }
            }else{
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
                Date now = Calendar.getInstance().getTime();
                String nowDate = sdfDate.format(now);
                //保存路径
                if(StringUtils.isNotEmpty(batchNo)&&StringUtils.isNotEmpty(combo)){
                    savePath = PropertiesUtils.getString("foreign","disk.no")+File.separator
                            +PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate
                            +File.separator+batchNo+File.separator+combo;
                }else{
                    //保存路径
                    if(StringUtils.isNotEmpty(combo)&&StringUtils.isNotEmpty(batchNo)){
                        savePath = PropertiesUtils.getString("foreign","disk.no")+File.separator
                                +PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate+File.separator+batchNo+File.separator+combo;
                    }else{
                        savePath = PropertiesUtils.getString("foreign","disk.no")+File.separator
                                +PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate;
                    }
                }
            }
            log.info("发送请求前  reqJson.toString(): "+reqJson.toString());
            HttpGet get = HttpUtils.createHttpGet(url,contentType,reqJson.toString());
            if(get!=null){
                HttpClient client = HttpUtils.createHttpClient();
                if(client!=null){
                    resp = client.execute(get);
                    HttpEntity entity = resp.getEntity();
                    content = EntityUtils.toString(entity, "UTF-8");
                    if(content!=null&&content.length()>0){
                        result = new JSONObject(content);
                        log.info("返回结果 content:"+content);
                        Integer code = result.getInt(HttpUtils.CODE);
                        if(code==HttpStatus.SC_OK){
                            JSONObject dataJson = result.getJSONObject(HttpUtils.DATA);
                            JSONObject reportJson = null;
                            if(dataJson!=null){
                                reportJson = dataJson.getJSONObject("report");
                                if(reportJson!=null){
                                    rawResults = reportJson.getJSONArray("rawResults");
                                    String barcode = reportJson.getString("barcode");
                                    if(rawResults!=null&&rawResults.length()>0){
                                        String httpUrl = null;
                                        String fileName = null;
                                        int num = 0;
                                        String suffix = null;
                                        for (int i = 0; i < rawResults.length(); i++) {
                                            httpUrl = rawResults.getString(i);
                                            log.info("httpUrl: "+httpUrl);
                                            //获取后缀
                                            suffix = httpUrl.substring(httpUrl.lastIndexOf("."));
                                            fileName = barcode+"_"+(++num)+suffix;
                                            taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
                                            Future<Map<String, String>> futrue = taskExecutor.submit(new DownloadThread(httpUrl, fileName, savePath));
                                            Map<String, String>respMap = futrue.get();
                                            //报告单保存的路径
                                            physicalPath = respMap.get(fileName);
                                            log.info(fileName+" 的保存路径： ");
                                            log.info(fileName+": "+savePath);
                                        }
                                    }
                                }
                            }
                        }else{
                            log.info("状态码code: "+code);
                        }
                    }else{
                        log.info("msg: 返回内容为空！");
                    }
                }else{
                    log.info("无法创建HttpClient对象！");
                }
            }else{
                log.info("无法创建GET请求！");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            throw e;
        }
        return physicalPath;
    }

    @Override
    public String findTestessAll(String jsonStr) throws Exception {
        Logger log = Logger.getLogger("findTestessAll");
        JSONObject respJson = new JSONObject();
        JSONArray testArr = null;
        String testeesStr = null;
        log.info("传入的信息： "+jsonStr);
        if(StringUtils.isNotEmpty(jsonStr)){
            testeesStr = this.findTestees(jsonStr);
            //获取团单检测人的JSONObject
            JSONObject testeesJson = new JSONObject(jsonStr);
            if(testeesStr!=null&&testeesStr.length()>0){
                int total = 0;
                int pageSize = 200;
                if(testeesJson.has("limit")){
                    pageSize = testeesJson.getInt("limit");
                }
                int pageNum = 0;

                respJson = new JSONObject(testeesStr);
                String code = respJson.getString("code");
                String msg = respJson.getString("msg");
                if("200".equals(code)){
                    String data = respJson.getString("data");
                    JSONObject dataJson = new JSONObject(data);
                    if(dataJson!=null&&dataJson.length()>0){
                        testArr = dataJson.getJSONArray("testees");
                        total = dataJson.getInt("total");

                        //当场次人数超过200需分页获取，页数从1开始
                        if(testArr.length()<total){
                            String contentNext = null;
                            JSONArray testArrNext = null;
                            int times = total%pageSize==0?total/pageSize:total/pageSize+1;
                            for (int i = 1; i < times; i++) {
                                contentNext = null;
                                pageNum = i+1;
                                if(testeesJson.has("page")){
                                    testeesJson.remove("page");
                                }
                                testeesJson.put("page", pageNum);
                                contentNext = this.findTestees(testeesJson.toString());
                                JSONObject respJsonNext = new JSONObject(contentNext);
                                String codeNext = respJson.getString("code");
                                if("200".equals(codeNext)){
                                    String dataNext = respJsonNext.getString("data");
                                    JSONObject dataJsonNext = new JSONObject(dataNext);
                                    if(dataJsonNext!=null&&dataJsonNext.length()>0){
                                        testArrNext = dataJsonNext.getJSONArray("testees");
                                        if(testArrNext!=null){
                                            log.info("第【"+pageNum+"】页返回的json数据："+testArr.toString());
                                            for (int j = 0; j < testArrNext.length(); j++) {
                                                testArr.put(testArrNext.get(j));
                                            }
                                        }
                                    }
                                }else{
                                    log.info("返回错误!");
                                    log.info("返回码["+code+"]");
                                    log.info("错误信息："+msg);
                                }
                            }
                        }
                        //移除旧数据
                        dataJson.remove("testees");
                        dataJson.put("testees", testArr);
                        //
                        respJson.remove("data");
                        respJson.put("data", dataJson);
                    }
                }else{
                    log.info("返回错误!");
                    log.info("返回码["+code+"]");
                    log.info("错误信息："+msg);
                }
            }
        }else{
            log.info("传入的为空数据!");
        }
        return respJson.toString();

    }

    @Override
    public String findTestees(String jsonStr) throws Exception {
        Logger log = Logger.getLogger("findTestees");
        log.info("基因项目传过来的jsonStr: "+jsonStr);

        HttpClient client = null;
        HttpGet get = null;
        HttpResponse resp = null;
        String content = null;

        String appId = PropertiesUtils.getString("foreign","jz.appId");
        String key = PropertiesUtils.getString("foreign","jz.key");
        String url = PropertiesUtils.getString("foreign","findTestees.url");
        String contentType = PropertiesUtils.getString("foreign","findTestees.contentType");

        if(StringUtils.isNotEmpty(appId)&&StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(url)
                &&StringUtils.isNotEmpty(contentType)&&StringUtils.isNotEmpty(jsonStr)){
            JSONObject reqJson;
            try {
                reqJson = new JSONObject(jsonStr);
                //金埻提供的appId
                if(!reqJson.has("appId")){
                    reqJson.put("appId", appId);
                }
                //随机字符串
                String nonceStr = GeneratorUtils.randomUUID();
                if(!reqJson.has("nonceStr")){
                    reqJson.put("nonceStr", nonceStr);
                }
                //分页
                if(!jsonStr.contains("page")){
                    reqJson.put("page", 0);
                }
                if(!jsonStr.contains("limit")){
                    reqJson.put("limit", 200);
                }
                //团单ID
                String serviceId = reqJson.getString("serviceId");
                //重组URL
                url = url+serviceId+"/testees";
                reqJson.remove("serviceId");
                if(reqJson!=null&&reqJson.length()>0){
                    log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
                    //创建签名
                    if(reqJson.has("sign")){
                        reqJson.remove("sign");
                    }
                    String sign = GeneratorUtils.generateSign(reqJson, key);
                    reqJson.put("sign", sign);
                    log.info("sign :"+ sign);
                    //创建get
                    reqJson.put(HttpUtils.URL, url);
                    reqJson.put(HttpUtils.CONTENT_TYPE, contentType);
                    log.info("发送请求前  reqJson.toString(): "+reqJson.toString());
                    get = HttpUtils.createHttpGet(reqJson);
                    if(get!=null){
                        client = HttpUtils.createHttpClient();
                        if(client!=null){
                            resp = client.execute(get);
                            HttpEntity entity = resp.getEntity();
                            content = EntityUtils.toString(entity, "UTF-8");
                            log.info("返回结果 content:"+content);
                        }
                    }
                }
            }catch (JSONException e1) {
                log.info(e1.getMessage());
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        return content;
    }

    @Override
    public String gainReportInfo(String jsonStr) throws Exception {
        Logger log = Logger.getLogger("gainReportInfo");
        //1. 传入的参数
        // 2） name:
        // 3） code:
        // 4） batchNo:
        // 5） eventsNo:
        // 6） serviceId:对应场次中的团单号 groupOrderNo

        //返回的信息
        String respStr = null;

        if(StringUtils.isEmpty(jsonStr)){
            log.info("接收到空数据！");
            return respStr;
        }
        log.info("接收到的jsonStr: "+jsonStr);
        try {
            //接收到的数据
            JSONObject rcvJson = new JSONObject(jsonStr);

            if(rcvJson!=null&&rcvJson.length()>0){
                //配置数据
                String appId = PropertiesUtils.getString("foreign","jz.appId");
                String key = PropertiesUtils.getString("foreign","jz.key");
                String url = PropertiesUtils.getString("foreign","gainReport.url");
                String contentType = PropertiesUtils.getString("foreign","gainReport.contentType");
                String method = PropertiesUtils.getString("foreign","gainReport.method");
                //请求参数
                JSONObject reqJson = new JSONObject();
                reqJson.put("appId", appId);
                String nonceStr = GeneratorUtils.randomUUID();
                reqJson.put("nonceStr", nonceStr);

                if(!reqJson.has("page")){
                    reqJson.put("page", 1);
                }
                if(!reqJson.has("limit")){
                    reqJson.put("limit", 200); //默认每页200
                }

                log.info("基本数据： "+reqJson.toString());
                //
                String serviceId;
                //场次相关
                ErpEventsService eventsService;
                Map<String,String> props;
                List<ErpEvents> eventsList = null;
                ErpEvents events;
                String eventsNo;
                //会员相关
                GeneCustomerService customerService;
                Map<String,String> params;
                List<ErpCustomer> list;
                ErpCustomer customer;

                if(rcvJson.has(ErpCustomer.F_NAME)){
                    //根据姓名查询会员信息
                    String name = rcvJson.getString(ErpCustomer.F_NAME);
                    customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
                    params = new HashMap<String, String>();
                    params.put(ErpCustomer.F_NAME, name);
                    list = customerService.listCustomerByProps(params);
                    if(list!=null&&list.size()>0){
                        customer = list.get(0);
                        String barcode = customer.getCode();
                        //请求参数添加条码
                        reqJson.put("barcode", barcode);

                        eventsNo = customer.getEventsNo();
                        //根据场次号查找场次信息
                        props = new HashMap<String,String>();
                        props.put(ErpEvents.F_EVENTSNO, eventsNo);
                        eventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
                        eventsList = eventsService.listEventsByProps(props);
                    }else{
                        log.info("查无此人，姓名["+name+"]");
                    }
                }
                //只传条码
                else if(rcvJson.has(ErpCustomer.F_CODE)){
                    //根据条码查询
                    String barcode = rcvJson.getString(ErpCustomer.F_CODE);
                    if(StringUtils.isNotEmpty(barcode)){
                        //请求参数添加条码
                        reqJson.put("barcode", barcode);

                        customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
                        params = new HashMap<String, String>();
                        params.put(ErpCustomer.F_CODE, barcode);
                        list = customerService.listCustomerByProps(params);
                        if(list!=null&&list.size()>0){
                            customer = list.get(0);
                            eventsNo = customer.getEventsNo();
                            //根据场次号查找场次信息
                            props = new HashMap<String,String>();
                            props.put(ErpEvents.F_EVENTSNO, eventsNo);
                            eventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
                            eventsList = eventsService.listEventsByProps(props);
                        }else{
                            log.info("查无此人，条码["+barcode+"]");
                        }
                    }
                }
                else if(rcvJson.has(ErpEvents.F_BATCHNO)){
                    //根据批次号获取团单号
                    String batchNo = rcvJson.getString(ErpEvents.F_BATCHNO);
                    if(StringUtils.isNotEmpty(batchNo)){
                        params = new HashMap<String, String>();
                        params.put(ErpEvents.F_BATCHNO, batchNo);
                        eventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
                        eventsList = eventsService.listEventsByProps(params);
                    }
                }
                else if(rcvJson.has(ErpEvents.F_EVENTSNO)){
                    eventsNo = rcvJson.getString(ErpEvents.F_EVENTSNO);
                    if(StringUtils.isNotEmpty(eventsNo)){
                        params = new HashMap<String, String>();
                        params.put(ErpEvents.F_EVENTSNO, eventsNo);
                        eventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
                        eventsList = eventsService.listEventsByProps(params);
                    }
                }

                // modify 2016-12-26 start
                if(!CollectionUtils.isEmpty(eventsList)){
                    events = eventsList.get(0);
                    serviceId = events.getGroupOrderNo();
                    if(StringUtils.isNotEmpty(serviceId)){
                        //请求参数添加团单号
                        reqJson.put("serviceId", serviceId);
                    }
                }
                // modify 2016-12-26 end

                else if(rcvJson.has("serviceId")||rcvJson.has(ErpEvents.F_GROUPORDERNO)){
                    serviceId = StringUtils.isNotEmpty(rcvJson.getString("serviceId"))? rcvJson.getString("serviceId")
                            :(StringUtils.isNotEmpty(rcvJson.getString(ErpEvents.F_GROUPORDERNO))?rcvJson.getString(ErpEvents.F_GROUPORDERNO):"");
                    if(StringUtils.isNotEmpty(serviceId)){
                        //请求参数添加团单号
                        reqJson.put("serviceId", serviceId);
                    }
                }
                //生成签名
                log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
                String sign = GeneratorUtils.generateSign(reqJson, key);
                reqJson.put("sign", sign);
                log.info("sign :"+ sign);

                if(StringUtils.isNotEmpty(url)&&reqJson!=null){
                    log.info("向金域发起请求的url：	"+url);
                    log.info("向金域发起请求的数据：	"+reqJson.toString());
                    //有条码或者电话时，可以确定唯是获取唯一一个人的报告
                    if(reqJson.has("barcode")||reqJson.has("phone")){
                        respStr = HttpUtils.sendRequest(url, method, contentType, reqJson.toString());
                    } else{
                        //当只有团单号，可能会获取多个人的报告
                        //1.获取该团单的所有检测人
                        String testessStr = this.findTestessAll(reqJson.toString());
                        log.info("返回的团单检测人信息 ：	"+testessStr);
                        //团单总人数
                        int total = 0;
                        if(StringUtils.isNotEmpty(testessStr)){
                            JSONObject testessJson = new JSONObject(testessStr);
                            int restessCode = testessJson.getInt("code");
                            if(200==restessCode){
                                //团单总人数
                                total = testessJson.getJSONObject("data").getInt("total");
                            }else{
                                log.info("获取团单检测人信息失败代码： "+restessCode);
                                log.info("获取团单检测人信息失败信息： "+testessJson.getString("msg"));
                            }
                        }
                        //获取报告数据
                        respStr = HttpUtils.sendRequest(url, method, contentType, reqJson.toString());

                        if(StringUtils.isNotEmpty(respStr)){
                            log.info("金域返回的报告数据： "+respStr);
                            JSONObject respJson = new JSONObject(respStr);
                            int respCode = respJson.getInt("code");
                            if(respCode==200){
                                JSONObject dataJson = respJson.getJSONObject("data");
                                if(dataJson!=null){
                                    JSONArray reportsArr = dataJson.getJSONArray("reports");
                                    log.info("dataArr String:　"+reportsArr.toString());
                                    log.info("dataArr size:　"+reportsArr.length());
                                    if(reportsArr.length()<total){
                                        int pageSize = reqJson.getInt("limit")!=0?reqJson.getInt("limit"):200;
                                        int times = total%pageSize==0?total/pageSize:total/pageSize+1;
                                        String respStrNext;
                                        int page = 0;
                                        for (int i = 1; i < times; i++) {
                                            page = i+1;//下一页
                                            if(reqJson.has("page")){
                                                reqJson.remove("page");
                                            }
                                            reqJson.put("page", page);
                                            //该处因修改了page，需要重新生成sign
                                            reqJson.remove("sign");
                                            String signNext = GeneratorUtils.generateSign(reqJson, key);
                                            reqJson.put("sign", signNext);
                                            log.info("向金域发起请求的数据：	"+reqJson.toString());
                                            //获取下一页数据
                                            respStrNext = HttpUtils.sendRequest(url, method, contentType, reqJson.toString());
                                            if(StringUtils.isNotEmpty(respStrNext)){
                                                log.info("金域返回的数据 respStrNext： "+respStrNext);
                                                JSONObject respJsonNext = new JSONObject(respStrNext);
                                                int respCodeNext = respJsonNext.getInt("code");
                                                if(respCodeNext==200){
                                                    JSONObject dataJsonNext = respJson.getJSONObject("data");
                                                    if(dataJsonNext!=null){
                                                        JSONArray reportsArrNext = dataJsonNext.getJSONArray("reports");
                                                        log.info("dataArrNext["+page+"] String:　"+reportsArrNext.toString());
                                                        log.info("dataArrNext["+page+"] size:　"+reportsArrNext.length());
                                                        int size = reportsArrNext.length();
                                                        for (int j = 0; j < size; j++) {
                                                            JSONObject tempJson = reportsArrNext.getJSONObject(j);
                                                            //添加到报告
                                                            reportsArr.put(tempJson);
                                                        }
                                                    }
                                                }else{
                                                    log.info("金域返回的状态码： "+respCodeNext);
                                                    log.info("金域返回的错误信息： "+respJsonNext.getString("msg"));
                                                }
                                            }else{
                                                log.info("向金域发起请求的数据：	"+reqJson.toString());
                                                log.info("第【"+page+"】页没有获取到报告数据");
                                            }
                                        }
                                        //返回的数据中添加新的报告数据
                                        //移出之前的数据
                                        dataJson.remove("reports");
                                        dataJson.put("reports", reportsArr);
                                        //移出之前的数据
                                        respJson.remove("data");
                                        respJson.put("data", dataJson);
                                        respStr = respJson.toString();
                                        log.info("获取【"+page+"】页后的数据，respStr: "+respStr);
                                    }
                                }
                            }else{
                                log.info("金域返回的状态码： "+respCode);
                                log.info("金域返回的错误信息： "+respJson.getString("msg"));
                            }
                        }else{
                            log.info("返回空数据!");
                        }
                    }
                }
            }else{
                log.info("无法转换为JSON对象： "+jsonStr);
            }
        }catch(Exception e){
            log.info(e);
        }
        log.info("返回给调用者的数据： respStr: "+respStr);
        return respStr;
    }

    @Override
    public String gainReportInfoDetail(String jsonStr) throws Exception {
        Logger log = Logger.getLogger("gainReportInfoDetail");
        //1. 传入的参数
        // 1） reportId: 可直接获取原始报告单
        // 2） name:
        // 3） code:
        // 4） batchNo:
        // 5） eventsNo:
        // 6） serviceId:对应场次中的团单号 groupOrderNo
        log.info("接收到的jsonStr: "+jsonStr);
        String respStr = null;
        //报告信息
        String reportStr = null;
        //报告单信息
        String reportDetailStr = null;

        if(StringUtils.isEmpty(jsonStr)){
            log.info("接收到空数据！");
            return respStr;
        }
        try {
            //接收到的数据
            JSONObject rcvJson = new JSONObject(jsonStr);
            if(rcvJson!=null&&rcvJson.length()>0){
                //配置数据
                String appId = PropertiesUtils.getString("foreign","jz.appId");
                String key = PropertiesUtils.getString("foreign","jz.key");
                String url = PropertiesUtils.getString("foreign","gainReport.url");
                String contentType = PropertiesUtils.getString("foreign","gainReport.contentType");
                String method = PropertiesUtils.getString("foreign","gainReport.method");
                //请求参数
                JSONObject reqJson = new JSONObject();
                reqJson.put("appId", appId);
                String nonceStr = GeneratorUtils.randomUUID();
                reqJson.put("nonceStr", nonceStr);
                log.info("生成sign之前的 reqJson Str: "+ reqJson.toString());
                String sign = GeneratorUtils.generateSign(reqJson, key);
                reqJson.put("sign", sign);
                log.info("sign :"+ sign);
                log.info("已添加sign的 reqJson： "+reqJson.toString());
                String reportId = null;

                if(rcvJson.has("reportId")){
                    reportId = rcvJson.getString("reportId");
                    //单条报告
                    if(StringUtils.isNotEmpty(reportId)){
                        //添加reportId
                        url = url +"/" +reportId;
                        if(StringUtils.isNotEmpty(url)&&reqJson!=null){
                            log.info("向金域发起请求的url：	"+url);
                            log.info("向金域发起请求的数据：	"+reqJson.toString());
                            respStr = HttpUtils.sendRequest(url, method, contentType, reqJson.toString());
                            if(StringUtils.isNotEmpty(respStr)){
                                log.info("请求成功，金域返回的数据： "+respStr);
                            }else{
                                log.info("请求失败");
                            }
                        }
                    }else{
                        log.info("无法获取reportId，不向金域接口发起请求");
                    }
                }
            }else{
                log.info("无法转换为JSON对象： "+jsonStr);
            }
        }catch(Exception e){
            log.info(e);
        }
        log.info("方法[gainReportInfoDetail]返回的数据： "+respStr);
        return respStr;
    }

    /**
     * 取消服务
     * @param jsonStr
     * @return
     */
    @Override
    public String cancelService(String jsonStr) {
        Logger log = Logger.getLogger("cancelOrder");
        log.info("jsonStr: "+jsonStr);
        String content = null;

        if(StringUtils.isNotEmpty(jsonStr)) {
            String appId = PropertiesUtils.getString("foreign","jz.appId");
            String key = PropertiesUtils.getString("foreign","jz.key");
            String url = PropertiesUtils.getString("foreign","cancelOrder.url");
            String contentType = PropertiesUtils.getString("foreign","cancelOrder.contentType");
            String method = PropertiesUtils.getString("foreign","cancelOrder.method");

            if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(url)
                    && StringUtils.isNotEmpty(contentType)) {
                try {
                    log.info(url + " , " + contentType + " , " + jsonStr);
                    //JSON字符串请求数据转换成JSONObject
                    JSONObject reqJson = new JSONObject(jsonStr);
                    if (reqJson != null) {
                        log.info("reqJSON 1: " + reqJson.toString());
                        //提取团单号
                        if (reqJson.has("serviceId")) {
                            String serviceId = reqJson.getString("serviceId");
                            if (StringUtils.isNotEmpty(serviceId)) {
                                url = url + "/" + serviceId + "/cancel";
                                //重构JSON
                                reqJson = new JSONObject();
                                //金埻提供的appI
                                reqJson.put("appId", appId);
                                //随机字符串
                                String nonceStr = GeneratorUtils.randomUUID();
                                reqJson.put("nonceStr", nonceStr);
                                log.info("nonceStr :" + nonceStr);
                                log.info("reqJson Str: " + reqJson.toString());
                                //创建签名
                                log.info("生成sign之前的 reqJson Str: " + reqJson.toString());
                                String sign = GeneratorUtils.generateSign(reqJson, key);
                                log.info("sign :" + sign);
                                reqJson.put("sign", sign);
                                log.info("提交请求前的reqJson.toString(): " + reqJson.toString());
                                //获取报告数据
                                content = HttpUtils.sendRequest(url, method, contentType, reqJson.toString());
                            }else {
                                log.info("团单号为空！");
                            }
                        }else {
                            log.info("没有团单号信息："+reqJson.toString());
                        }
                    }else {
                        log.info("无法转换成JSON对象!");
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            } else {
                log.info(new NullArgumentException("无法获取全部配置参数！请检查foreign.properties文件"));
            }
        }else {
            log.info(new NullArgumentException("jsonStr 传入的参数不能为空!"));
        }
        return content;
    }

    /**
     * 删除团单
     * @param jsonStr JSON字符串
     * @return
     */
    @Override
    public String delOrder(String jsonStr) {
        return null;
    }

    @Override
    public String loadEvent(String str) {
        ErpEventsService eventsService = (ErpEventsService) SpringTool.getBean(ErpEventsService.class);
//        String eventsNo = "HK20170216";
        String eventsNo = str;
        try {
            List<ErpEvents> eventsList = eventsService.listEventsByInfo(eventsNo, ErpEvents.F_EVENTSNO);
            if (!CollectionUtils.isEmpty(eventsList)){
                for (ErpEvents obj: eventsList ) {
                    System.out.println(obj.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
