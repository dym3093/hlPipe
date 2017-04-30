package org.hpin.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.common.util.XmlUtils;
import org.hpin.webservice.bean.*;
import org.hpin.webservice.bean.yg.ErpBxCompanyPreSet;
import org.hpin.webservice.crmwebsclient.PmForwardInfoImpl;
import org.hpin.webservice.crmwebsclient.PmForwardInfoImplServiceLocator;
import org.hpin.webservice.foreign.DownloadThread;
import org.hpin.webservice.service.ty.ErpTYCustomerService;
import org.hpin.webservice.service.yg.ErpBxCompanyPreSetService;
import org.hpin.webservice.util.HttpUtils;
import org.hpin.webservice.util.JaxbXmlBeanUtils;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.Tools;
import org.hpin.webservice.websclientTPWC.WeiciMsgService;
import org.hpin.webservice.websclientTPWC.WeiciMsgServiceServiceLocator;
import org.hpin.webservice.websclientXN.TransferDataWebService;
import org.hpin.webservice.websclientXN.TransferDataWebServiceService;
import org.hpin.webservice.websclientXN.TransferDataWebServiceServiceLocator;
import org.hpin.webservice.wptwebsclient.GeneEventService;
import org.hpin.webservice.wptwebsclient.GeneEventServiceServiceLocator;
import org.hpin.webservice.wptwebsclient.GeneService;
import org.hpin.webservice.wptwebsclient.GeneServiceServiceLocator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.jws.WebService;
import javax.xml.rpc.ServiceException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@Service(value = "org.hpin.webservice.service.YmGeneReportServiceImpl")
@WebService
public class YmGeneReportServiceImpl implements YmGeneReportService {

	private final Logger log = Logger.getLogger(YmGeneReportServiceImpl.class); 
	public static String LOCK = "lock"; //create henry.xu 20161213 锁

	private GeneReportService geneReportService;
	private GeneOrderService geneOrderService;
	private GeneGroupOrderService geneGroupOrderService;
	private GeneCustomerService geneCustomerService;

	private GeneServiceImplJY jyService ;

	//报告权限Service
	private ErpReportAuthorityService reportAuthorityService;

	private GeneServiceImplHK hkService;
	
	private GeneServiceImplTY tyService;

	private ErpReportdetailImgtaskService imgTaskService;

	//微磁
	private static final WeiciMsgServiceServiceLocator weiciLocator = new WeiciMsgServiceServiceLocator();
	//星宁基因
	private static final TransferDataWebServiceServiceLocator xnLocator = new TransferDataWebServiceServiceLocator();

	private WeiciMsgService weiciMsgService;

	/**
	 * 保险公司营销员验证
	 * create by henry.xu 2017年2月8日
	 * @param xml
	 * @return
	 */
	public String verifySalesMan(String xml) {
		geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		return geneCustomerService.findSalesManInfoByParams(xml);
	}

