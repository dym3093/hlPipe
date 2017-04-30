package org.hpin.webservice.websExt.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.bean.pa.Report;
import org.hpin.webservice.service.pa.ErpPACustomerService;
import org.hpin.webservice.service.pa.FileStreamUploadUtil;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.websExt.GeneServicePA;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.pajk.openapi.codec.client.RequestEncoder;
import com.pajk.openapi.codec.client.RequestEntity;
import com.pajk.openapi.codec.client.ResponseDecoder;


/**
 * 平安健康基因检测业务实现类
 * @author machuan
 * @date 2017年2月9日
 */
@Service(value = "org.hpin.webservice.service.GeneServiceImplPA")
@WebService
@SuppressWarnings("unchecked")
public class GeneServiceImplPA implements GeneServicePA{
	//测试环境 域名
//	public static final String BASEURL = "http://openapi.test.pajk.cn/api/v1/";
	
	//正式环境 接口地址
	public static final String BASEURL = "http://openapi.jk.cn/api/v1/";
	
	
	
	Logger log = Logger.getLogger("geneServicePA");
	
	@Override
	public String getOrder(String orderId, String boxSn) {
		log.info("getOrder--基因系统传过来的参数---orderId："+orderId+",boxSn:"+boxSn);
		String results = "";
		try{
			List<Object> params = new ArrayList<Object>();
			//20170228 orderId 为0L
			Long orderIdL = 0L;
			params.add(orderIdL);
			params.add(boxSn);
			results = doPostForGetData("GetOrder",params);
			log.info("getOrder---平安健康的返回报文："+results);
			//解析返回值
			Map<String,Object> obj = JSONObject.parseObject(results, Map.class);
			//如果请求业务成功，即进行后续操作
			if((Boolean) obj.get("success")){
				Map<String, Object> t = (Map<String, Object>) obj.get("t");
				ErpPACustomerService service = (ErpPACustomerService) SpringTool.getBean(ErpPACustomerService.class);
				//保存客户信息进入erp_pre_customer 一个履约单号只有一条数据
				orderId = String.valueOf(t.get("orderId"));
				ErpPreCustomer preCustomer = service.getPreCustomerByOrderId(orderId);
				boolean isUpdated = true;
				if(preCustomer==null){
					preCustomer = new ErpPreCustomer();
					isUpdated = false;
				}
				//场次号  生成一个虚拟场次号 PA开头加当前日期
				preCustomer.setEventsNo(createEventsNo());
				//履约单号
				preCustomer.setPerformNo(String.valueOf(t.get("orderId")));
				//条形码
				preCustomer.setCode(String.valueOf(t.get("boxSn")));
				//姓名--受检人姓名
				preCustomer.setWereName(String.valueOf(t.get("examineeName")));
				//检测公司套餐
				preCustomer.setCheckCobmo(String.valueOf(t.get("serviceName")));
				//远盟套餐 根据检测公司套餐查询
				String ymCombo = service.findYmComboByCheckCombo(String.valueOf(t.get("serviceName")));
				preCustomer.setYmCombo(ymCombo);
				//性别
				preCustomer.setWereSex("M".equals(String.valueOf(t.get("examineeGender")))?"男":"女");
				//年龄
				preCustomer.setWereAge(String.valueOf(t.get("examineeAge")));
				//订单生成日期--申请日期
				Date orderCreateDate = new Date();
				String gmtCreate = String.valueOf(t.get("gmtCreate"));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					orderCreateDate = df.parse(gmtCreate);
				} catch (ParseException e) {
					//解析失败不做处理  即选择当前时间
				}
				preCustomer.setOrderCreateDate(orderCreateDate);
				//履约单状态
				preCustomer.setPerformStatus(String.valueOf(t.get("status")));
				//套餐详细检测项 detailedTestItem
				preCustomer.setDetailedTestItem(StringUtils.join(((List<Object>) t.get("examineOptions")).toArray(), ","));
				//客户状态 --样本已获取
				preCustomer.setStatusYm(PropertiesUtils.getInt("status", "statusYm.yhq"));
				//支公司ID  companyId  ff8080815a211c39015a21a048fa0c27
				preCustomer.setCompanyId("ff8080815a211c39015a21a048fa0c27");
				//项目ID shipPorId   ff8080815a211c39015a21a048fa0c28
				preCustomer.setShipPorId("ff8080815a211c39015a21a048fa0c28");
				preCustomer.setIsDeleted(0);
				if(isUpdated){
					service.update(preCustomer);
					log.info("getOrder---修改成功："+boxSn);
				}else{
					service.save(preCustomer);
					log.info("getOrder---保存成功："+boxSn);
				}
			}
		}catch(Exception e){
			log.error("getOrder--保存客户信息失败：",e);
			results = "{\"success\":false,\"msg\":\"保存客户信息失败，请检查！\"}";
		}
		return results;
	}

	/**
	 * @return
	 * @author machuan
	 * @date  2017年2月16日
	 */
	private String createEventsNo() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return "PA"+df.format(new Date());
	}

	@Override
	public String boxReceived(String orderId) {
		log.info("boxReceived--基因系统传过来的参数---orderId："+orderId);
		String results = "";
		try{
			List<Object> params = new ArrayList<Object>();
			params.add(orderId);
			results = doPostForGetData("BoxReceived",params);
			log.info("boxReceived---平安健康的返回报文："+results);
			//解析返回值
			Map<String,Object> obj = JSONObject.parseObject(results, Map.class);
			//如果请求业务成功，即进行后续操作
			if((Boolean) obj.get("success")){
				Map<String, Object> t = (Map<String, Object>) obj.get("t");
				ErpPACustomerService service = (ErpPACustomerService) SpringTool.getBean(ErpPACustomerService.class);
				//修改客户信息进入erp_pre_customer
				ErpPreCustomer preCustomer = service.getPreCustomerByOrderId(orderId);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				preCustomer.setConfirmCaryTime(df.parse((String) t.get("gmtReceive")));
				//履约单状态设置为确认受检
				preCustomer.setPerformStatus("4");
				service.update(preCustomer);
			}
		}catch(Exception e){
			log.error("boxReceived--保存确认受检失败：",e);
			results = "{\"success\":false,\"msg\":\"保存确认受检失败，请检查！\"}";
		}
		return results;
	}

	@Override
	public String detectFailed(String orderId, String reason) {
		log.info("detectFailed--基因系统传过来的参数---orderId："+orderId+",reason:"+reason);
		String results = "";
		try{
			List<Object> params = new ArrayList<Object>();
			params.add(orderId);
			params.add(reason);
			results = doPostForGetData("DetectFailed",params);
			//解析返回值
			Map<String,Object> obj = JSONObject.parseObject(results, Map.class);
			//如果请求业务成功，即进行后续操作
			if((Boolean) obj.get("success")){
				ErpPACustomerService service = (ErpPACustomerService) SpringTool.getBean(ErpPACustomerService.class);
				//修改客户信息进入erp_pre_customer
				ErpPreCustomer preCustomer = service.getPreCustomerByOrderId(orderId);
				//检测失败原因
				preCustomer.setCheckFailCaus(reason);
				//履约单状态设置为检测失败
				preCustomer.setPerformStatus("5");
				//设置isdeleted为1
				preCustomer.setIsDeleted(1);
				preCustomer.setUpdateTime(new Date());
				service.update(preCustomer);
			}
		}catch(Exception e){
			log.error("detectFailed--保存检测失败异常：",e);
			results = "{\"success\":false,\"msg\":\"保存检测失败异常，请检查！\"}";
		}
		return results;
	}
	
	/**
     * 上传报告
     * @param orderId 履约单号
     * @param examineOption 检测项名称
     * @param pathFile 报告地址
     * @return
     * @author machuan
     * @date  2017年2月17日
     */
	@Override
	public String uploadReports(String orderId,String examineOption, String pathFile) {
		log.info("uploadReports--基因系统传过来的参数---orderId："+orderId+"，examineOption："+examineOption+",pathFile:"+pathFile);
		String results = "";
		//调用上传文件接口之前  先上传文件  获取tfsFileName
		try{
			String tfsFileName = FileStreamUploadUtil.uploadFileStream(pathFile);
			log.info("uploadReports---平安健康的返回报文："+tfsFileName);
			//组装参数
			List<Object> params = new ArrayList<Object>();
			params.add(orderId);
			List<Report> reports = new ArrayList<Report>();
			Report report = new Report();
			report.setExamineOption(examineOption);
			report.setTfsFileName(tfsFileName);
			reports.add(report);
			params.add(reports);
			results = doPostForGetData("UploadReports",params);
			//解析返回值
			Map<String,Object> obj = JSONObject.parseObject(results, Map.class);
			//如果请求业务成功，即进行后续操作
			if((Boolean) obj.get("success")){
				ErpPACustomerService service = (ErpPACustomerService) SpringTool.getBean(ErpPACustomerService.class);
				//修改客户信息进入erp_pre_customer
				ErpPreCustomer preCustomer = service.getPreCustomerByOrderId(orderId);
				//上传报告时间
				preCustomer.setUploadReportTime(new Date());
				//履约单状态设置为已完成
				preCustomer.setPerformStatus("6");
				//平安保存的报告名称
				preCustomer.setSystemReportName(tfsFileName);
				service.update(preCustomer);
			}
		}catch(Exception e){
			log.error("uploadReports--上传报告失败：",e);
			results = "{\"success\":false,\"msg\":\"上传报告失败，请检查！\"}";
		}
		return results;
	}
	
	//发起http请求代码
	private String do_urlencoded_post(String url_address, String request_body) throws Exception{
			String body ="";
            // Configure and open a connection to the site you will send the request  
            // java.net.URL  java.net.URLConnection
            URL url = new URL(url_address);  
            URLConnection urlConnection = url.openConnection();  
            // 设置doOutput属性为true表示将使用此urlConnection写入数据  
            urlConnection.setDoOutput(true);  
            //定义待写入数据的内容类型，我们设置为application/x-www-form-urlencoded类型  
            urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");  
            // 得到请求的输出流对象  
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());  
            // 把数据写入请求的Body  
            out.write(request_body);  
            out.flush();  
            out.close();  
            // 从服务器读取响应  
            InputStream inputStream = urlConnection.getInputStream(); 
            body = ConvertStream2Json(inputStream);
            System.out.println(body);  
			return body;
		}
		
	private String ConvertStream2Json(InputStream inputStream) throws Exception{
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1){
            out.write(buffer, 0, len);
        }
        // 将内存流转换为字符串
        jsonStr = new String(out.toByteArray());
        return jsonStr;
	}
	
	private String doPostForGetData(String methodName,List<Object> params) throws Exception{
		String results="";
		// 数据准备，服务端提供的配置参数
		String key = PropertiesUtils.getString("pingan","pingan.key");
		String partnerId = PropertiesUtils.getString("pingan","pingan.partnerid");
		String apiId = PropertiesUtils.getString("pingan","pingan.apiId"+methodName); //即上文apiId
		String apiGroup = PropertiesUtils.getString("pingan","pingan.apiGroupName"+methodName); //即上文 apiGroup
		String apiName = PropertiesUtils.getString("pingan","pingan.apiName"+methodName); //即上文apiName 
		RequestEncoder encoder = new RequestEncoder(partnerId, key, apiId);
		//参数类型支持基本类型，复杂对象
		//按接口定义的参数顺序放入参数
		for(Object object : params){
			encoder.addParameter(object);
		}
		//进行加密
		RequestEntity e = encoder.encode();
		//拼装url
		String url = BASEURL + apiGroup +"/"+ apiName +"?";
		String postURL = url + e.getQueryParams();
		String postData = e.getFormParams();
		//post "application/x-www-form-urlencoded" http请求 发起请求代码示例见下方
		String text = do_urlencoded_post(postURL, postData);
		log.info(methodName+"----平安健康返回的报文："+text);
		// 解析返回值(此处使用fastjson 1.2.23解析json字符串，也可使用其他json解析类库)
		Map<String,Object> obj = JSONObject.parseObject(text, Map.class);
		String objectStr = obj.get("object").toString();
		Integer code =  (Integer)obj.get("code");
		//调用成功
		if(code==0){
		   //解码返回结果
			ResponseDecoder decoder =new ResponseDecoder(key);
			decoder.decode(objectStr);
			results =  decoder.getData();
			log.info(methodName+"----results："+results);
		}else{
			results = "{\"success\":false,\"msg\":\"服务请求失败，请检查！\"}";
		}
		return results;
	}
}
