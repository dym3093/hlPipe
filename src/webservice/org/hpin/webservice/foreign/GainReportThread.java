/**
 * @author DengYouming
 * @since 2016-10-11 下午4:27:42
 */
package org.hpin.webservice.foreign;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.bean.*;
import org.hpin.webservice.service.*;
import org.hpin.webservice.util.HttpUtils;
import org.hpin.webservice.util.PropertiesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 根据传入的信息，下载报告
 * @author DengYouming
 * @since 2016-10-11 下午4:27:42
 */
public class GainReportThread implements Callable<Map<String,String>> {

	Logger log = Logger.getLogger("GainReportThread");
	Logger dealLog = Logger.getLogger("dealReport");
	Logger jyErrLog = Logger.getLogger("jyErrorInfo"); //1）金域返回的会员信息与数据库中的会员不一致，2）会员报告无法下载
	Logger jyNoReportLog = Logger.getLogger("jyNoReport"); //数据库中有该会员，但金域没有报告的
	
	private JSONObject reqJsonObj;
	private String reqJsonStr;
	
	public static final String REPORT_DIR = "reportDir";
	public static final String REPORT_DETAIL_DIR = "reportDetailDir";
	
	public GainReportThread(JSONObject reqJsonObj) {
		super();
		this.setReqJsonObj(reqJsonObj);
	}

	public GainReportThread(String reqJsonStr) {
		super();
		this.setReqJsonStr(reqJsonStr);
	}

	@Override
	public Map<String, String> call() throws Exception {
		Map<String, String> respMap = null;
		if(getReqJsonObj()!=null&&getReqJsonObj().length()>0){
			respMap = this.dealService(getReqJsonObj(), null);			
		}else{
			log.info("reqJsonObj为空");
		}
		
		if(StringUtils.isNotEmpty(getReqJsonStr())){
			respMap = this.dealService(null, getReqJsonStr());
		}else{
			log.info("reqJsonStr为空");
		}
		
		return respMap;
	}
	

	public JSONObject getReqJsonObj() {
		return reqJsonObj;
	}

	public void setReqJsonObj(JSONObject reqJsonObj) {
		this.reqJsonObj = reqJsonObj;
	}

	public String getReqJsonStr() {
		return reqJsonStr;
	}

	public void setReqJsonStr(String reqJsonStr) {
		this.reqJsonStr = reqJsonStr;
	}
	
	
	/**
	 * 处理下载报告业务
	 * @return Map
	 * @author DengYouming
	 * @throws Exception 
	 * @since 2016-10-11 下午4:46:29
	 */
	private Map<String,String> dealService(JSONObject json, String jsonStr) throws Exception{
		Map<String,String> dealMap = new HashMap<String,String>();
		YmGeneReportServiceImpl ymGeneReportService = (YmGeneReportServiceImpl)SpringTool.getBean(YmGeneReportServiceImpl.class);
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)SpringTool.getBean("taskExecutor");	
		//封装
		JSONObject rcvJson = null;
		//报告数据
		String report ;
		//报告单所保存的文件夹
		String saveDetailDir ;
		
		JSONObject tempJsonReport ;
		
