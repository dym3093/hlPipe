/**
 * @author DengYouming
 * @since 2016-7-29 上午9:31:03
 */
package org.hpin.webservice.foreign;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.ErpPrintTaskContent;
import org.hpin.webservice.bean.ErpReportdetailPDFContent;
import org.hpin.webservice.service.ErpEventsService;
import org.hpin.webservice.service.ErpPrintTaskContentService;
import org.hpin.webservice.service.ErpReportdetailPDFContentService;
import org.hpin.webservice.service.GeneCustomerService;
import org.hpin.webservice.service.YmGeneReportServiceImpl;
import org.hpin.webservice.util.PropertiesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

/**
 * 根据团单号获取会员数据并下载检测报告的线程
 * @author DengYouming
 * @since 2016-7-29 上午9:31:03
 */
public class TesteesThread implements Callable<String> {

	private String serviceId;
	Logger logger = Logger.getLogger("TesteesThread");
	Logger jyErrLog = Logger.getLogger("jyErrorInfo"); //1）金域返回的会员信息与数据库中的会员不一致，2）会员报告无法下载
	Logger jyNoReportLog = Logger.getLogger("jyNoReport"); //数据库中有该会员，但金域没有报告的日子
	public TesteesThread(String serviceId) {
		this.setServiceId(serviceId);
	}

	@Override
	public String call() throws Exception{
		JSONObject resultJson = new JSONObject();
		
		YmGeneReportServiceImpl ymGeneReportService = (YmGeneReportServiceImpl)SpringTool.getBean(YmGeneReportServiceImpl.class);
		ErpEventsService erpEventsService = (ErpEventsService)SpringTool.getBean(ErpEventsService.class);
		ErpReportdetailPDFContentService pdfContentService = (ErpReportdetailPDFContentService)SpringTool.getBean(ErpReportdetailPDFContentService.class);
		GeneCustomerService customerService = (GeneCustomerService)SpringTool.getBean(GeneCustomerService.class);
		ErpPrintTaskContentService contentService = (ErpPrintTaskContentService)SpringTool.getBean(ErpPrintTaskContentService.class);
		//检测人列表
		JSONArray testArr = null;
		//检测人总数
		Integer total = 0;
		//循环次数
		int counter = 0;
		
		int pageNum = 0;
		int pageSize = 200;
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("serviceId", this.getServiceId());
		//获取检测人数据
		String content = ymGeneReportService.findTestees(paramJson.toString());
		logger.info("查找联系人返回的信息："+content);
		if(content!=null&&content.length()>0){
			JSONObject respJson = new JSONObject(content);
			String code = respJson.getString("code");
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
							if(paramJson.has("page")){
								paramJson.remove("page");
							}
							paramJson.put("page", pageNum);
							contentNext = ymGeneReportService.findTestees(paramJson.toString());
							JSONObject respJsonNext = new JSONObject(contentNext);
							String codeNext = respJson.getString("code");
							if("200".equals(codeNext)){
								String dataNext = respJsonNext.getString("data");
								JSONObject dataJsonNext = new JSONObject(dataNext);
								if(dataJsonNext!=null&&dataJsonNext.length()>0){
									testArrNext = dataJsonNext.getJSONArray("testees");
									if(testArrNext!=null){
										logger.info("第【"+pageNum+"】页返回的json数据："+testArr.toString());
										for (int j = 0; j < testArrNext.length(); j++) {
											testArr.put(testArrNext.get(j));
										}
									}
								}
							}
						}
					}
						
					StringBuffer mailContent = new StringBuffer();
					