	/**
	 * 获取CRM中相关项目负责人以及项目名称信息，解析后返填到当前申请界面中
	 * create by henry.xu 20160905
	 * @param projectNum 项目编码
	 * @return
	 */
	public String getCrmBaseInfoByProCode(String projectNum) {
		String url="http://192.168.1.16:8010/websHbs/hbs/hbsPmInfo!getGeneProjectInfo.action";
		String resContent="";
		HttpClient client = null;
		try {
			client = HttpUtils.createHttpClient(); //获取HttpClient;  new DefaultHttpClient()
			HttpPost post = new HttpPost(url); //创建请求;
		
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("projectNum", projectNum));		
		
			HttpEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(formEntity);

			HttpResponse response = client.execute(post);
			log.info("status:"+response.getStatusLine().getStatusCode());
			
			//当访问成功后,返回200状态码;
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
			    resContent = EntityUtils.toString(entity, "UTF-8");  
			}
			
		} catch (Exception e1) {
			log.error("获取CRM异常YmGeneReportServiceImpl.getCrmBaseInfoByProCode", e1);
		} 
		return resContent;
	}

	public String getGeneReportInfo (String idcard,String name, String tel) {
		Logger log = Logger.getLogger("getGeneReportInfo");
		log.info("idcard:"+idcard +",name:"+name +",tel:"+tel);
		boolean flag = false;
		Response response=null;
		String backXml="";
		// modify by YoumingDeng 2016-12-06 start
		// 根据客户信息获取查看报告的权限
		Map<String,String> cParams = new HashMap<String, String>();
		if(StringUtils.isNotEmpty(idcard)){
			cParams.put(ErpCustomer.F_IDNO, idcard);
		}
		if(StringUtils.isNotEmpty(name)){
			cParams.put(ErpCustomer.F_NAME, name);
		}
		if(StringUtils.isNotEmpty(tel)){
			cParams.put(ErpCustomer.F_PHONE, tel);
		}
		cParams.put(ErpCustomer.F_ISDELETED, "0");
		try {
			if(!CollectionUtils.isEmpty(cParams)){
				reportAuthorityService = (ErpReportAuthorityService) SpringTool.getBean(ErpReportAuthorityService.class);
				//获取查看报告权限
				flag = reportAuthorityService.findAuthorityByProps(cParams);
			}
		} catch (Exception e) {
			log.info(e);
		}
		// modify by YoumingDeng 2016-12-06 end
		geneReportService = (GeneReportService) SpringTool.getBean(GeneReportService.class);
		//先决条件
		if(flag) {
			//判定是否为空
			if(!geneReportService.checkParam(idcard, name, tel)) {
				response = new Response();
				response.setCode("");
				response.setIdentityStatus("4");//必录项为空
				response.setPdfPath("");
				response.setReportStatus("0");
			}else {
				//符合要求，则查询
				response = geneReportService.getCheckResponse(idcard, name, tel);
				backXml = JaxbXmlBeanUtils.bean2Xml(response);
			}
		}else{
			//没有查询权限，直接返回空
			backXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response>" +
					"<code></code><identityStatus>0</identityStatus><pdfPath></pdfPath><reportStatus>0</reportStatus>" +
					"</response>";
		}
		//日志处理
		LogInfo loginfo = geneReportService.getLogInfo(idcard, name, tel, response);
		geneReportService.save(loginfo);
		log.info("backXml:" + backXml);
	    return backXml;
		
	}
	public String pushGeneReportInfo (String xml){
		Logger log = Logger.getLogger("pushGeneReportInfo");
		log.info("xml:"+xml);
		ResponseGene response=new ResponseGene();
		String retcode="1";
		String backXml="";		
		
		 geneReportService = (GeneReportService)SpringTool.getBean(GeneReportService.class);
		 Request request=(Request)JaxbXmlBeanUtils.xml2Bean(xml, new Request().getClass());
		 request.setCreateTime(new Date());
		 try{
		   geneReportService.save(request);
		 }catch(Exception e){
			 log.error("Exception: "+e.getMessage());
			 retcode="0";
			 //e.printStackTrace();
			 //INDEX_GENGREPORT_CODE
			 if(e instanceof  org.springframework.dao.DataIntegrityViolationException){
					String errorMessage = e.getCause().getCause().toString();
					if(errorMessage.contains("INDEX_GENGREPORT_CODE")){
						retcode="2";
					}
			 }			 			
		 }
		 response.setCode(request.getCode());
		 response.setName(request.getName());
		 response.setPhone(request.getPhone());
		 response.setRetcode(retcode);
		 response.setRetmsg(geneReportService.getRetmsg(retcode));
		
		 backXml=JaxbXmlBeanUtils.bean2Xml(response);
		log.info("backXml:"+backXml);
		return backXml;
	}
	
	/**
	 * 基因套餐信息
	 * @param xml
	 * @return String 
	 * @author DengYouming
	 * @since 2016-6-6 下午6:56:24
	 */
	public String pushGeneComboInfo (String xml){
		Logger log = Logger.getLogger("pushGeneComboInfo");
		log.info("xml:"+xml);		
		String retcode="1";		
		try {
			GeneService geneService=GeneServiceServiceLocator.getInstance().getGeneServicePort();
         	retcode=geneService.pushGeneComboInfo(xml);
		}catch (ServiceException e) {
			log.error("ServiceException: "+e.getMessage());
			e.printStackTrace();
			log.info("retcode:0");
			return "0";
		}catch (RemoteException e) {
			log.error("RemoteException: "+e.getMessage());
			e.printStackTrace();
			log.info("retcode:0");
			return "0";
		}		
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	/**
	 * 订单信息
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-6 下午7:33:23
	 */
	public String pushGeneOrderInfo (String xml){
		Logger log = Logger.getLogger("pushGeneOrderInfo");
		log.info(xml);
		String retcode="0";		
		try{
			OrderInfo order=(OrderInfo)JaxbXmlBeanUtils.xml2Bean(xml, new OrderInfo().getClass());
			if(null!=order){
				geneOrderService = (GeneOrderService)SpringTool.getBean(GeneOrderService.class);
				String orderNo = order.getOrderNo();
				log.info("orderNo:"+orderNo);
				if(StringUtils.isNotEmpty(orderNo)){
					if(geneOrderService.isRepeat(orderNo)){
						retcode = "2";
					}else{
						order.setCreatedate(new Date());
						geneOrderService.save(order);
						retcode = "1";
					}
				}
			}
		}catch(Exception e){
			log.error("Exception: "+e.getMessage());
			log.info("retcode:0");
			retcode="0";
		}
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	/**
	 * 更新订单信息
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-6 下午7:35:01
	 */
	public String updateGeneOrderInfo (String xml){
		Logger log = Logger.getLogger("updateGeneOrderInfo");
		log.info("xml:"+xml);		
		String retcode="1";		
		try {
			GeneService geneService=GeneServiceServiceLocator.getInstance().getGeneServicePort();
         	retcode=geneService.updateGeneOrderInfo(xml);
		}catch (ServiceException e) {
			log.error("ServiceException: "+e.getMessage());
			e.printStackTrace();
			log.info("retcode:0");
			return "0";
		}catch (RemoteException e) {
			log.error("RemoteException: "+e.getMessage());
			e.printStackTrace();
			log.info("retcode:0");
			return "0";
		}		
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	/**
	 * 团购订单处理
	 * @param xml
	 * @return String 
	 * @author DengYouming
	 * @since 2016-6-22 下午3:06:27
	 */
	public String pushGeneGroupOrderInfo(String xml){
		Logger log = Logger.getLogger("pushGroupOrderInfo");
		log.info("xml:"+xml);		
		String retcode = "0";
		
		try{
			GroupOrderInfo obj = (GroupOrderInfo)JaxbXmlBeanUtils.xml2Bean(xml, new GroupOrderInfo().getClass());
			if(null!=obj){
				geneGroupOrderService = (GeneGroupOrderService)SpringTool.getBean(GeneGroupOrderService.class);
				String orderNo = obj.getOrderNo();
				log.info("orderNo:"+orderNo);
				log.info("orderNum:"+obj.getComboList().size());
				if(StringUtils.isNotEmpty(orderNo)){
					if(geneGroupOrderService.saveGroupOrderInfo(obj)){
						retcode = "1";
					}
				}
			}
		}catch(Exception e){
			log.error("Exception: "+e.getMessage());
			log.info("retcode:0");
			retcode="0";
		}
		log.info("retcode:"+retcode);
		return retcode;
	}

	/**
	 * 根据电话号码验证是否为会员
	 * @param phone
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-23 下午3:23:23
	 */
	@Override
	public String verifyMemberPhone(String phone) {
		Logger log = Logger.getLogger("verifyMemberPhone");
		log.info("phone: "+phone);		
		String retcode = "0";
		try{
			if(null!=phone&&phone.trim().length()>0){
				geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
				Map<String, Object> props = new HashMap<String, Object>();
				props.put(ErpCustomer.F_PHONE, phone);
				List<ErpCustomer> list = geneCustomerService.findByProps(props);
				if(list!=null&&list.size()>0){
					if(phone.equals(list.get(0).getPhone())){
						retcode = "1";
					}
				}
			}
		}catch(Exception e){
			log.error("Exception: "+e.getMessage());
			log.info("retcode:0");
			retcode="0";
		}
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	@Override
	public String verifyMemberInfo(String xml) {
		Logger log = Logger.getLogger("verifyMemberPhone");
		log.info("xml"+xml);		
		String retcode = "0";
		try{
			geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
			Map<String,String> result = geneCustomerService.fetchXmlValue(xml, "memberInfo", new String[]{"code","name","idno"});
			
			if(!CollectionUtils.isEmpty(result)){
				Map<String, Object> props = new HashMap<String, Object>();
				props.put(ErpCustomer.F_CODE, result.get(ErpCustomer.F_CODE));
				props.put(ErpCustomer.F_NAME, result.get(ErpCustomer.F_NAME));
				props.put(ErpCustomer.F_IDNO, result.get(ErpCustomer.F_IDNO));
				List<ErpCustomer> list = geneCustomerService.findByProps(props);
				if(list!=null&&list.size()>0){
					if(list.get(0).getCode().equals(result.get(ErpCustomer.F_CODE))){
						retcode = "1";
					}
				}
			}
		}catch(Exception e){
			log.error("Exception: "+e.getMessage());
			log.info("retcode:0");
			retcode="0";
		}
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	@Override
	public String pushPdfInfo(String xml) {
		Logger log = Logger.getLogger("pushPdfInfo");
		log.info("xml"+xml);		
		String retcode = "0";
		if(StringUtils.isNotEmpty(xml)){
			try{
				geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
				Map<String,String> result = geneCustomerService.fetchXmlValue(xml, "pdfInfo", new String[]{"phone","code","pdfPath","pdfDate"});
				
				if(!CollectionUtils.isEmpty(result)){
					Map<String, Object> props = new HashMap<String, Object>();
					props.put(ErpCustomer.F_CODE, result.get("code"));
					props.put(ErpCustomer.F_PHONE, result.get("phone"));
					List<ErpCustomer> list = geneCustomerService.findByProps(props);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(list!=null&&list.size()>0){
						ErpCustomer obj = list.get(0);
						obj.setPdffilepath(result.get("pdfPath"));
						String strDate = result.get("pdfDate");
						if(strDate.length()<11){
							strDate += " 00:00:00";
						}
						obj.setUpdateTime(sdf.parse(strDate));
						geneCustomerService.update(obj);
						retcode = "1";
					}
				}
				
			}catch(Exception e){
				log.error("Exception: "+e.getMessage());
				log.info("retcode:0");
				retcode="0";
			}
		}else{
			retcode="0";
			log.info("空数据!");
		}
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	@Override
	public String createGroupOrder(String jsonStr){
	    String content = null;
	    if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.createGroupOrder(jsonStr);
        }
		return content;
	}
	
	@Override
	public String gainReport(String jsonStr) throws Exception{
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.gainReport(jsonStr);
        }
        return content;

	}
	
	@Override
	public String gainReportDetail(String jsonStr) throws Exception {
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.gainReportDetail(jsonStr);
        }
        return content;
	}
    
	@Override
	public String gainReportInfo(String jsonStr) throws Exception {
		Logger log = Logger.getLogger("gainReportInfo");
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.gainReportInfo(jsonStr);
        }
        log.info("content: "+content);
        return content;
	}
	
	@Override
	public String gainReportInfoDetail(String jsonStr) throws Exception {
		Logger log = Logger.getLogger("gainReportInfoDetail");
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.gainReportInfoDetail(jsonStr);
        }
        log.info("content: "+content);
        return content;
	}
	
	/**
	 * 根据团单号查找所有相关团单的会员信息，返回会员信息的JSONArray字符串
	 * @param jsonStr JSON字符串
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-21 上午10:21:39
	 */
	public String findTestessAll(String jsonStr) throws Exception{
		Logger log = Logger.getLogger("findTestessAll");
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
			jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
            content = jyService.findTestessAll(jsonStr);
        }
        log.info("content: "+content);
        return content;
	}
	
	@Override
	public String findTestees(String jsonStr) {
		Logger log = Logger.getLogger("findTestees");
        String content = null;
        if(StringUtils.isNotEmpty(jsonStr)) {
            try {
				jyService = (GeneServiceImplJY) SpringTool.getBean(GeneServiceImplJY.class); // add by Damian 2016-12-26
                content = jyService.findTestees(jsonStr);
            } catch (Exception e) {
                log.info(e);
            }
        }
        return content;
	}
	
	@Override
	public String pushSampleInfo(String xml) {
		Logger log = Logger.getLogger("pushSampleInfo");
		log.info(xml);
		String retcode="0";		
		if(StringUtils.isNotEmpty(xml)){
			try{
				OrderInfo order=(OrderInfo)JaxbXmlBeanUtils.xml2Bean(xml, new OrderInfo().getClass());
				if(null!=order){
					geneOrderService = (GeneOrderService)SpringTool.getBean(GeneOrderService.class);
					String orderNo = order.getOrderNo();
					String status =order.getStatus();
					log.info("orderNo:"+orderNo);
					log.info("status:"+status);
					if(StringUtils.isNotEmpty(orderNo) && StringUtils.isNotEmpty(status)){
						order.setCreatedate(new Date());
						List<OrderInfo> list = geneOrderService.getOrderInfo(orderNo,status);
						geneOrderService.updateStatus(list);
						retcode = "1";
					}
				}else{
					retcode="0";
					log.info("retcode: "+retcode);
					log.info("数据转换失败!");
				}
			}catch(Exception e){
				log.error("Exception: "+e.getMessage());
				log.info("retcode:0");
				retcode="0";
			}
		}else{
			retcode="0";
			log.info("retcode: "+retcode);
			log.info("空数据!");
		}
		log.info("retcode:"+retcode);
		return retcode;
	}
	
	@Override
	public String pushEventQRCodeInfo(String xml) {
		Logger log = Logger.getLogger("pushEventQRCodeInfo");
		log.info("基因系统请求的数据： "+xml);
		String retcode="0";
		log.info("retcode first: "+retcode);
		if(xml!=null&&xml.length()>0){
			try {
			
				GeneEventServiceServiceLocator locator = new GeneEventServiceServiceLocator();
				//地址
				String address = PropertiesUtils.getString("foreign","ub.address");
				locator.setGeneEventServicePortEndpointAddress(address);
				GeneEventService geneEventService = locator.getGeneEventServicePort();
				
				String[] destAttr = {"eventsNo","eventTime","ownedCompany","branchCompany","issueNo","keyword","validHour","batchNo","companyComboList"};
				Map<String,String> ymMap = XmlUtils.fetchXmlValue(xml, "eventQRCodeInfo", destAttr);
					
				SimpleDateFormat sdf = new SimpleDateFormat();

				//调用微服务接口
				String respXml = geneEventService.pushEventQRCodeInfo(xml);
				
				log.info("优宝返回数据： "+respXml);
				
				if(respXml!=null&&respXml.length()>0){
					Map<String,String> respMap = XmlUtils.fetchXmlValue(respXml, "BackXml", 
							new String[]{"status","errorInfo","qrCodeUrl"});
					
					String eventsNo = ymMap.get("eventsNo");
					String batchNo = ymMap.get("batchNo");
					
					//佳宝接口返回的XML中没有qrCodePi，默认设置为qrCodeUrl
					String qrCodeUrl = respMap.get("qrCodeUrl");
					
					//保存二维码图片
					sdf.applyPattern("yyyyMMdd");
					String dateStr = sdf.format(Calendar.getInstance().getTime());
					String fileName = dateStr+ymMap.get("branchCompany")+batchNo+eventsNo+".png";
					//保存位置
					String savePath = PropertiesUtils.getString("foreign","ub.disk.no")+File.separator
							+PropertiesUtils.getString("foreign","ub.qrCodeSaveDir")+File.separator+dateStr;
					
					//下载二维码图片，返回保存的地址
					DownloadThread downThread = new DownloadThread(qrCodeUrl, fileName, savePath);
					ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
					Future<Map<String,String>> future = taskExecutor.submit(downThread);
					
					Map<String, String> resp = future.get();
					retcode = resp.get(fileName);
					log.info("二维码图片保存的物理地址： "+retcode);
					//数据入库				
				}
			} catch (ServiceException e1) {
				retcode="0";
				log.info("retcode e1: "+retcode);
				log.info(e1);
			} catch (Exception e) {
				retcode="0";
				log.info("retcode e: "+retcode);
				log.info(e);
			}
		}else{
			retcode="0";
			log.info("retcode: "+retcode);
			log.info("空数据!");
		}
		return retcode;
	}
	
	@Override
	public String getEventQRCodeInfo(String xml) {
		Logger log = Logger.getLogger("getEventQRCodeInfo");
		String inXml = "";
		try {
			inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
		} catch (UnsupportedEncodingException e1) {
			log.info(e1);
		}
		log.info("优宝请求的数据： "+inXml);
		String retcode="0";
		log.info("retcode: "+retcode);
		if(inXml!=null&&inXml.length()>0) try {
			// 1.调用微信接口，获取生成的二维码信息
			// 2.把二维码数据生成图片，保存到本地，返回二维码图片地址
			// 3.把二维码的相关信息保存到数据库
			Map<String, String> result = XmlUtils.fetchXmlValue(inXml, "eventQRCodeInfo",
					new String[]{"eventsNo", "eventKey", "qrCodePic", "qrCodeUrl"});

			if (result != null && result.size() > 0) {
				ErpQRCodeService qrCodeService = (ErpQRCodeService) SpringTool.getBean(ErpQRCodeService.class);
				boolean flag = qrCodeService.saveFromUB(result);
				if (flag) {
					retcode = "1";
				} else {
					retcode = "0";
				}
				log.info("处理完成~");
				log.info("retcode: " + retcode);
			}
		} catch (Exception e) {
			retcode = "0";
			log.info("retcode: " + retcode);
			log.info(e);
		}
		else{
			retcode="0";
			log.info("retcode: "+retcode);
			log.info("空数据!");
		}

		return retcode;
	}
	
	@Override
	public String setEventQRCodeInvalid(String xml) {
		Logger log = Logger.getLogger("setEventQRCodeInvalid");
		String inXml = "";
		try {
			inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
		} catch (UnsupportedEncodingException e1) {
			log.info(e1);
		}
		log.info("基因系统请求的数据： "+inXml);
		String retcode="0";
		if(inXml!=null&&inXml.length()>0){
			try {
				
				GeneEventServiceServiceLocator locator = new GeneEventServiceServiceLocator();
				String address = PropertiesUtils.getString("foreign","ub.address");
				locator.setGeneEventServicePortEndpointAddress(address);
				GeneEventService geneEventService = locator.getGeneEventServicePort();
				retcode = geneEventService.setEventQRCodeInvalid(inXml);
				log.info("retcode: "+retcode);
			} catch (Exception e) {
				retcode="0";
				log.info("retcode: "+retcode);
				log.info(e);
			}
		}else{
			retcode="0";
			log.info("retcode: "+retcode);
			log.info("空数据!");
		}
		return retcode;
	}
	
	@Override
	public String pushEventCustomerInfo(String xml) {
		Logger log = Logger.getLogger("pushEventCustomerInfo");
		String inXml;
		String retcode="0";
		Map<String,String> map;
		try {
			inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
			log.info("优宝请求的数据： "+inXml);
		if(inXml!=null&&inXml.length()>0){
			map = XmlUtils.fetchXmlValue(inXml,"customer",new String[]{"eventsNo","ownedCompany","salesMan","salesManNo","customerHistory","familyHistory",
					"name","idno","sex","age","phone","code","guardianName","guardianPhone","createTime","setmealName" });
			if(map!=null&&map.keySet().size()>0) {
				geneCustomerService =  (GeneCustomerService) SpringTool.getBean(GeneCustomerService.class);
			    //保存扫描场次二维码传送的信息
				boolean flag = geneCustomerService.saveFromEvent(map);
				if(flag) {
					retcode = "1";
				}
				log.info("retcode: " + retcode);

			}else{
				retcode = "0";
				log.info("retcode: "+retcode);
				log.info("提取XML数据失败");
			}
		}else {
			retcode = "0";
			log.info("retcode: " + retcode);
			log.info("空数据!");
		}
		} catch (Exception e) {
			retcode="0";
			log.info("retcode: "+retcode);
			log.info(e);

		}
		return retcode;
	}
	
	@Override
	public String getComboInfo(String xml) {
		Logger log = Logger.getLogger("getComboInfo");
		String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><comboInfo>";
		String xmlTail = "</comboInfo>";
		String flag = "0";
		String eventsNo = "";
		String inXml;
		String respXml;
		StringBuilder sb = new StringBuilder();
		sb.append("<companyComboList>");
		try {
			if(StringUtils.isNotEmpty(xml)){
				log.info("原始数据： "+xml);
				inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
				log.info("转换后的数据： "+inXml);
				Map<String,String> filterMap = XmlUtils.fetchXmlValue(inXml, "info", 
						new String[]{"uniqueCode", "userName"});
				if(!filterMap.isEmpty()){
					//根据拿到的唯一识别码和客户姓名查找ERP_BXCOMPANY_PRESET表
					ErpBxCompanyPreSetService service = (ErpBxCompanyPreSetService) SpringTool.getBean(ErpBxCompanyPreSetService.class);
					List<ErpBxCompanyPreSet> list = service.getComboInfo(filterMap);
					if(!CollectionUtils.isEmpty(list)){
						flag = "1";
						for(ErpBxCompanyPreSet preSet : list){
							eventsNo = preSet.getEventsNo();
							sb.append("<comboItem>");
							sb.append("<comboName>");
							sb.append(preSet.getComboName());
							sb.append("</comboName>");
							sb.append("<comboDisName>");
							//根据套餐名  申请单号  支公司ID 查询套餐显示名
							String comboShowName = service.getComboShowNameByParam(preSet.getComboName(),preSet.getApplicationNo(),preSet.getCustomerRelationshipId());
							sb.append(comboShowName);
							sb.append("</comboDisName>");
							sb.append("</comboItem>");
						}
					}else{
						sb.append("<comboItem>");
						sb.append("<comboName>");
						sb.append("</comboName>");
						sb.append("<comboDisName>");
						sb.append("</comboDisName>");
						sb.append("</comboItem>");
					}
					sb.append("</companyComboList>");
				}
			}else{
				log.info("flag: "+flag);
				log.info("message: 接收到空数据！");
			}
		} catch (Exception e) {
			log.info("flag: "+flag);
			log.info("message: 网络异常！");
			log.info(e);
		}		
		respXml = xmlHead+"<flag>"+flag+"</flag><eventNo>"+eventsNo+"</eventNo>"+sb.toString()+xmlTail;
		log.info("返回的xml: "+respXml);
		return respXml;
	}


	@Override
	public String pushErpOrder(String xml) {
		Logger log = Logger.getLogger("pushErpOrder");
		
		String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>";
		String xmlTail = "</root>";
		String code = "0";
		String message = "保存失败";		
		String inXml;
		String respXml;
		ErpOrder order;
		
		try {
			if(StringUtils.isNotEmpty(xml)){
				log.info("原始数据： "+xml);
				inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
				log.info("转换后的数据： "+inXml);
				Map<String,String> filterMap = XmlUtils.fetchXmlValue(inXml, "order", 
						new String[]{ErpOrder.F_ORDERNO, ErpOrder.F_DATASOURCE});
				if(!filterMap.isEmpty()){
					ErpOrderService orderService = (ErpOrderService) SpringTool.getBean(ErpOrderService.class);
					List<ErpOrder> list = orderService.findByProps(filterMap);
					
					if(CollectionUtils.isEmpty(list)){
						order = (ErpOrder) JaxbXmlBeanUtils.xml2Bean(xml, new ErpOrder().getClass());
						if(order!=null){
							order.setCreateTime(Calendar.getInstance().getTime());
							log.info("order toString: "+order.toString());
							orderService.save(order);
							code = "1";
							message = "订单号："+order.getOrderNo()	+"， 人员："+order.getName()+" ，保存成功！";
							log.info("code: "+code);
							log.info("message: "+message);
						}
					}else{
						order = list.get(0);
						ErpOrder tempOrder = (ErpOrder) JaxbXmlBeanUtils.xml2Bean(xml, new ErpOrder().getClass());
						if(tempOrder!=null){
							tempOrder.setId(order.getId());	
							tempOrder.setCreateTime(order.getCreateTime());
							tempOrder.setUpdateTime(Calendar.getInstance().getTime());
							orderService.update(tempOrder);
							
							code = "1";
							message = "订单号："+tempOrder.getOrderNo()+"， 人员："+tempOrder.getName()+" ，更新成功！";
							log.info("code: "+code);
							log.info("message: "+message);
						}else{
							code = "0";
							message = "订单号："+filterMap.get(ErpOrder.F_ORDERNO)+" ，提取数据失败，无法更新！";
							log.info("code: "+code);
							log.info("message: "+message);
						}
					}
				}
			}else{
				code = "0";
				message = "接收到空数据！";
				log.info("code: "+code);
				log.info("message: "+message);
			}
		} catch (Exception e) {
			code = "0";
			message = "网络异常！";
			log.info("code: "+code);
			log.info("message: "+message);
			log.info(e);
		}		
		respXml = xmlHead+"<code>"+code+"</code><message>"+message+"</message>"+xmlTail;
		log.info("返回的xml: "+respXml);
		return respXml;
	}

	@Override
	public String productMemberImmediate(String xml) {
		Logger log = Logger.getLogger("productMemberImmediate");

		String inXml = "";
		String retCode = "1";
		StringBuffer reqXmlBuff = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?><DataXml><Header><tranCode>");
		String tranCodeT = "</tranCode>";
		String headerT = "</Header>";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendTime = "<sendTime>"+sdf.format(Calendar.getInstance().getTime())+"</sendTime>";
		String channel = "<channel>gene</channel>";
		String other = "<other>远盟基因增值服务</other>";
		String requestH = "<Request>";
		String reqXmlTail = "</Request></DataXml>";
		String respXml;
		log.info("接收到的XML："+xml);
		if (StringUtils.isNotEmpty(xml)){
			try {
				inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
				log.info("接收到的XML转换成UTF8格式后的文本："+inXml);
				//24位随机数
				String randNumStr = Tools.getTimeStr()+Tools.getNumber7FromRandom();
				log.info("随机生成的交易流水号："+randNumStr);
				reqXmlBuff.append(randNumStr);
				reqXmlBuff.append(tranCodeT);
				reqXmlBuff.append(sendTime);
				reqXmlBuff.append(channel);
				reqXmlBuff.append(other);
				reqXmlBuff.append(headerT);
				reqXmlBuff.append(requestH);
				//添加基因系统传过来的信息
				reqXmlBuff.append(inXml);
				reqXmlBuff.append(reqXmlTail);

				//封装完成后的XML字符串
				log.info("封装完成后的XML字符串： "+reqXmlBuff.toString());
				//获取远程接口
				PmForwardInfoImplServiceLocator locator = new PmForwardInfoImplServiceLocator();
				PmForwardInfoImpl service = locator.getPmForwardInfoImplPort();
				respXml = service.productMemberImmediate(reqXmlBuff.toString());
				log.info("CRM接口返回的数据 respXml: "+respXml);
				if(StringUtils.isNotEmpty(respXml)){
					respXml = respXml.replace("<Result>","").trim();
					respXml = respXml.replace("</Result>","").trim();
					Map<String,String> resultMap = XmlUtils.fetchXmlValue(respXml, "Header",
							new String[]{"tranCode","sendTime","channel","other","retCode","retMsg"});
					if(resultMap!=null&&!resultMap.isEmpty()) {
						retCode = resultMap.get("retCode");
					}
				}
			} catch (ServiceException e) {
				log.info(e);
			} catch (RemoteException e) {
				log.info(e);
			} catch (Exception e) {
				log.info(e);
			}
		}
		return retCode;
	}


	
	/**
	 * 保存相关场次客户信息;
	 * 1、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
	 * （条码生成表中的条码默认规则为7位，以”W”开头，后面为数字：000001、000002……..）
	 * 2、 解析XML报文中的客户信息，将解析出来的客户信息以及步骤一中自动生成的条码信息存储erp_customer表中。
	 * 3、 接口返回步骤一中生成的条形码
	 * create by henry.xu 20161125
	 * @param xml 接口返回xml数据;
	 * @return String 条形码;
 	 */
	@Override
	public String pushCustomerGenerCode(String xml) {
		this.geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		synchronized (LOCK) {
			return this.geneCustomerService.saveCustomerGenerCode(xml);
		}
	}
	
	/**
	 *  客户扫描支公司二维码，填写信息，微服务调用基因系统同pushCustomerGenerCode
	 * create by henry.xu 2016年12月1日
	 * @return
	 */
	public String pushCustomerGCByCompany(String xml) {
		this.geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		synchronized (LOCK) {
			return this.geneCustomerService.saveCustomerGenerCodeByCompany(xml);
		}
	}
	
	/**
	 *  客户扫描支公司二维码，填写信息，微服务调用基因系统同pushCustomerGCByCompanyTaiPing
	 *  针对太平系统使用时间暂时在1个月;
	 * create by henry.xu 2017年02月16日
	 * @return
	 */
	public String pushCustomerGCByCompanyTaiPing(String xml) {
		this.geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		synchronized (LOCK) {
			return this.geneCustomerService.saveCustomerGenerCodeByCompanyTaiPing(xml);
		}
	}
	
	@Override
	public String receiveExamined(String json) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		this.geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		this.geneCustomerService.saveCustomerReceive(json);
		
		String result = "";
		try {
			result = this.geneCustomerService.receiveExaminedObject();
		} catch (Exception e) {
			log.error("异常抛出: "+e.getMessage(), e);
		}
		log.info("推送数据返回值:　result　＝　"+ result);
		return result;
	}
	
	/**
	 * 基因系统验证方案：
		1、 基因系统收到【输入报文】后，解析出场次号。
		2、 查询该场次对应的二维码状态是失效还是有效：
		     a) 如果失效则直接返回0
		     b) 如果未失效，则用当前系统时间与二维码的有效期字段进行比较：
			     如果当前系统时间已经超过二维码的有效期（例如：二维码有效期是2016-11-01 9:00:00，
			     系统当前时间为2016-11-02，注意此处比较只需要用年月日进行比较即可），则返回0；
			     如果当前系统时间还未到二维码的有效期，则返回1.
	 */
	@Override
	public String validateQRCode(String xml) {
		this.geneCustomerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		return geneCustomerService.validateQRCode(xml);
	}

	@Override
	public synchronized String pushCompanyQRCodeInfoWuChuang(String xml) {
		Logger log = Logger.getLogger("pushCompanyQRCodeInfoWuChuang");
		log.info("基因系统请求的数据： "+xml);
		String retcode="0";
		log.info("retcode first: "+retcode);
		if(xml!=null&&xml.length()>0){
			try {
			
				GeneEventServiceServiceLocator locator = new GeneEventServiceServiceLocator();
				//测试地址
				String address = PropertiesUtils.getString("foreign","ub.address");
				locator.setGeneEventServicePortEndpointAddress(address);
				GeneEventService geneEventService = locator.getGeneEventServicePort();
				
				String[] destAttr = {"ownedCompanyId","ownedCompanyName","branchCompanyId","branchCompanyName","projectType","keyword","validHour"};
				Map<String,String> ymMap = null;
				ymMap = XmlUtils.fetchXmlValue(xml, "companyInfo", destAttr);
				//根据	branchCompanyId projectType查询hl_customer_ship_code表  是否重复
				ErpTYCustomerService tyCustomerService = (ErpTYCustomerService) SpringTool.getBean(ErpTYCustomerService.class);
				if(tyCustomerService.checkRepeat(ymMap.get("branchCompanyId"),ymMap.get("projectType"))){
					return retcode;
				}
				SimpleDateFormat sdf = new SimpleDateFormat();

				//调用微服务接口
				String respXml = geneEventService.pushCompanyQRCodeInfoWuChuang(xml);
				
				log.info("优宝返回数据： "+respXml);
				
				if(respXml!=null&&respXml.length()>0){
					Map<String,String> respMap = XmlUtils.fetchXmlValue(respXml, "BackXml", 
							new String[]{"status","errorInfo","qrCodeUrl"});
					
					String branchCompanyId = ymMap.get("branchCompanyId");
					
					//佳宝接口返回的XML中没有qrCodePi，默认设置为qrCodeUrl
					String qrCodeUrl = respMap.get("qrCodeUrl");
					
					//保存二维码图片
					sdf.applyPattern("yyyyMMdd");
					String dateStr = sdf.format(Calendar.getInstance().getTime());
					String fileName = dateStr+ymMap.get("branchCompanyName")+branchCompanyId+".png";
					//保存位置
					String savePath = PropertiesUtils.getString("foreign","disk.commpanyNo")+File.separator
							+PropertiesUtils.getString("foreign","ub.commpanyQrCodeDir")+File.separator+dateStr;
					
					//下载二维码图片，返回保存的地址
					DownloadThread downThread = new DownloadThread(qrCodeUrl, fileName, savePath);
					ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");
					Future<Map<String,String>> future = taskExecutor.submit(downThread);
					
					Map<String, String> resp = future.get();
					retcode = resp.get(fileName);
					log.info("二维码图片保存的物理地址： "+retcode);
					//数据入库				
				}
			} catch (ServiceException e1) {
				retcode="0";
				log.info("retcode e1: "+retcode);
				log.info(e1);
			} catch (Exception e) {
				retcode="0";
				log.info("retcode e: "+retcode);
				log.info(e);
			}
		}else{
			retcode="0";
			log.info("retcode: "+retcode);
			log.info("空数据!");
		}
		return retcode;
	}

	/**
	 * 客户身份验证
	 *
	 * @param xml
	 * @return String
	 * @auther Damian
	 * @since 2016-12-28
	 */
	@Override
	public String getCustomerAuth(String xml) {
		Logger log = Logger.getLogger("getCustomerAuth");
		String respXml;
		log.info("接收到的xml "+xml);
		if(StringUtils.isNotEmpty(xml)){
			hkService = (GeneServiceImplHK) SpringTool.getBean(GeneServiceImplHK.class);
		    respXml = hkService.getCustomerAuth(xml);
		}else{
			respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<respAuthInfo>\n" +
					"<result>0</result>\n" +
					"<message>您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。</message>\n" +
					"<mealName></mealName>\n" +
					"<eventNo></eventNo>\n" +
					"<reportReceiveName></reportReceiveName>\n" +
					"<reportReceivePhone></reportReceivePhone>\n" +
					"<reportReceiveAddress></reportReceiveAddress>\n" +
					"</respAuthInfo>";

		}
		log.info("推送给微服务的xml: "+respXml);
		return respXml;
	}
	
	/**
	 * 检测码验证
	 *
	 * @param xml
	 * @return String
	 * @auther machuan
	 * @since 2017-01-18
	 */
	@Override
	public String getIDAuth(String xml) {
		Logger log = Logger.getLogger("getIDAuth");
		String respXml = null;
		log.info("接收到的xml "+xml);
		tyService = (GeneServiceImplTY) SpringTool.getBean(GeneServiceImplTY.class);
	    respXml = tyService.getIDAuth(xml);
		log.info("推送给微服务的xml: "+respXml);
		return respXml;
	}

	/**
	 * 保存客户信息
	 *
	 * @param xml
	 * @return String
	 * @auther Damian
	 * @since 2016-12-28
	 */
	@Override
	public String pushCustomerInfoHK(String xml) {
		Logger log = Logger.getLogger("pushCustomerInfoHK");
		String respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<respCustomer>\n" +
				"<result>0</result>\n" +
				"<message>保存失败</message>\n" +
				"</respCustomer>";

		log.info("接收到的xml "+xml);
		if(StringUtils.isNotEmpty(xml)){
			hkService = (GeneServiceImplHK) SpringTool.getBean(GeneServiceImplHK.class);
			respXml = hkService.pushCustomerInfoHK(xml);
		}
		log.info("推送给微服务的xml: "+respXml);
		return respXml;
	}
	
	
	/**
	 * 保存客户信息 TY
	 * @param xml
	 * @return String
	 * @auther machuan
	 * @since 2017-01-18
	 */
	@Override
	public String pushCustomerInfo(String xml) {
		Logger log = Logger.getLogger("pushCustomerInfoTY");
		String respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<respCustomer>\n" +
				"<result>0</result>\n" +
				"<message>保存失败</message>\n" +
				"</respCustomer>";

		log.info("接收到的xml "+xml);
		//String reqXml = Tools.decodeStr(xml);
		//log.info("转换成UTF8格式后的xml字符串： "+reqXml);
		if(StringUtils.isNotEmpty(xml)){
			tyService = (GeneServiceImplTY) SpringTool.getBean(GeneServiceImplTY.class);
			respXml = tyService.pushCustomerInfo(xml);
		}
		log.info("推送给微服务的xml: "+respXml);
		return respXml;
	}

	/**
	 * 客户状态推送
	 *
	 * @param xml
	 * @return String
	 * @auther Damian
	 * @since 2016-12-28
	 */
	@Override
	public String pushCustomerStatus(String xml) {
		Logger log = Logger.getLogger("pushCustomerStatus");
		String respXml;
		log.info("接收到的xml "+xml);
		if(StringUtils.isNotEmpty(xml)){
			hkService = (GeneServiceImplHK) SpringTool.getBean(GeneServiceImplHK.class);
			respXml = hkService.pushCustomerStatus(xml);
		}else{
			respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<respStatus>\n" +
					"<result>0</result>\n" +
					"<message>失败</message>\n" +
					"</respStatus>";
		}
		log.info("微服务返回的xml: "+respXml);
		return respXml;
	}

	/**
	 * 华夏，易安，北京邮政项目：查询预导入表进行客户信息验证
	 *
	 * @param xml XML格式的字符串
	 * @return String
	 * @auther Damian
	 * @since 2017-01-17
	 */
	@Override
	public String getCustAuth(String xml) {
		Logger log = Logger.getLogger("getCustAuth");
		String respXml;
		log.info("接收到的xml "+xml);
		if(StringUtils.isNotEmpty(xml)){
			hkService = (GeneServiceImplHK) SpringTool.getBean(GeneServiceImplHK.class);
			respXml = hkService.getCustAuth(xml);
		}else{
			respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<respStatus>\n" +
					"<result>0</result>\n" +
					"<message>失败</message>\n" +
					"</respStatus>";
		}
		log.info("传送给微服务xml: "+respXml);
		return respXml;
	}
	
	@Override
	public String getGeneReportInfoMyMutil(String idcard, String name, String tel) {
		imgTaskService = (ErpReportdetailImgtaskService) SpringTool.getBean(ErpReportdetailImgtaskService.class);
		Logger log = Logger.getLogger("getGeneReportInfoImg");
		String resultXml = null;
		log.info("服务器接收数据-格式String: idcard="+idcard+", name="+name+", tel="+tel);
		
		//调用查询;
		resultXml = imgTaskService.findGeneReportInfoMyMutil(idcard, name, tel);
		
		log.info("服务器接收数据-格式XML: "+resultXml);
		return resultXml;
	}

	/**
	 * 微磁客户报告JPG查询
	 *
	 * @param name     姓名
	 * @param tel      电话
	 * @param birthday 生日,格式 yyyy-MM-dd
	 * @return String XML字符串
	 * @auther Damian
	 * @since 2017-02-06
	 */
	@Override
	public String getGeneReportInfoImg(String name, String tel, String birthday) {
		Logger log = Logger.getLogger("getGeneReportInfoImg");
		String respXml = null;
		log.info("接收到微服务的数据： name="+name+", tel="+tel+", birthday="+birthday);
		try {
			imgTaskService = (ErpReportdetailImgtaskService) SpringTool.getBean(ErpReportdetailImgtaskService.class);
			Map<String,String> params = new HashMap<String, String>();
			params.put(ErpReportdetailImgtask.F_USERNAME, name);
			params.put(ErpReportdetailImgtask.F_PHONENO, tel);
			params.put(ErpReportdetailImgtask.F_BIRTHDAY, birthday);
			respXml = imgTaskService.findImg(params);
		} catch (Exception e) {
			log.info(e);
		}
		log.info("返回给微服务的数据： "+ respXml);
		return respXml;
	}

	/**
	 * 无创-生物电客户报告JPG查询接口
	 *
	 * @param name 姓名
	 * @param tel 电话
	 * @param idno 身份证号
	 * @return String XML字符串
	 * @auther Damian
	 * @since 2017-02-10
	 */
	@Override
	public String getBlyReportInfoImg(String name, String tel, String idno) {
		Logger log = Logger.getLogger("getBlyReportInfoImg");
		String respXml = null;
		log.info("接收到微服务的数据： name="+name+", tel="+tel+", idno="+ idno);
		try {
			imgTaskService = (ErpReportdetailImgtaskService) SpringTool.getBean(ErpReportdetailImgtaskService.class);
			Map<String,String> params = new HashMap<String, String>();
			params.put(ErpReportdetailImgtask.F_USERNAME, name);
			params.put(ErpReportdetailImgtask.F_PHONENO, tel);
			params.put(ErpReportdetailImgtask.F_IDNO, idno);
			respXml = imgTaskService.findImg(params);
		} catch (Exception e) {
			log.info(e);
		}
		log.info("返回给微服务的数据： "+ respXml);
		return respXml;
	}

	/**
	 * 客户状态推送(太平微磁TPWC）
	 * @param xml
	 * @return String
	 * @auther Damian
	 * @since 2017-02-17
	 */
	@Override
	public String pushCustomerStatusTPWC(String xml) {
		Logger log = Logger.getLogger("pushCustomerStatusTPWC");
		String respXml = null;
		try {
		    log.info("xml: "+xml);
			if (StringUtils.isNotEmpty(xml)) {
				weiciMsgService = weiciLocator.getWeiciMsgServicePort();
				respXml = weiciMsgService.pushCustomerStatus(xml);
			}
		} catch (ServiceException e) {
		    log.info(e);
		} catch (RemoteException e) {
		    log.info(e);
		}
		log.info("respXml: "+respXml);
		return respXml;
	}

	/**
	 * 客户状态推送(星宁基因）
	 * @param xml
	 * @return String
	 * @auther Damian
	 * @since 2017-04-17
	 */
	@Override
	public String pushCustomerStatusXN(String xml) {
	    Logger log = Logger.getLogger("pushCustomerStatusXN");
	    String respXml = null;
		try {
			log.info("xml: "+xml);
			if (StringUtils.isNotEmpty(xml)) {
				TransferDataWebService xnService = xnLocator.getTransferDataWebServicePort();
				respXml = xnService.pushCustomerState(xml);
			}
		} catch (ServiceException e) {
		    log.info(e);
		} catch (RemoteException e) {
			log.info(e);
		}
		log.info("星宁基因返回的数据: "+respXml);
		return respXml;
	}

	 /**
	  * 
	  * 基因通用报告JPG查询接口
	  * 姓名（必输）、手机号（非必输）、证件号（非必输）   注意：手机号和证件号有一个是必须输入项
	  * 
	  * @param idcard 	身份证号
	  * @param name		姓名
	  * @param tel		电话
	  * @return String
	  * @author LeslieTong
	  * @date 2017年4月20日15:03:53
	  */
	@Override
	public String getGeneReportInfoAll(String idcard, String name, String tel) {
		Logger log = Logger.getLogger("getGeneReportInfoAll");
		log.info("idcard:"+idcard+",name:"+name+",tel:"+tel);
	    String respXml = null;
	    
		try {
			imgTaskService = (ErpReportdetailImgtaskService) SpringTool.getBean(ErpReportdetailImgtaskService.class);
			Map<String,String> params = new HashMap<String, String>();
			params.put(ErpCustomer.F_NAME, name);
			params.put(ErpCustomer.F_PHONE, tel);
			params.put(ErpCustomer.F_IDNO, idcard);
			respXml = imgTaskService.assembleReportInfo(params);
		} catch (Exception e) {
			log.info("getGeneReportInfoAll exception -- "+e);
		}
	    
	    log.info("基因通用报告JPG查询接口返回的数据: "+respXml);
		return respXml;
	}

}
