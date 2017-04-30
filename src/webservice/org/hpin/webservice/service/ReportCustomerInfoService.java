package org.hpin.webservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.DateUtils;
import org.hpin.webservice.bean.ReportCustomerInfo;
import org.hpin.webservice.dao.ReportCustomerInfoDao;
import org.hpin.webservice.util.Dom4jDealUtil;
import org.hpin.webservice.util.ReturnStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="org.hpin.webservice.service.ReportCustomerInfoService")
@Transactional
public class ReportCustomerInfoService extends BaseService {

	private static Logger log = Logger.getLogger("pushCustomerGenerCode"); //日志;

	@Autowired
	private ReportCustomerInfoDao reportCustomerInfoDao;

	@SuppressWarnings("unchecked")
	public String saveReportCustomerInfo(String xml) {
		log.info("客户端接收数据-格式XML: " + xml);
		String result = "";
		String flag = "1"; //0：失败  1：成功
		String message = "";

		//空判断
		try {
			if(StringUtils.isNotEmpty(xml)) {
				Map<String, String> mapValidateInfo = new HashMap<String, String>();
				Map<String, String> mapCustomerList = new HashMap<String, String>();
				mapValidateInfo.put("level", "2");
				mapValidateInfo.put("params", "validateInfo");
				mapCustomerList.put("level", "3");
				mapCustomerList.put("params", "customerList");


				List<Map<String, String>> params = new ArrayList<Map<String, String>>();
				params.add(mapValidateInfo);
				params.add(mapCustomerList);
				Map<String, Object> objMap = Dom4jDealUtil.readStringXml2Out(xml, params);

				ReportCustomerInfo reCusInfo = null;
				if(objMap != null && !objMap.isEmpty()) {
					mapValidateInfo = (Map<String, String>) objMap.get("validateInfo");
					String accountName = mapValidateInfo.get("accountName");
					String password = mapValidateInfo.get("password");

					List<Map<String, String>> reCusInfos = (List<Map<String, String>>) objMap.get("customerList");
					Map<String, String> map =  null;
					Map<String, String> judge = null;
					/*
					 * 重复验证;
					 */
					for(int i=0; i< reCusInfos.size(); i++) {
						map =  reCusInfos.get(i);
						//重复验证,当出现重复的时候跳过保存;
						String reportId = (String)map.get("reportId");
						String reportNum = (String)map.get("reportNum");
						
						boolean isExist = this.reportCustomerInfoDao.findIsExcitByCondtions(reportId, reportNum);
						
						if(isExist) {
							flag = "0";
							message = "该批次数据中部分数据在远盟数据库已存在!";
							result = ReturnStringUtils.reportCustomerInfoResult(flag, message);
							log.info("服务端返回数据-格式XML: " + result);
							return result;
						}
						
						//在判断是否在该批次中重复;
						for(int j=i+1; j<reCusInfos.size(); j++) {
							judge = reCusInfos.get(j);
							//重复验证,当出现重复的时候跳过保存;
							String reportId2 = (String)judge.get("reportId");
							String reportNum2 = (String)judge.get("reportNum");
							if(reportId.equals(reportId2) && reportNum.equals(reportNum2)) {
								flag = "0";
								message = "该批次数据中存在重复的数据!";
								result = ReturnStringUtils.reportCustomerInfoResult(flag, message);
								log.info("服务端返回数据-格式XML: " + result);
								return result;
							}
						}
					}
					
					/*
					 * 验证后数据保存;
					 */
					Map<String, String> mapData = null;
					for (int i=0; i< reCusInfos.size(); i++) {
						mapData = reCusInfos.get(i);
						//重复验证,当出现重复的时候跳过保存;
						String code = (String)mapData.get("code");
						String name = (String)mapData.get("name");
						String combo = (String)mapData.get("setmealName");
						String reportLaunchDate = (String)mapData.get("reportDate");
						//判断数据库是否重复;
						
						reCusInfo = new ReportCustomerInfo();
						reCusInfo.setBatch((String)mapData.get("batchNo"));
						reCusInfo.setCode(code);
						reCusInfo.setCombo(combo);
						reCusInfo.setCreateTime(new Date());
						reCusInfo.setCreateUserId("-1");
						reCusInfo.setIsDeleted(0);
						reCusInfo.setName(name);
						reCusInfo.setPhone((String)mapData.get("phone"));
						String receivedDate = (String)mapData.get("receiveDate");
						Date receDate = null;
						if(StringUtils.isNotEmpty(receivedDate)) {
							receDate = DateUtils.convertDate(receivedDate, "yyyy-MM-dd");
						}
						reCusInfo.setReceivedDate(receDate);
						reCusInfo.setReportAccountName(accountName);
						reCusInfo.setReportAccountPass(password);
						Date relancDate = null;
						if(StringUtils.isNotEmpty(reportLaunchDate)) {
							relancDate = DateUtils.convertDate(reportLaunchDate, "yyyy-MM-dd");
						}
						reCusInfo.setReportLaunchDate(relancDate);
						reCusInfo.setSex((String)mapData.get("sex"));
						
						/*
						 * * update henry.xu 2017-03-14
						 * 添加: reportId 报告ID; reportNum 报告编号; isSuccess 是否成功上传; 默认为-1, 成功为1, 失败为0
						 */
						reCusInfo.setReportId((String)mapData.get("reportId"));
						reCusInfo.setReportNum((String)mapData.get("reportNum"));
						reCusInfo.setIsSuccess(-1);
						
						this.reportCustomerInfoDao.save(reCusInfo);
					}
					
				} else {
					flag = "0";
					message = "传入XML格式不符合约定!";
					log.info("客户端传入数据处理格式不正确.");
				}

			} else {
				flag = "0";
				message = "传入的XML为空!";
				log.info("客户端传入数据为空字符串.");
			}

		}catch(Exception e) {
			flag = "0";
			message = "传入XML格式不符合约定!";
			log.error("数据处理", e);
		}
		
		result = ReturnStringUtils.reportCustomerInfoResult(flag, message);
		log.info("服务端返回数据-格式XML: " + result);
		return result;
	}
}