		if(getReqJsonObj()!=null&&getReqJsonObj().length()>0){
			rcvJson = json;
		}		
		if(StringUtils.isNotEmpty(jsonStr)){
			rcvJson = new JSONObject(jsonStr);
		}
		//根据情况进行判定
		if(rcvJson!=null){
			/**
			//下载类型，默认为0:全部下载， 1：只下载基因报告， 2：只下载报告单
			String down = "0";
			if(rcvJson.has("down")){
				down = rcvJson.getString("down");
				rcvJson.remove("down");
			}
			
			if(rcvJson.has("reportId")){
				log.info("只下载报告单: "+rcvJson.toString());
				saveDetailDir = this.dealReportDetailPrev(rcvJson.toString(), ymGeneReportService, taskExecutor);
				log.info("报告单保存文件夹  saveDetailDir:"+saveDetailDir);
				if(StringUtils.isNotEmpty(saveDetailDir)){
					dealMap.put(REPORT_DETAIL_DIR, saveDetailDir);
				}
			}else{
				//只下载基因报告
				if("1".equals(down)){
					log.info("只下载基因报告: "+rcvJson.toString());
					report = ymGeneReportService.gainReportInfo(rcvJson.toString());
					log.info("返回结果  report:"+report);
					if(StringUtils.isNotEmpty(report)){
						//处理基因报告，返回基因报告单ID
						String result = this.dealReport(report, ymGeneReportService, taskExecutor);
						log.info("处理基因报告返回结果  result:"+result);
						tempJsonReport = new JSONObject(result);
						//返回报告所在的文件夹
						dealMap.put(REPORT_DIR, tempJsonReport.getString(REPORT_DIR));
					}
				}
				//只下载报告单
				else if("2".equals(down)){
					log.info("只下载报告单: "+rcvJson.toString());
					//返回保存路径
					saveDetailDir = this.dealReportDetailPrev(rcvJson.toString(), ymGeneReportService, taskExecutor);
					log.info("报告单保存文件夹  saveDetailDir:"+saveDetailDir);
					if(StringUtils.isNotEmpty(saveDetailDir)){
						dealMap.put(REPORT_DETAIL_DIR, saveDetailDir);
					}
				}
				//基因报告和报告单都下载
				else{
					//获取基因报告信息
					report = ymGeneReportService.gainReportInfo(rcvJson.toString());
					log.info("返回结果  report:"+report);
					if(StringUtils.isNotEmpty(report)){
						//处理基因报告，返回基因报告单ID
						String result = this.dealReport(report, ymGeneReportService, taskExecutor);
						log.info("返回结果  result:"+result);
						//根据基因报告ID，处理基因报告单相关业务
						if(StringUtils.isNotEmpty(result)){
							tempJsonReport = new JSONObject(result);
							//返回报告所在的文件夹
							dealMap.put(REPORT_DIR, tempJsonReport.getString(REPORT_DIR));
							saveDetailDir = this.dealReportDetailPrev(result, ymGeneReportService, taskExecutor);
							log.info("报告单保存文件夹  saveDetailDir:"+saveDetailDir);
							if(StringUtils.isNotEmpty(saveDetailDir)){
								dealMap.put(REPORT_DETAIL_DIR, saveDetailDir);
							}
						}
					}
				}
			}
			*/
			saveDetailDir = this.dealReportDetailPrev(rcvJson.toString(), ymGeneReportService, taskExecutor);
			dealMap.put(REPORT_DETAIL_DIR, saveDetailDir);
		}
		return dealMap;
	}

	/**
	 * 处理基因报告
	 * @param report
	 * @param ymGeneReportService
	 * @param taskExecutor
	 * @return String
	 * @author DengYouming
	 * @since 2016-10-27 下午7:20:10
	 */
	private synchronized String dealReport(String report, YmGeneReportServiceImpl ymGeneReportService, ThreadPoolTaskExecutor taskExecutor) {
		JSONObject resultJson = new JSONObject();
		
		ErpEventsService erpEventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
		ErpReportdetailPDFContentService pdfContentService = (ErpReportdetailPDFContentService)SpringTool.getBean(ErpReportdetailPDFContentService.class);
		GeneCustomerService customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		ErpPrintTaskContentService contentService = (ErpPrintTaskContentService)SpringTool.getBean(ErpPrintTaskContentService.class);
		
		//报告列表
		JSONArray reportArr ;
		//检测人总数
		Integer total = 0;
		//循环次数
		int counter = 0;
		
		dealLog.info("report："+report);
		if(report!=null&&report.length()>0){
			JSONObject respJson ;
			try {
				respJson = new JSONObject(report);
				int code = respJson.getInt("code");
				if(200==code){
					String data = respJson.getString("data");
					JSONObject dataJson = new JSONObject(data);
					if(dataJson!=null&&dataJson.length()>0){
						//邮件内容
						StringBuffer mailContent = new StringBuffer();
						//报告列表
						reportArr = dataJson.getJSONArray("reports");
						if(reportArr!=null&&reportArr.length()>0){
							ErpEvents events = null;
							//获取场次信息
							List<ErpEvents> eventsList = null;
							//会员
							ErpCustomer entity = null;
							//保存基因报告的物理路径
							String reportPath = null;
							//下载的基因报告名称
							String fileNameReport = null;
							//格式化日期
							SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
							Date now = Calendar.getInstance().getTime();
							String nowDate = sdfDate.format(now);
							//本次下载基因报告保存的文件夹
							String reportDir = null;
							//保存报告单ID
							Map<String,String> reportIdMap = new LinkedHashMap<String, String>();
							//更新计数
							int updateNum = 0;
							//异常下载计数
							int errorNum = 0;
							//用于年龄
							SimpleDateFormat birthSdf = new SimpleDateFormat("yyyy-MM-dd");
							Date rightNow = Calendar.getInstance().getTime();
							
							dealLog.info("报告总数:"+reportArr.length());
							//报告
							JSONObject myObj = null;
							int len = reportArr.length();
							total = len;
							for (int i=0; i<len; i++) {
								
								entity = null;								
								reportPath = null;
								myObj = reportArr.getJSONObject(i);
								counter += 1;
								
								String reportId = myObj.getString("reportId"); //报告单ID
								String barcode = myObj.getString("barcode");//条码(唯一的)
								String serviceId = myObj.getString("serviceId");//服务ID
								String reportName = myObj.getString("name"); //报告单名称
								String name = myObj.getString("username");//受检人姓名
								
								String gender = myObj.getString("gender")!=null?myObj.getString("gender"):"男";
								String phone = myObj.getString("phone")!=null?myObj.getString("phone"):""; //受检人手机号码
								Date birthday = myObj.getString("birthday")!=null?birthSdf.parse(myObj.getString("birthday")): Calendar.getInstance().getTime();//生日
								String showUrl = myObj.getString("showUrl")!=null?myObj.getString("showUrl"):"";//预览地址
								String pdfUrl = myObj.getString("pdfUrl")!=null?myObj.getString("pdfUrl"):"";//pdf地址

								dealLog.info("解析的报告信息： 报告单ID["+reportId+"],报告单名称["+reportName+"],团单号["+serviceId+"],姓名["+name+"],条码["+barcode+"],性别["+gender+"],电话["+phone
										+"],生日["+birthday.getTime()+"],pdf地址["+pdfUrl+"],预览地址["+showUrl+"]");
								
								//根据条码查询该会员所在场次信息
								dealLog.info("姓名["+name+"],根据条码["+barcode+"]查询场次信息...");
								eventsList = erpEventsService.listEventsByInfo(barcode, "code");
								if(eventsList!=null&&eventsList.size()>0){
									events = eventsList.get(0);
									dealLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，正在执行第 ["+counter+"]次遍历...");
									
									String combo = null;
									//根据生日计算日期
									int age = calculateAge(rightNow, birthday);
									
									if(barcode.contains("-")){
										barcode = barcode.substring(0, barcode.lastIndexOf("-"));
									}
									dealLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]中金域返回的会员信息 ： "
												+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"],基因报告PDF地址[ "+pdfUrl+"]");		
								
									//查找数据库是否有匹配的会员信息
									List<ErpCustomer> existList = customerService.findByProps(name, barcode);
									
									if(!CollectionUtils.isEmpty(existList)){
										entity = existList.get(0);
										entity.setUpdateTime(Calendar.getInstance().getTime()); //修改时间
										entity.setUpdateUserName("金域");
										//设置套餐
										combo = entity.getSetmealName();
										//会员中没有pdf路径且已出报告，则下载
										//重新修正报告保存位置
										//多个报告，以批次做文件夹
										if(len>1){
											reportDir = PropertiesUtils.getString("foreign","disk.no")+File.separator
												+PropertiesUtils.getString("foreign","dir.jyRp")+File.separator+nowDate
												+ File.separator+events.getBatchNo();
										}else{
											//单个报告，
											reportDir = PropertiesUtils.getString("foreign","disk.no")+File.separator
													+PropertiesUtils.getString("foreign","dir.jyRp")+File.separator+nowDate
													+ File.separator+barcode;
										}
										if(null!=entity){
											//添加报告ID
											reportIdMap.put(entity.getCode(), reportId);
											//判定是否需要重新下载报告:1）报告路径为空  2）报告路径不含有后缀名
											if(StringUtils.isEmpty(entity.getPdffilepath()) || !entity.getPdffilepath().contains(".")){
												//报告地址是否完整
												if(StringUtils.isNotEmpty(pdfUrl)){
													//下载pdf文档，返回在服务器上的保存地址
													try{
														//获取后缀
														String fileSuff = pdfUrl.substring(pdfUrl.lastIndexOf("."));
														if(StringUtils.isEmpty(fileSuff)){
															fileSuff = ".pdf";
														}
														//文件重命名
														fileNameReport = entity.getCode()+fileSuff;
														// 1) 执行下载基因报告任务
														dealLog.info("姓名："+name+",条码："+barcode+",pdffilepath字段为空，开始下载报告...");
														Future<Map<String, String>> futrue = taskExecutor.submit(new DownloadThread(pdfUrl, fileNameReport, reportDir));
														Map<String, String> respMap = futrue.get();
														if(!"200".equals(respMap.get(HttpUtils.CODE))){
															continue;
														}
														reportPath = respMap.get(fileNameReport);
														String fileSize = respMap.get("fileSize");
														dealLog.info("姓名："+name+",条码："+barcode+",报告下载完成，基因报告保存的实际位置： "+reportPath);
														dealLog.info("姓名："+name+",条码："+barcode+",报告下载完成，文件大小： "+fileSize+" Byte");
														//已下载，则更新对应路径
														if(StringUtils.isNotEmpty(reportPath)&&reportPath.contains(fileSuff)){
															//更新预览地址
															String viewPath = reportPath.replace(PropertiesUtils.getString("foreign","disk.no")
																								,PropertiesUtils.getString("foreign","viewPath_head"));
															dealLog.info("["+entity.getCode()+" , "+ entity.getName()+"]的报告预览路径["+viewPath+"]");
															//更新PDF预览路径
															entity.setPdffilepath(viewPath);
															//更新会员信息
															customerService.update(entity);
															dealLog.info("姓名："+name+",条码："+barcode+",在erp_customer表中的预览地址已更新为： "+viewPath);
															//添加到打印任务
															this.deal4PrintTask(reportPath, entity, events, pdfContentService, contentService);
															//更新数量计数
															updateNum +=1;
														}else{
															jyErrLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]第"+(++errorNum)+"条错误数据...");
															jyErrLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]， 无法下载该会员报告：	" +
																	"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"] ");
															jyErrLog.info("reportPath: "+reportPath);
															//存在该会员但目前无法下载，把该会员放入定时任务
															boolean flag_add = this.addToSchedule(ErpCustomer.F_CODE, entity.getCode(), entity.getId());
															if(flag_add){
																dealLog.info("无法下载的会员添加到定时任务: 批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，"+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"]");
															}else{
																dealLog.info("添加定时任务失败！: 批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，"+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"]");
															}
															continue;
														}
													}catch(Exception e){
														dealLog.info(e);
														jyErrLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]第"+(++errorNum)+"条错误数据...");
														jyErrLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]， 下载报告时出现错误：	" +
																"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"],基因报告PDF地址[ "+pdfUrl+"]");
														//下载错误，把该会员放入定时任务
														boolean flag_add = this.addToSchedule(ErpCustomer.F_CODE, entity.getCode(), entity.getId());
														if(flag_add){
															dealLog.info("无法下载的会员添加到定时任务: 批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，"+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"]");
														}else{
															dealLog.info("添加定时任务失败！: 批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，"+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"]");
														}
														continue;
													}
												}else{
													dealLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，没有基因报告地址：	" +
																"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"] "+"，pdfUrl["+pdfUrl+"]");
													jyNoReportLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，没有基因报告地址：	" +
																"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"] "+"，pdfUrl["+pdfUrl+"]");
													continue;
												}
											}else{
												dealLog.info("姓名："+name+",条码："+barcode+",已有报告，信息完整，无需下载！");
												continue;
											}
										}
									}else{
										//找不到该会员
										dealLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]中不存在该会员 ： "
												+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"],基因报告PDF地址[ "+pdfUrl+"]");
										jyNoReportLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]中不存在该会员 ： "
												+"姓名["+name+"]，条码["+barcode+"]，电话["+phone+"],基因报告PDF地址[ "+pdfUrl+"]");
										continue;
									}
								}else{
									dealLog.info("姓名["+name+"]，条码["+barcode+"]，数据库中不存在对应的场次");
									jyErrLog.info("数据库中不存在对应的场次");
									continue;
								}
							}
							dealLog.info("批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()+"]，场次总人数["+events.getHeadcount()
									+"]，本次执行，金域返回的人数["+reportArr.length()+"]，下载的人数["+updateNum+"]，本次执行出现错误的条数["+errorNum+"]");
						
							resultJson.put("total", ""+reportArr.length());
							resultJson.put("msg", "本次执行的报告总数：["+reportArr.length()+"]份 ，已下载报告数量：["+updateNum+"]份");
							//邮件内容
							mailContent.append("远盟基因：\n");
							mailContent.append("本次下载报告任务执行的信息如下：\n");
							mailContent.append("场次号: "+events.getEventsNo()+" ， \n");
							mailContent.append("场次时间: "+events.getEventDate()+" ， \n");
							mailContent.append("销售负责人: "+events.getCreateUserName()+" , \n");
							mailContent.append("本次金域推送的报告数量（份）: "+total+"  ， 已下载的报告数量（份）：  "+updateNum+" , 无法下载的报告数量（份）："+errorNum+"\n");
							dealLog.info("邮件内容: "+mailContent.toString());
							resultJson.put("mailContent", mailContent.toString());
							//返回报告保存路径
							resultJson.put(REPORT_DIR, reportDir);
							//返回报告ID
							if(reportIdMap!=null&&reportIdMap.keySet().size()>0){
								JSONObject mapJson = new JSONObject(reportIdMap);
								resultJson.put("reportIdMap", mapJson);
							}else{
								resultJson.put("reportIdMap", "");
							}
							resultJson.put("code", ""+200);
						}
					}
				}
			} catch (JSONException e2) {
				dealLog.info(e2);
			} catch (Exception e) {
				dealLog.info(e);
			}
		}
		return resultJson.toString();
	}
	
	/**
	 * 报告单预处理流程
	 * @param jsonStr
	 * @param ymGeneReportService
	 * @param taskExecutor
	 * @return String
	 * @author DengYouming
	 * @since 2016-10-28 上午11:38:26
	 */
	private String dealReportDetailPrev(String jsonStr, YmGeneReportServiceImpl ymGeneReportService, ThreadPoolTaskExecutor taskExecutor ){
		//返回结果集 
		JSONObject resultJson = null;
		//用于获取报告单明细的JSON
		JSONObject detailJson = null;
		//格式化日期
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
		Date now = Calendar.getInstance().getTime();
		String nowDate = sdfDate.format(now);
		//报告单保存的文件夹
		String saveDir = null;
		//
		List<ErpEvents> eventsList = null;
		//场次
		ErpEvents events = null;
		//会员
		ErpCustomer entity = null;
		//会员集合
		List<ErpCustomer> customerList = null;
		//接受到的数据
		JSONObject rcvJson = null;
		try {
			//数据转换成JSON
			rcvJson = new JSONObject(jsonStr);
			//条码
			String barcode = null;
			//姓名
			String name = null;
			//报告ID
			String reportId = null;
			//报告ID集合的JSON
			JSONObject reportIdsJson = null;
			if(rcvJson!=null){
				ErpReportDetailService reportDetailService = (ErpReportDetailService)SpringTool.getBean(ErpReportDetailService.class);
				ErpEventsService erpEventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
				ErpReportdetailPDFContentService pdfContentService = (ErpReportdetailPDFContentService)SpringTool.getBean(ErpReportdetailPDFContentService.class);
				GeneCustomerService customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
				ErpPrintTaskContentService contentService = (ErpPrintTaskContentService)SpringTool.getBean(ErpPrintTaskContentService.class);
				
				//查询参数
				Map<String,String> params = new HashMap<String, String>();
				//报告单信息
				String respDetailInfo = null;
				//case1: 先执行了  dealReport(String report, YmGeneReportServiceImpl ymGeneReportService, ThreadPoolTaskExecutor taskExecutor) 方法，
				//已下载报告后，返回的数据有 reportIdMap
				if(rcvJson.has("reportIdMap")){
					String reportIdMapStr = rcvJson.getString("reportIdMap");
					dealLog.info("reportIdMapStr:　"+reportIdMapStr);
					
					//转换
					if(StringUtils.isNotEmpty(reportIdMapStr)){
						reportIdsJson = new JSONObject(reportIdMapStr);
						if(reportIdsJson!=null){
							Iterator<String> iter = reportIdsJson.keys();
							while(iter.hasNext()){
								entity = null;
								events = null;
								
								barcode = iter.next();//条码
								reportId = reportIdsJson.getString(barcode);//报告单号
								//根据条码查询该会员所在场次信息
								eventsList = erpEventsService.listEventsByInfo(barcode, "code");
								if(eventsList!=null&&eventsList.size()>0){
									events = eventsList.get(0);
									//查询会员信息
									params.clear();
									//经过基因报告处理返回的条码均在数据库中有数据，不必要添加姓名条件查找
									params.put(ErpCustomer.F_CODE, barcode);
									customerList = customerService.listCustomerByProps(params);
									if(customerList!=null&&customerList.size()>0){
										entity = customerList.get(0);
										
										detailJson = new JSONObject();
										detailJson.put("reportId", reportId);
										//根据报告单ID查询报告单信息
										respDetailInfo = ymGeneReportService.gainReportInfoDetail(detailJson.toString());
										dealLog.info("返回的报告单信息："+respDetailInfo);
										///TODO
					if(StringUtils.isNotEmpty(respDetailInfo)){
						log.info("发起线程前的JSON字符串 reqStr: "+respDetailInfo );
						JSONObject receviceJson = new JSONObject(respDetailInfo);
						if(receviceJson.has("data")){
							JSONObject respDataJson = receviceJson.getJSONObject("data");
							if(respDataJson.has("report")) {
								String reportInfo = respDataJson.getString("report");
								//有报告信息，则为金域传送的数据，另外进行解析处理
								if (StringUtils.isNotEmpty(reportInfo)) {
									DecodeReportThread reportThread = new DecodeReportThread(reportInfo);
									Future<Map<String, String>> future = taskExecutor.submit(reportThread);
								}
							}
						}
					}

										//根据批次和套餐名修改保存目录
										//报告单保存的文件夹
										//TODO
										/**
										saveDir = PropertiesUtils.getString("foreign","disk.no")+File.separator
													+PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate
													+ File.separator+events.getBatchNo()+File.separator+entity.getSetmealName();
										//保存报告单
										resultJson = this.saveReportDetail(respDetailInfo, reportId, saveDir, entity, events,  taskExecutor, 
																			reportDetailService, ymGeneReportService, pdfContentService, contentService);
										 */
									}else{
										jyErrLog.info("方法[dealReportDetailPrev]，批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()
												+"]，中查询不到相关的会员信息: 条码["+barcode+"] , 姓名["+name+"]");
										dealLog.info("方法[dealReportDetailPrev]，远盟数据库中查询不到相关的会员信息,条码["+barcode+"] , 姓名["+name+"]");
									}
								}else{
									jyErrLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
									dealLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
								}
							}
						}
					}
				}
				//case2: 直接根据报告ID下载对应的报告单
				else if(rcvJson.has("reportId")){
					//根据reportId获取报告单信息
					respDetailInfo = ymGeneReportService.gainReportInfoDetail(rcvJson.toString());
					dealLog.info("返回的报告单信息："+respDetailInfo);
					//TODO
					if(StringUtils.isNotEmpty(respDetailInfo)){
						log.info("发起线程前的JSON字符串 reqStr: "+respDetailInfo );
						JSONObject receviceJson = new JSONObject(respDetailInfo);
						if(receviceJson.has("data")){
							JSONObject respDataJson = receviceJson.getJSONObject("data");
							if(respDataJson.has("report")) {
								String reportInfo = respDataJson.getString("report");
								//有报告信息，则为金域传送的数据，另外进行解析处理
								if (StringUtils.isNotEmpty(reportInfo)) {
									DecodeReportThread reportThread = new DecodeReportThread(reportInfo);
									Future<Map<String, String>> future = taskExecutor.submit(reportThread);
								}
							}
						}
					}
					/**
					reportId = rcvJson.getString("reportId");
					
					//根据报告单信息获取条码
					JSONObject result = new JSONObject(respDetailInfo);
					Integer code = result.getInt(HttpUtils.CODE);
					if(code==HttpStatus.SC_OK){
						JSONObject dataJson = result.getJSONObject(HttpUtils.DATA);
						if(dataJson!=null){
							JSONObject reportDetailJson = dataJson.getJSONObject("report");
							if(reportDetailJson!=null){
								entity = null;
								events = null;
								dealLog.info("报告单明细： "+reportDetailJson.toString());
								barcode = reportDetailJson.getString("barcode");//获取条码
								name = reportDetailJson.getString("username"); //获取姓名
								//根据条码查询该会员所在场次信息
								eventsList = erpEventsService.listEventsByInfo(barcode, ErpCustomer.F_CODE);
								if(eventsList!=null&&eventsList.size()>0){
									events = eventsList.get(0);
									//查询会员信息
									params.clear();
									params.put(ErpCustomer.F_CODE, barcode);
									params.put(ErpCustomer.F_NAME, name);
									customerList = customerService.listCustomerByProps(params);
									if(customerList!=null&&customerList.size()>0){
										entity = customerList.get(0);
										//根据批次和套餐名修改保存目录
										saveDir = PropertiesUtils.getString("foreign","disk.no")+File.separator
												+PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate
												+ File.separator+events.getBatchNo()+File.separator+entity.getSetmealName();
										resultJson = this.saveReportDetail(respDetailInfo, reportId, saveDir, entity, events, taskExecutor, 
												reportDetailService, ymGeneReportService, pdfContentService, contentService);
									}else{
										jyErrLog.info("方法[dealReportDetailPrev]，批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()
														+"]，中查询不到相关的会员信息: 条码["+barcode+"] , 姓名["+name+"]");
										dealLog.info("方法[dealReportDetailPrev]，远盟数据库中查询不到相关的会员信息,条码["+barcode+"] , 姓名["+name+"]");
									}
								}else{
									jyErrLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
									dealLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
								}
							}else{
								dealLog.info("报告单明细为空！");
							}
						}
					}
					 */
				}
				//case3: 传过来的数据是  code, name, batchNo, groupOrderNo, serviceId, eventsNo
				else{
					//根据其他信息获取报告信息在进行解析获取reportId
					//根据姓名和条码为单条报告，根据场次的信息则为多条报告
					//需要遍历循环，组装数据
					String reportStr = ymGeneReportService.gainReportInfo(rcvJson.toString());
					dealLog.info("reportStr: "+reportStr);
					if(StringUtils.isNotEmpty(reportStr)){
						JSONObject reportJson = new JSONObject(reportStr);
						int code = reportJson.getInt("code");
						if(HttpStatus.SC_OK==code){
							String data = reportJson.getString("data");
							JSONObject dataJson = new JSONObject(data);
							if(dataJson!=null&&dataJson.length()>0){
								//报告列表
								JSONArray reportArr = dataJson.getJSONArray("reports");
								if(reportArr!=null&&reportArr.length()>0){
									//报告
									JSONObject myObj = null;
									JSONObject reqJson = new JSONObject();
									dealLog.info("报告单ID数量： 	"+reportArr.length());
									for (int i=0; i<reportArr.length(); i++) {
										myObj = reportArr.getJSONObject(i);
										//获取报告ID
										reportId = myObj.getString("reportId");
										//单条报告
										if(StringUtils.isNotEmpty(reportId)){
											//添加reportId
											reqJson.put("reportId", reportId);
											//根据reportId获取报告单信息
											respDetailInfo = ymGeneReportService.gainReportInfoDetail(reqJson.toString());
											dealLog.info("返回的报告单信息："+respDetailInfo);
											//TODO
											if(StringUtils.isNotEmpty(respDetailInfo)){
												JSONObject receviceJson = new JSONObject(respDetailInfo);
												if(receviceJson.has("data")){
													JSONObject respDataJson = receviceJson.getJSONObject("data");
													if(respDataJson.has("report")) {
														String reportInfo = respDataJson.getString("report");
														//有报告信息，则为金域传送的数据，另外进行解析处理
														if (StringUtils.isNotEmpty(reportInfo)) {
															DecodeReportThread reportThread = new DecodeReportThread(reportInfo);
															Future<Map<String, String>> future = taskExecutor.submit(reportThread);
														}
													}
												}
											}

											/**
											//根据报告单信息获取条码
											JSONObject result = new JSONObject(respDetailInfo);
											Integer code2 = result.getInt(HttpUtils.CODE);
											if(code2==HttpStatus.SC_OK){
												JSONObject dataJsonNext = result.getJSONObject(HttpUtils.DATA);
												if(dataJson!=null){
													JSONObject reportDetailJson = dataJsonNext.getJSONObject("report");
													if(reportDetailJson!=null){
														entity = null;
														events = null;
														dealLog.info("报告单明细： "+reportDetailJson.toString());

														barcode = reportDetailJson.getString("barcode");//获取条码
														name = reportDetailJson.getString("username"); //获取姓名
														//根据条码查询该会员所在场次信息
														eventsList = erpEventsService.listEventsByInfo(barcode, ErpCustomer.F_CODE);
														if(eventsList!=null&&eventsList.size()>0){
															events = eventsList.get(0);
															//查询会员信息
															params.clear();
															params.put(ErpCustomer.F_CODE, barcode);
															params.put(ErpCustomer.F_NAME, name);
															customerList = customerService.listCustomerByProps(params);
															if(customerList!=null&&customerList.size()>0){
																entity = customerList.get(0);
																//根据批次和套餐名修改保存目录
																saveDir = PropertiesUtils.getString("foreign","disk.no")+File.separator
																		+PropertiesUtils.getString("foreign","dir.jyRpDetail")+File.separator+nowDate
																		+ File.separator+events.getBatchNo()+File.separator+entity.getSetmealName();
																resultJson = this.saveReportDetail(respDetailInfo, reportId, saveDir, entity, events, taskExecutor,
																		reportDetailService, ymGeneReportService, pdfContentService, contentService);
															}else{
																jyErrLog.info("方法[dealReportDetailPrev]，批次号["+events.getBatchNo()+"]，场次["+events.getEventsNo()+"]，团单号["+events.getGroupOrderNo()
																				+"]，中查询不到相关的会员信息: 条码["+barcode+"] , 姓名["+name+"]");
																dealLog.info("方法[dealReportDetailPrev]，远盟数据库中查询不到相关的会员信息,条码["+barcode+"] , 姓名["+name+"]");
															}
														}else{
															jyErrLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
															dealLog.info("方法[dealReportDetailPrev]， 远盟数据库中查询不到相关的场次信息, 条码["+barcode+"] , 姓名["+name+"]");
														}
													}else{
														dealLog.info("报告单明细为空！");
													}
												}
											}*/
										}else{
											log.info("无法获取reportId，不向金域接口发起请求");
											dealLog.info("无法获取reportId，不向金域接口发起请求");
										}
									}
									dealLog.info("没有获取到报告内容!");
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveDir;
	}

	/**
	 * 下载报告单并保存报告单信息
	 * @param reportDetail 从金域获取报告单内容
	 * @param reportId 报告ID
	 * @param saveReportDetailDir 保存的文件夹
	 * @param entity 会员
	 * @param events 场次
	 * @param taskExecutor
	 * @param reportDetailService
	 * @param ymGeneReportService
	 * @param pdfContentService
	 * @param contentService
	 * @return JSONObject
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-28 上午11:29:19
	 */
	private JSONObject saveReportDetail(String reportDetail, String reportId, String saveReportDetailDir, ErpCustomer entity, ErpEvents events, ThreadPoolTaskExecutor taskExecutor,
                                        ErpReportDetailService reportDetailService, YmGeneReportServiceImpl ymGeneReportService,
                                        ErpReportdetailPDFContentService pdfContentService, ErpPrintTaskContentService contentService) throws Exception{
		JSONObject result = new JSONObject();
		//调用接口获取报告单内容
		//从金域获取报告单内容
		//报告单的物理路径
		String reportDetailPath = null;
		//报告单的名称
		String reportDetailName = null;
		//报告单详情
		ErpReportDetail reportDetailObj = null;
		//
		List<ErpReportDetail> detailList = null;
		//
		Map<String,String> params = new HashMap<String, String>();
		
		//报告ID
		if(StringUtils.isNotEmpty(reportDetail)){
			//下载报告单,返回已下载的报告单名称和保存路径组成的Map，映射成JSON字符串
			String detailStr = this.downLoadReportDetail(reportDetail, saveReportDetailDir, entity, taskExecutor);
			//处理报告单信息
			JSONObject respDetailJson = new JSONObject(detailStr);
			Iterator<String> iterDetail = respDetailJson.keys();
			
			while(iterDetail.hasNext()){
				//报告单文件名（条码+编号）
				reportDetailName = iterDetail.next();
				//报告单保存的物理地址
				reportDetailPath = respDetailJson.getString(reportDetailName);
				if(StringUtils.isEmpty(reportDetailPath)){
					continue;
				}
				//添加到打印任务
				boolean flag_print = this.deal4PrintTask(reportDetailPath, entity, events, pdfContentService, contentService);
				if(flag_print){
					dealLog.info("方法[saveReportDetail]， 场次号["+events.getEventsNo()+"]，批次号["+events.getBatchNo()+"]，团单号["+events.getGroupOrderNo()+"]，" +
								" 姓名["+entity.getName()+"]， 条码["+entity.getCode()+"]，报告单["+reportDetailName+"]，保存位置["+reportDetailPath+"]，已添加到打印任务!");
				}else{
					dealLog.info("方法[saveReportDetail]， 场次号["+events.getEventsNo()+"]，批次号["+events.getBatchNo()+"]，团单号["+events.getGroupOrderNo()+"]，" +
							" 姓名["+entity.getName()+"]， 条码["+entity.getCode()+"]，报告单["+reportDetailName+"]，保存位置["+reportDetailPath+"]，未能添加到打印任务!");
				}
				
				params.clear();
				params.put(ErpReportDetail.F_CODE, entity.getCode());
				params.put(ErpReportDetail.F_FILENAME, reportDetailName);
				detailList = reportDetailService.listRerortDetailByProps(params);
				if(!CollectionUtils.isEmpty(detailList)){
					reportDetailObj = detailList.get(0);
					
					reportDetailObj.setFilePath(reportDetailPath);//物理地址
					
					String suffix = reportDetailName.substring(reportDetailName.lastIndexOf(".")+1);
					reportDetailObj.setFileSuffix(suffix);//后缀名
					
					//Linux服务器上的切割方式
					//预览地址
					String viewPathDetail = reportDetailPath.replace(PropertiesUtils.getString("foreign","disk.no")
																	,PropertiesUtils.getString("foreign","viewPath_head"));
					dealLog.info("["+entity.getCode()+" , "+ entity.getName()+"]的报告预览路径["+viewPathDetail+"]");
					reportDetailObj.setViewPath(viewPathDetail);//预览地址
					
					reportDetailObj.setReportId(reportId);//金域报告ID，可用于重新获取报告
					reportDetailObj.setUpdateTime(Calendar.getInstance().getTime());
					//更新
					reportDetailService.update(reportDetailObj);
					dealLog.info("方法[saveReportDetail]，场次号["+events.getEventsNo()+"]，批次号["+events.getBatchNo()+"]，团单号["+events.getGroupOrderNo()+"]，" +
							" 姓名["+entity.getName()+"]， 条码["+entity.getCode()+"]，报告单["+reportDetailName+"]，保存位置["+reportDetailPath+"]，已更新到报告单明细表!");
				}else{
					//保存报告单信息
					reportDetailObj = new ErpReportDetail();
					
					reportDetailObj.setFileName(reportDetailName);//文件名
					reportDetailObj.setFilePath(reportDetailPath);//物理地址
					String suffix = reportDetailName.substring(reportDetailName.lastIndexOf(".")+1);
					reportDetailObj.setFileSuffix(suffix);//后缀名
					
					//Linux服务器上的切割方式
					//预览地址
					String viewPathDetail = reportDetailPath.replace(PropertiesUtils.getString("foreign","disk.no")
																	,PropertiesUtils.getString("foreign","viewPath_head"));
					dealLog.info("["+entity.getCode()+" , "+ entity.getName()+"]的报告预览路径["+viewPathDetail+"]");
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
					dealLog.info("方法[saveReportDetail]，场次号["+events.getEventsNo()+"]，批次号["+events.getBatchNo()+"]，团单号["+events.getGroupOrderNo()+"]，" +
							" 姓名["+entity.getName()+"]， 条码["+entity.getCode()+"]，报告单["+reportDetailName+"]，保存位置["+reportDetailPath+"]，已保存到报告单明细表!");
				}
			}
		}
		return result;
	}
	
	/**
	 * 下载报告单，返回报告单保存的相关信息
	 * @param content
	 * @param saveDir
	 * @param taskExecutor
	 * @return String
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @author DengYouming
	 * @since 2016-10-26 下午4:48:32
	 */
	private String downLoadReportDetail(String content, String saveDir, ErpCustomer entity, ThreadPoolTaskExecutor taskExecutor ) 
																throws JSONException, InterruptedException, ExecutionException{
		//返回 文件名作为key，文件的物理地址作为value的JSON对象
		JSONObject respJson = null;//返回的内容
		JSONObject result = null;
		JSONArray rawResults = null;
		if(content!=null&&content.length()>0){
			try{
			respJson = new JSONObject();
			result = new JSONObject(content);
			dealLog.info("返回结果 content:"+content);
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
							String httpUrl = null; // 所在的url
							String fileName = null; //文件名
							String suffix = null; //文件后缀名
							String physicalPath = null; //实际保存物理路径
							ErpReportDetailService detailService = (ErpReportDetailService) SpringTool.getBean(ErpReportDetailService.class);
							List<ErpReportDetail> existDetails = null;
							HashMap<String,String> detailMap = new HashMap<String,String>();
							int num = 0;
							int len = rawResults.length();
							for (int i = 0; i < len; i++) {
								httpUrl = rawResults.getString(i);
								dealLog.info("httpUrl: "+httpUrl);
								//获取后缀
								suffix = httpUrl.substring(httpUrl.lastIndexOf("."));
								fileName = barcode+"_"+(++num)+suffix;
								detailMap.clear();
								detailMap.put(ErpReportDetail.F_CODE, barcode);
								detailMap.put(ErpReportDetail.F_FILENAME,fileName);
								existDetails = detailService.listRerortDetailByProps(detailMap);
								if(!CollectionUtils.isEmpty(existDetails)){
									continue;
								}
								Future<Map<String, String>> futrue = taskExecutor.submit(new DownloadThread(httpUrl, fileName, saveDir));
								Map<String, String>respMap = futrue.get();
								if(!"200".equals(respMap.get(HttpUtils.CODE))){
									continue;
								}
								physicalPath = respMap.get(fileName);
								String fileSize = respMap.get("fileSize");
								//非空，而且路径中有后缀
								if(StringUtils.isNotEmpty(physicalPath)&&physicalPath.contains(".")){
									//文件名为key，文件的物理地址为value
									respJson.put(fileName, physicalPath);
									dealLog.info("报告单["+fileName+"] 下载成，保存的物理路径： "+physicalPath);
									dealLog.info("报告单["+fileName+"] 大小： "+fileSize);
								}else{
									dealLog.info(httpUrl+" 下载失败： ");
									//存在该会员但目前无法下载，把该会员放入定时任务
									boolean flag_add = this.addToSchedule(ErpCustomer.F_CODE, entity.getCode(), entity.getId());
									if(flag_add){
										dealLog.info("无法下载的会员，已添加到定时任务: 姓名["+entity.getName()+"]，条码["+entity.getCode()+"]，电话["+entity.getPhone()+"]");
									}else{
										dealLog.info("添加定时任务失败！: 姓名["+entity.getName()+"]，条码["+entity.getCode()+"]，电话["+entity.getPhone()+"]");
									}
									continue;
								}
							}
						}
					}
				}
			}else{
				dealLog.info("状态码code: "+code);
				dealLog.info("错误信息: "+result.getString("msg"));
			}
			}catch(JSONException e1){
				dealLog.info(e1);
				throw e1;
			}catch(InterruptedException e2){
				dealLog.info(e2);
				throw e2;
			}catch(ExecutionException e3){
				dealLog.info(e3);
				throw e3;
			} catch (Exception e) {
				dealLog.info(e);
			}
		}else{
			dealLog.info("传入的为空数据！");
		}
		return respJson.toString();
	}
	
	/**
	 * 把要打印的文件添加到打印任务
	 * @param filePath 文件的物理路径 
	 * @param entity 客户信息
	 * @param events 场次信息
	 * @param pdfContentService
	 * @param contentService
	 * @return boolean
	 * @author DengYouming
	 * @since 2016-10-26 下午2:14:56
	 */
	private boolean deal4PrintTask(String filePath, ErpCustomer entity, ErpEvents events,
			ErpReportdetailPDFContentService pdfContentService, ErpPrintTaskContentService contentService){
		//返回标志
		boolean flag = false;
		ErpReportdetailPDFContent pdfContentObj = null;

		dealLog.info("处理打印任务，条码："+entity.getCode()+", 姓名： "+entity.getName());
		dealLog.info("物理路径： "+filePath);
		String name = entity.getName();
		String barcode = entity.getCode();
		String age = entity.getAge();
		String gender = entity.getSex();
		String combo = entity.getSetmealName();
		Date date = Calendar.getInstance().getTime();
		//查找PDF匹配报告
		List<ErpReportdetailPDFContent> pdfList = null;
		
		String pdfName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
		
		try {
			pdfList = pdfContentService.findByProps(barcode, pdfName);
				
			if(StringUtils.isNotEmpty(filePath)&&filePath.contains(".")){
				
				String type = "3";
				if(StringUtils.containsIgnoreCase(filePath, "pdf")){
					type = "2";
				}
				
				if(!CollectionUtils.isEmpty(pdfList)){
					pdfContentObj = pdfList.get(0);
					if(pdfContentObj!=null){
						//无文件路径或者文件路径不正常，则更新，否则不更新
						if(StringUtils.isEmpty(pdfContentObj.getFilepath())|| !pdfContentObj.getFilepath().contains(".")){
							pdfContentObj.setUsername(name); //修复bug用
							pdfContentObj.setFilepath(filePath);
							pdfContentObj.setUpdateTime(date);
							pdfContentService.update(pdfContentObj);
							dealLog.info("条码："+entity.getCode()+", 姓名： "+entity.getName()+"，erp_reportdetail_pdfcontent 表更新完毕!!!");
							/**@since 2016年10月8日15:18:37 @author Carly*/
							List<ErpPrintTaskContent> contentList = contentService.getContentByPdfId(pdfContentObj.getId()); 
							if(contentList.size()!=0){
								ErpPrintTaskContent content2 = contentList.get(0);
								content2.setFilePath(filePath);
								content2.setUserName(name);
								content2.setUpdateTime(date);
								content2.setType(type);
								contentService.update(content2);
								dealLog.info("条码："+entity.getCode()+", 姓名： "+entity.getName()+"，erp_print_task_content 表更新完毕!!!");
							}
							flag = true;
						}else{
							dealLog.info("姓名："+name+",条码："+barcode+",ERP_REPORTDETAIL_PDFCONTENT表中信息完整，无需更新！");
						}
					}
					
				}else{
				
					pdfContentObj = new ErpReportdetailPDFContent();
					
					pdfContentObj.setPdfname(pdfName);
					pdfContentObj.setUsername(name);
					pdfContentObj.setAge(""+age);
					pdfContentObj.setCode(barcode);
					pdfContentObj.setSex(gender);
					pdfContentObj.setSetmeal_name(combo);
					
//					pdfContentObj.setFilesize(filesize);
//					pdfContentObj.setMd5(md5);
					pdfContentObj.setBatchno(events.getBatchNo());
					pdfContentObj.setFilepath(filePath);
					pdfContentObj.setIsrecord(2);
					pdfContentObj.setIsrepeat(0);
					
					pdfContentObj.setCreatedate(Calendar.getInstance().getTime());
					pdfContentObj.setProvice(entity.getProvice());
					pdfContentObj.setCity(entity.getCity());
					pdfContentObj.setBranch_company(events.getBranchCompanyId());
					pdfContentObj.setEvents_no(events.getEventsNo());
														
					pdfContentObj.setPs("0");
					pdfContentObj.setSettlement_status("0");
					
					pdfContentService.saveEntity(pdfContentObj);

					dealLog.info("条码："+entity.getCode()+", 姓名： "+entity.getName()+"，erp_reportdetail_pdfcontent 数据已添加!!!");

					//add by chenqi @since 2016年10月11日12:01:34 添加到需要打印的表（ErpPrintTaskContent）中
					List<ErpCustomer> customerList = contentService.getCustomerInfoByCode(pdfContentObj.getCode());
					CustomerRelationShipPro shipPro = contentService.getProjectCodeByEvent(customerList.get(0).getEventsNo());
					ErpPrintTaskContent contents = new ErpPrintTaskContent();
					contents.setAge(pdfContentObj.getAge());
					contents.setBatchNo(pdfContentObj.getBatchno());
					contents.setBranchCompanyId(pdfContentObj.getBranch_company());
					contents.setProvince(pdfContentObj.getProvice());
					contents.setCity(pdfContentObj.getCity());
					contents.setCode(pdfContentObj.getCode());
					contents.setUserName(pdfContentObj.getUsername());
					contents.setCombo(pdfContentObj.getSetmeal_name());
					contents.setGender(pdfContentObj.getSex());
					contents.setBatchNo(pdfContentObj.getBatchno());
					contents.setFilePath(pdfContentObj.getFilepath());
					contents.setSaleman(pdfContentObj.getSales_man());
					contents.setPdfContentId(pdfContentObj.getId());
	
					contents.setCustomerId(customerList.get(0).getId());
					contents.setDept(customerList.get(0).getDepartment());
					contents.setOwnedCompanyId(customerList.get(0).getOwnedCompanyId());

					if (shipPro!=null) {
						contents.setProjectCode(shipPro.getProjectCode());
					}
					contents.setPs("0");
					contents.setType(type);
					contents.setIsManuallyAdd("2");
					contents.setCreateTime(date);
					contents.setCreateUser("金域");

					contents.setReportType(1); // modify 2016-12-12

					contentService.save(contents);
					dealLog.info("条码："+entity.getCode()+", 姓名： "+entity.getName()+"，erp_print_task_content 表数据已添加!!!");

					flag = true;
					}
				}
		} catch (Exception e) {
			dealLog.info(e);
		}
		
		return flag;
	}
	
	/**
	 * 添加到定时任务
	 * @param infoType
	 * @param info
	 * @param customerId
	 * @return
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-31 下午5:17:00
	 */
	private boolean addToSchedule(String infoType, String info, String customerId) throws Exception{
		boolean flag = false;
		ErpScheduleJob job = null;
		
		if(StringUtils.isNotEmpty(info)&&StringUtils.isNotEmpty(infoType)){
			ErpScheduleJobService service = (ErpScheduleJobService)SpringTool.getBean(ErpScheduleJobService.class);
//			GeneCustomerService customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
			Map<String,String> params = new HashMap<String, String>();
			params.put(ErpScheduleJob.F_INFOTYPE, infoType);
			params.put(ErpScheduleJob.F_INFO, info);
			List<ErpScheduleJob> list = service.listScheduleJobByProps(params);
//			Map<String,String> otherParams = new HashMap<String, String>();
//			List<ErpCustomer> customerList = customerService.listCustomerByProps(otherParams);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Calendar.getInstance().getTime();
			String scheduleTime = sdf.format(date)+" 02:00:00";
			if(list!=null&&list.size()>0){
				job = list.get(0);
				job.setRelatedId(customerId);
				job.setRelatedTable("ERP_CUSTOMER");
				job.setInfoType(infoType);
				job.setInfo(info);
				job.setScheduleTime(scheduleTime);
				job.setUpdateTime(date);
				int num = job.getCount()+1;
				job.setCount(num);
				service.update(job);
				flag = true;
			}else{
				job = new ErpScheduleJob();
				job.setRelatedId(customerId);
				job.setRelatedTable("ERP_CUSTOMER");
				job.setInfoType(infoType);
				job.setInfo(info);
				job.setScheduleTime(scheduleTime);
				job.setCreateTime(date);
				int num = job.getCount()+1;
				job.setCount(num);
				service.save(job);
				flag = true;
			}
		}
		return flag;
	}
	
	//计算根据出生日期年龄
	private static int calculateAge(Date rightNow, Date birthday){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String nowStr = null;
		if(rightNow!=null){
			nowStr = sdf.format(rightNow);
		}else{
			nowStr = sdf.format(Calendar.getInstance().getTime());
		}
		String birthStr = sdf.format(birthday);
		return Integer.valueOf(nowStr)-Integer.valueOf(birthStr);
	}
	
}