					if(testArr!=null&&testArr.length()>0){
						Map<String,String> pMap = new HashMap<String, String>();
						pMap.put(ErpEvents.F_GROUPORDERNO, serviceId);
						//根据团单好获取场次信息
						List<ErpEvents> eventsList = erpEventsService.listEventsByProps(pMap);
						
						if(!eventsList.isEmpty()){
							ErpEvents events = eventsList.get(0);
							
							List<ErpReportdetailPDFContent> pdfContentList = new ArrayList<ErpReportdetailPDFContent>();
							ErpCustomer entity = null;
							ErpReportdetailPDFContent pdfContentObj = null;
							String filePath = null;
							//更新计数
							int updateNum = 0;
							//异常计数
							int errorNum = 0;
							
							SimpleDateFormat birthSdf = new SimpleDateFormat("yyyy-MM-dd");
							Date rightNow = Calendar.getInstance().getTime();
							
							logger.info("总人数:"+testArr.length());
							JSONObject myObj = null;
							for (int i=0; i<testArr.length(); i++) {
								
							try {
								myObj = testArr.getJSONObject(i);
								counter += 1;
								logger.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】，正在执行第 【"+counter+"】次遍历...");
								entity = null;								
								filePath = null;
								
								String name = myObj.getString("name");
								String gender = myObj.getInt("gender")==1?"女":"男";
								String phone = myObj.getString("phone");
								Date birthday = myObj.getString("birthday")!=null?birthSdf.parse(myObj.getString("birthday")): Calendar.getInstance().getTime();
								String testeeId = myObj.getString("testeeId");
								String seqNum = myObj.getString("seqNum");
								String hasReport = myObj.getString("hasReport"); 
								String productionName = myObj.getString("productionName"); //套餐名
								
								String combo = null;
								if(productionName!=null){
									String subStr = ""+productionName.charAt(productionName.length()-3);
									if("(".equals(subStr)){
										combo = productionName.substring(productionName.lastIndexOf("-")+1, productionName.lastIndexOf("("));
									}
									if("（".equals(subStr)){
										combo = productionName.substring(productionName.lastIndexOf("-")+1, productionName.lastIndexOf("（"));
									}
								}
								
								//根据生日计算日期
								int age = calculateAge(rightNow, birthday);
								
								//获取条码
								String barcode = myObj.getJSONArray("tubes").getJSONObject(0).getString("barcode");
								if(barcode.contains("-")){
									barcode = barcode.substring(0, barcode.lastIndexOf("-"));
								}
								logger.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】中金域返回的会员信息 ： "
											+"姓名【"+name+"】，条码【"+barcode+"】，电话【"+phone+"】,是否有报告【 "+(hasReport=="true"?"是":"否")+"】");		
							
								//查找数据库是否有相同数据
								List<ErpCustomer> existList = customerService.findByProps(name, barcode);
								
								if(!CollectionUtils.isEmpty(existList)){
									entity = existList.get(0);
									entity.setUpdateTime(Calendar.getInstance().getTime()); //修改时间
									entity.setUpdateUserName("金域");
								}else{
									jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】，本次执行的第"+(++errorNum)+"条错误数据...");
									jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】中查不到该会员信息：  " +
											"姓名【"+name+"】，条码【"+barcode+"】，电话【"+phone+"】   ");
								}
								
								/*else{
									entity = new ErpCustomer();
															
									entity.setName(name);
									entity.setSex(gender);
									entity.setPhone(phone);
									entity.setCode(barcode);
									entity.setTestInstitution("金域");
									
									entity.setAge(""+age);
									entity.setSetmealName(combo);
									entity.setEventsNo(events.getEventsNo());
									entity.setBranchCompany(events.getBranchCompany());
									entity.setBranchCompanyId(events.getBranchCompanyId());

									entity.setOwnedCompany(events.getOwnedCompany());
									entity.setOwnedCompanyId(events.getOwnedCompanyId());
									entity.setCity(events.getCity());
									entity.setProvice(events.getProvice());
									entity.setYmSalesman(events.getYmSalesman());
									
									entity.setSampleType("口腔粘膜上皮细胞"); //默认样本类型
									entity.setSamplingDate(events.getEventDate()); //采样日期为场次日期
									entity.setStatus("0");
									entity.setIsDeleted(0);
									entity.setEventsTime(events.getEventDate());
									
									entity.setCreateTime(new Date());
									entity.setCreateUserName(events.getCreateUserName());
									entity.setCreateUserId(events.getCreateUserId());
									
								}*/
								
								//会员中没有pdf路径且已出报告，则下载
								if(null!=entity){
									if(StringUtils.isEmpty(entity.getPdffilepath()) || entity.getPdffilepath().length()<55){
										if("true".equalsIgnoreCase(hasReport)){
											JSONObject jData = new JSONObject();
											jData.put("serviceId", serviceId);
											jData.put("barcode", barcode);
											jData.put("combo",entity.getSetmealName()); //TODO 临时
											jData.put("batchNo", events.getBatchNo()); //TODO 临时
											//下载pdf文档，返回在服务器上的保存地址
											try{
												logger.info("姓名："+name+",条码："+barcode+",pdffilepath字段为空，开始下载报告...");
												filePath = ymGeneReportService.gainReport(jData.toString());
												logger.info("姓名："+name+",条码："+barcode+",报告下载完成，报告保存的实际位置： "+filePath);
												//已下载，则更新对应路径
												if(StringUtils.isNotEmpty(filePath)&&filePath.length()>15){
													String subPath = filePath.substring(filePath.indexOf(File.separator));
													if(entity!=null){
														//更新预览地址
														String viewPath = "ftp://gene:gene@geneym.healthlink.cn/gene"+subPath;
														entity.setPdffilepath(viewPath);
														customerService.update(entity);
														logger.info("姓名："+name+",条码："+barcode+",在erp_customer表中的预览地址已更新为： "+viewPath);
														//更新数量计数
														updateNum +=1;
													}
												}else{
													jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】第"+(++errorNum)+"条错误数据...");
													jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】， 无法下载该会员报告：	" +
															"姓名【"+name+"】，条码【"+barcode+"】，电话【"+phone+"】 ");
													jyErrLog.info("filePath: "+filePath);

												}
											}catch(Exception e){
												logger.info(e.getMessage());
												jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】第"+(++errorNum)+"条错误数据...");
												jyErrLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】， 下载报告错误：	" +
														"姓名【"+name+"】，条码【"+barcode+"】，电话【"+phone+"】  ");
												continue;
											}
											logger.info("filePath: "+filePath);
										}else{
											jyNoReportLog.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】，没有报告：	" +
														"姓名【"+name+"】，条码【"+barcode+"】，电话【"+phone+"】  ");
										}
									}else{
										logger.info("姓名："+name+",条码："+barcode+",已有报告，信息完整，无需下载！");
									}
								}
								
								//PDF匹配报告
								List<ErpReportdetailPDFContent> pdfList = null;
								if(StringUtils.isNotEmpty(filePath)){
									String pdfName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
									pdfList = pdfContentService.findByProps(barcode,pdfName);
								}
								//ErpReportdetailPDFContent表相关的实际路径
								String pdfFilePath = null;
								
								//
								if(StringUtils.isNotEmpty(filePath)&&filePath.length()>15){
									pdfFilePath = filePath;
								}else{
									if(entity!=null){
										//非空，且预览路径正常
										if(StringUtils.isNotEmpty(entity.getPdffilepath())&&entity.getPdffilepath().length()>55){
											pdfFilePath = PropertiesUtils.getString("foreign","disk.no")+File.separator
													+entity.getPdffilepath().substring(entity.getPdffilepath().lastIndexOf("jy"));
										}
									}
								}
									
								if(StringUtils.isNotEmpty(pdfFilePath)&&pdfFilePath.length()>15){
									Date date = Calendar.getInstance().getTime();
									if(!CollectionUtils.isEmpty(pdfList)){
										pdfContentObj = pdfList.get(0);
										if(pdfContentObj!=null){
											//无文件路径或者文件路径不正常，则更新，否则不更新
											if(StringUtils.isEmpty(pdfContentObj.getFilepath())||pdfContentObj.getFilepath().length()<15
													||pdfContentObj.getFilepath().startsWith("D")){
												pdfContentObj.setUsername(name); //修复bug用
												pdfContentObj.setFilepath(pdfFilePath);
												pdfContentObj.setUpdateTime(new Date());
												pdfContentService.update(pdfContentObj);
												
												/**@since 2016年10月8日15:18:37 @author Carly*/
												List<ErpPrintTaskContent> contentList = contentService.getContentByPdfId(pdfContentObj.getId()); 
												if(contentList.size()!=0){
													ErpPrintTaskContent content2 = contentList.get(0);
													content2.setFilePath(pdfFilePath);
													content2.setUserName(name);
													content2.setUpdateTime(date);
													contentService.update(content2);
												}
											}else{
												logger.info("姓名："+name+",条码："+barcode+",ERP_REPORTDETAIL_PDFCONTENT表中信息完整，无需更新！");
											}
										}
										
									}else{
									
										pdfContentObj = new ErpReportdetailPDFContent();
										
										pdfContentObj.setPdfname(barcode+".pdf");
										pdfContentObj.setUsername(name);
										pdfContentObj.setAge(""+age);
										pdfContentObj.setCode(barcode);
										pdfContentObj.setSex(gender);
										pdfContentObj.setSetmeal_name(combo);
										
	//									pdfContentObj.setFilesize(filesize);
	//									pdfContentObj.setMd5(md5);
										pdfContentObj.setBatchno(events.getBatchNo());
										pdfContentObj.setFilepath(pdfFilePath);
										pdfContentObj.setIsrecord(2);
										pdfContentObj.setIsrepeat(0);
										
										pdfContentObj.setCreatedate(Calendar.getInstance().getTime());
										pdfContentObj.setProvice(events.getProvice());
										pdfContentObj.setCity(events.getCity());
										pdfContentObj.setBranch_company(events.getBranchCompanyId());
										pdfContentObj.setEvents_no(events.getEventsNo());
																			
										pdfContentObj.setPs("0");
										pdfContentObj.setSettlement_status("0");
										
										pdfContentList.add(pdfContentObj);										
										pdfContentService.saveEntity(pdfContentObj);
										
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
										contents.setType("2");
										contents.setIsManuallyAdd("2");
										contents.setCreateTime(date);
										contents.setCreateUser("金域");
										contentService.save(contents);
										}
									}
									
								} catch (JSONException e1) {
									logger.info(e1);
								} catch (ParseException e) {
									logger.info(e);
								} catch (Exception e) {
									logger.info(e);
								}
							}
							
							logger.info("批次号【"+events.getBatchNo()+"】，场次【"+events.getEventsNo()+"】，团单号【"+events.getGroupOrderNo()+"】，总人数【"+testArr.length()
										+"】，本次执行下载的人数【"+updateNum+"】，本次执行出现错误的条数【"+errorNum+"】");
							
							if(!pdfContentList.isEmpty()){
								//pdfContentService.save(pdfContentList);
							}
							
						/*	if(saveCustomerList!=null&&saveCustomerList.size()>0){
								total = customerService.saveList(saveCustomerList);
							}*/
							
							resultJson.put("total", ""+total);
							resultJson.put("msg", "保存会员："+total+" 人， 修改会员： "+updateNum+" 人。");
							
							mailContent.append("远盟基因：\n");
							mailContent.append("  已收到金域客户信息，如下：\n");
							mailContent.append("场次号: "+events.getEventsNo()+" , \n");
							mailContent.append("场次时间: "+events.getEventDate()+" , \n");
							mailContent.append("采样时间: "+events.getEventDate()+" , \n");
							mailContent.append("销售负责人: "+events.getCreateUserName()+" , \n");
							mailContent.append("本次保存的会员数量: "+total+"  , 修改的会员数量：  "+updateNum+" \n");
							
							resultJson.put("mailContent", mailContent.toString());
							resultJson.put("code", ""+200);
						}else{
							
						}
					}
					
				}
			}
		}
		return resultJson.toString();
	}

	
	public static void main(String[] args) throws ParseException {
		Date rightNow = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		Date prevDate = sdf.parse("2006-01-02 15:04:05 -0700");
		//根据生日计算日期
		int age = calculateAge(rightNow, prevDate);
		System.out.println("age: "+age);
	}
	
	private static int calculateAge(Date rightNow, Date birthday){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String nowStr = sdf.format(rightNow);
		String birthStr = sdf.format(birthday);
		return Integer.valueOf(nowStr)-Integer.valueOf(birthStr);
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
