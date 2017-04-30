package org.hpin.webservice.service.wuchuangty;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.util.DateUtil;
import org.hpin.common.util.DateUtils;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.ErpConference;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpCustomerReceive;
import org.hpin.webservice.bean.ErpCustomerTempWuChuang;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.ErpQRCode;
import org.hpin.webservice.dao.CustomerReceivesDao;
import org.hpin.webservice.dao.CustomerRelationShipProDao;
import org.hpin.webservice.dao.CustomerRelationshipDao;
import org.hpin.webservice.dao.ErpConferenceDao;
import org.hpin.webservice.dao.ErpCustomerDao;
import org.hpin.webservice.dao.ErpCustomerTempWuChuangDao;
import org.hpin.webservice.dao.ErpEventsDao;
import org.hpin.webservice.dao.ErpPreCustomerDao;
import org.hpin.webservice.dao.ErpQRCodeDao;
import org.hpin.webservice.mail.MailEntity;
import org.hpin.webservice.mail.MailUtil;
import org.hpin.webservice.service.ErpEventsService;
import org.hpin.webservice.service.hk.ErpPreCustomerService;
import org.hpin.webservice.util.ExcelCustomerUtil;
import org.hpin.webservice.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DengYouming
 * @since 2016-6-23 下午3:35:24
 */
@Service(value = "org.hpin.webservice.service.wuchuangty.ErpCustomerTempWuChuangService")
@Transactional
public class ErpCustomerTempWuChuangService {
	@Autowired
	private ErpCustomerDao erpcustomerDao;

	@Autowired
	private ErpEventsDao eventsDao;

	@Autowired
	private CustomerRelationshipDao  shipDao;

	@Autowired
	private CustomerRelationShipProDao shipProDao;

	@Autowired
	private ErpCustomerTempWuChuangDao erpCustoemrTempWuChuangDao;

	@Autowired
	private CustomerReceivesDao customerReceiveDao;

	@Autowired
	private ErpConferenceDao erpConferenceDao;
	@Autowired
	private ErpQRCodeDao erpQRCodeDao;//

	@Autowired
    private ErpEventsService eventsService;

	@Autowired
	private ErpPreCustomerService erpPreCustomerService;

	@Autowired
	private ErpPreCustomerDao erpPreCustomerDao;
	
	/**
	 * 把保存抽出来,目的是一起事务问题.导致数据不能查询;
	 * create by henry.xu 2016年12月7日
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean saveCustomerReceive(String json) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		//>1. 解析数据为对象;

		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);

		Object mapObject = mapJson.get("examCustomerInfo");

		JSONArray listArray = JSONArray.fromObject(mapObject);
		List<ErpCustomerReceive> listsReceives = JSONArray.toList(listArray, ErpCustomerReceive.class);

		//>2. 保存到表中;
		if (listsReceives != null && listsReceives.size() > 0) {
			String eventsType = null;
			String batchNo = DateUtils.toStrHMS(new Date());
			for (ErpCustomerReceive receive : listsReceives) {
				
				//验证是否已存在;
				boolean markFlag = this.customerReceiveDao.findCountByCondition(receive.getServiceCode());
				//当已存在时,跳过执行保存;避免重复保存;
				if(markFlag){
					continue;
				}
				
				String companyId = receive.getCompanyId();
				receive.setOther(companyId);
				
				//去无创临时客户信息表查询支公司id; 没有就默认现有的id;
				ErpCustomerTempWuChuang wuchuang = this.erpCustoemrTempWuChuangDao.findByCodeAndName(receive.getServiceCode(), receive.getUserName());
				if(null != wuchuang) {
					receive.setCompanyId(wuchuang.getBranchCompanyId());
					receive.setProjectType(wuchuang.getProjectType());
				}
				
				//上午场：6：00-15：00/下午场：15：00-18：00/晚场：18：00-23：00
				String time = receive.getExamTime();
				log.info("获取数据对应的检测时间: " + time);
				eventsType = dealEventsType(time);
				log.info("处理后的数据: " + eventsType);

				receive.setEventsType(eventsType);

				receive.setIsMatch("-1"); //默认为-1;
				receive.setCreateTime(new Date());
				receive.setBatchNo(batchNo);

				this.customerReceiveDao.saveObject(receive);
			}
			return true;
		} 
		return false;
	}

	/**
	 * 知康每天定时23:59:59 将当天检测的客户信息打包推送给远盟，远盟提供接收接口，进行处理
	 * 1.数据处理
	 * 2.保存到erp_custmoer_receive
	 * 3.提取当前数据表中当前批次的信息分场次
	 * 4.提取当前新建立场次的客户信息
	 * create by henry.xu 2016年12月2日
	 *
	 * @return
	 */
	public String receiveExaminedObject() throws Exception {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		boolean flag = true;

		log.info("receiveExaminedObject==>customerReceive表保存成功,进入下一步场次处理!");

		//>3. 查询表中数据;进行处理分场次; 场次分配规则：支公司Id+检测日期+场次类型 分为同一个场次，系统自动建立一个场次信息
		//通过数据分组当天传入日期, 然后场次处理;
		List<Map<String, Object>> listMaps = this.customerReceiveDao.findTYGroupByTime(); //Map支公司Id+检测日期+场次类型
		StringBuilder eventsSB = new StringBuilder();
		String projectType = null;
		String comboName = null;
		if (null != listMaps && listMaps.size() > 0) {

			ErpEvents events = null;
			List<File> files = new ArrayList<File>();
			String dataBacthNo = this.eventsDao.findTYEventsBatchNo("JSLD"); //在循环开始获取最大WD批次号;避免事务问题;
			int i=0; 
			for (Map<String, Object> map : listMaps) {
				events = new ErpEvents();
				String branchCompanyId = (String) map.get("branchCompanyId");
				String examDate = (String) map.get("examDate");
				String eventsType = (String) map.get("eventsType");
				String eventsCount = null != map.get("eventsCount") ? map.get("eventsCount").toString() : "0";
				projectType = null != map.get("projectType") ? map.get("projectType").toString() : "";
				String batType = "";
				
				if("PCT_005".equals(projectType)) { //吉思朗(JSL)
					batType = "JSLD";
					comboName = "生命反馈系统检测一";
				}
				
				String batchNo = this.dealBatchNo(dataBacthNo, i++, batType);

				Date date = new Date();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String eventDate = sf.format(date);
				String eventsNo = this.eventsDao.maxNo(eventDate);
				String data = null;
				data = this.getNo(eventsNo, date);
				log.info("receiveExaminedObject==>Events createNo : " + data);
				events.setEventsNo(data);

				eventsSB.append("场次号("+data+")、");
				eventsSB.append("批次号("+batchNo+")、");
				eventsSB.append("客户数量"+eventsCount+"位。请确认数据。");

				//场次日期：检测日期
				//上午场下午场还是晚场根据场次类型确定
				examDate = examDate + " " + eventsType + ":00:00";
				events.setEventDate(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", examDate));
				//支公司名称：支公司ID
				events.setBranchCompanyId(branchCompanyId);
				//查询支公司;
				CustomerRelationShip ship = (CustomerRelationShip) this.shipDao.findById(CustomerRelationShip.class, branchCompanyId);
				if (null != ship) {
					events.setAddress(ship.getBranchCommany());
					//场次地址：支公司名称
					events.setBranchCompany(ship.getBranchCommany());
					//总公司名称：总公司Id
					events.setOwnedCompanyId(ship.getOwnedCompany());

					String OwnedCompany = this.shipDao.findOwnedCompanyName(ship.getOwnedCompany());
					events.setOwnedCompany(OwnedCompany);
					//总公司名称;ship中没有总公司名称
					//省份：当前支公司的省份
					events.setProvice(ship.getProvince());
					//城市：当前支公司的城市
					events.setCity(ship.getCity());
				}
				//预计人数：当前分为一个场次的人数
				int nowHeadCount = StringUtils.isNotEmpty(eventsCount) ? Integer.valueOf(eventsCount) : 0;
				events.setHeadcount(nowHeadCount);
				events.setNowHeadcount(nowHeadCount);
				events.setLevel2("1010307");
				events.setIsDeleted(0);

				//项目编码ID：根据当前支公司，获取此支公司下项目类型为【无创生物电检测】类型的项目编码以及项目名称、项目负责人
				Map<String, Object> mapQuery = this.shipDao.findTYMapByShipId(events.getBranchCompanyId(), projectType);
				if (null != mapQuery) {
					events.setCustomerRelationShipProId((String) mapQuery.get("proId"));
					//远盟营销员：项目负责人
					events.setYmSalesman((String) mapQuery.get("projectOwner"));
				}

				//入门检测礼： 生命反馈系统检测一(吉思朗)
				events.setComboName(comboName);
				//批次号：
				events.setBatchNo(batchNo);
				events.setCreateTime(new Date());
				events.setCreateUserName("-1");
				events.setEventsType("说明会"); //默认说明会;

				//>4. 保存场次信息;
				this.eventsDao.save(events);

				//>5. 推送到会场管理;
				ErpConference conference = new ErpConference();    //增加场次的同时增加到会议表中
				conference.setConferenceNo(events.getEventsNo());
				conference.setConferenceDate(events.getEventDate());
				conference.setProvice(events.getProvice());
				conference.setCity(events.getCity());
				conference.setBranchCompany(events.getBranchCompany());
				conference.setBranchCompanyId(events.getBranchCompanyId());
				conference.setOwnedCompany(events.getOwnedCompany());
				conference.setOwnedCompanyId(events.getOwnedCompanyId());
				conference.setAddress(events.getAddress());
				conference.setSettNumbers(0);
				conference.setProduceCost(Double.parseDouble("0"));
				conference.setConferenceType("1010902"); //默认说明会;
				conference.setProBelong(ship.getProBelong());
				conference.setProCode(ship.getProCode());
				conference.setProUser(ship.getProUser());
				conference.setIsDeleted(0);
				conference.setIsExpress(0);
				conference.setHeadcount(0);
				conference.setCreateTime(new Date());
				conference.setCreateUserName("-1");
				conference.setCustomerRelationShipProId(events.getCustomerRelationShipProId()); //modified by henry.xu 项目信息Id

				this.erpConferenceDao.saveErpConference(conference);
				log.info("receiveExaminedObject==>会场推送成功!");
				//推送到二维码管理;
				saveErpQRCode(events);
				log.info("receiveExaminedObject==>二维码管理推送成功!");

				/*
				 *>6. 提取当前新建立场次的客户信息，与erp_customer_temp_wuchuang表中数据进行核对，
				 * 核对成功直接插入erp_customer表（套餐为当前支公司配置的入门礼检测套餐）并将erp_customer_receive表中
				 * 当前客户的【是否匹配】字段赋值为【是】，核对不成功，则将当前这条客户信息的【是否匹配】字段赋值为【否】
						a)	核对规则1： 通过条形码+姓名进行匹配
						b)	核对规则2： 通过姓名+出生日期+性别进行匹配
				 */
				//查找该分组对应的场次对应的客户;支公司Id+检测日期+场次类型
				List<ErpCustomerReceive> customReceives = this.customerReceiveDao.findTYObjectsByConditions(branchCompanyId, examDate, eventsType, projectType);

				if(null == customReceives) {
					return "{\"result\":\"" + true + "\"}";
				}

				List<ErpCustomer> customersExcel = new ArrayList<ErpCustomer>();
				
				for (ErpCustomerReceive receive : customReceives) {
					//根据条件筛选;核对规则1： 通过条形码+姓名进行匹配
					ErpCustomerTempWuChuang customerW = this.erpCustoemrTempWuChuangDao.findByCodeAndName(receive.getServiceCode(), receive.getUserName());
					if (null == customerW) {
						//否则核对规则2： 通过姓名+出生日期+性别进行匹配
						customerW = this.erpCustoemrTempWuChuangDao.findByNameBirthdaySex(receive.getUserName(), receive.getBirthday(), receive.getSex());
					}

					if (null != customerW) {
						updateIsMatch("1", customerW.getId(), receive.getId());
						ErpCustomer erpCustomer = new ErpCustomer();
						erpCustomer.setAge(customerW.getAge());
						erpCustomer.setBranchCompany(ship.getBranchCommany());
						erpCustomer.setBranchCompanyId(ship.getId());
						//查询支公司;
						String city = null;
						String province = null;
						if (null != ship) {
							//省份：当前支公司的省份
							province = ship.getProvince();
							//城市：当前支公司的城市
							city = ship.getCity();
						}
						erpCustomer.setCity(city);
						erpCustomer.setCode(customerW.getCode());
						erpCustomer.setComparStatus("0");
						erpCustomer.setCreateDeptId(null);
						erpCustomer.setCreateTime(new Date());
						erpCustomer.setCreateUserId("-1");
						erpCustomer.setCreateUserName("-1");
						erpCustomer.setCustomerHistory(customerW.getCustomerhistory());
						erpCustomer.setDocumentType(null);
						erpCustomer.setEventsNo(events.getEventsNo());
						erpCustomer.setEventsTime(events.getEventDate());
						erpCustomer.setFailBtachId(null);
						erpCustomer.setFamilyHistory(customerW.getFamilyHistory());
						erpCustomer.setGuardianName(customerW.getGuardianname());
						erpCustomer.setGuardianPhone(customerW.getGuardianphone());
						erpCustomer.setHeight(customerW.getHeight());
						erpCustomer.setIdno(customerW.getIdno());
						erpCustomer.setIsDeleted(0);
						erpCustomer.setName(customerW.getName());
						erpCustomer.setNote(null);
						erpCustomer.setOwnedCompany(customerW.getOwnedCompany());
						erpCustomer.setOwnedCompanyId(customerW.getOwnedCompanyId());
						erpCustomer.setPdffilepath(null);
						erpCustomer.setPhone(customerW.getPhone());
						erpCustomer.setProvice(province);
						erpCustomer.setRelationship(null);
						erpCustomer.setRemark(null);
						erpCustomer.setDepartment(ship.getBranchCommany()); //modified by henry.xu 20161219 部门ID;
						erpCustomer.setOtherCompanyId(receive.getCompanyId()); //modified by henry.xu 20161219 无创erp_customer_temp_wuchuang表中支公司id;
						erpCustomer.setSalesMan(customerW.getSalesMan());
						erpCustomer.setSalesManNo(customerW.getSalesManNo());
						erpCustomer.setSampleType(null);
						erpCustomer.setSamplingDate(events.getEventDate());
						
						//如果receive中已存在套餐名称则使用传过来的套餐名称,否则使用默认的套餐名称;
						if(StringUtils.isNotEmpty(receive.getReportType())) {
							comboName = receive.getReportType(); 
						}
						
						erpCustomer.setSetmealName(comboName);
						erpCustomer.setSettlement_status(null);
						erpCustomer.setSex(customerW.getSex());
						erpCustomer.setStatus("0");
						
						erpCustomer.setTestInstitution(customerW.getTestInstitution()); 
						
						erpCustomer.setWeight(customerW.getWeight());
						erpCustomer.setStatusYm(PropertiesUtils.getInt("status", "statusYm.yhq"));

						erpCustomer.setYmSalesman(events.getYmSalesman());
						customersExcel.add(erpCustomer);
						this.erpcustomerDao.save(erpCustomer);
					} else {
						updateIsMatch("0", null, receive.getId());
					}

				}
				
				//附件处理;
				if(customersExcel != null && customersExcel.size() > 0) {
					File file = ExcelCustomerUtil.dealExcelCreate(customersExcel, events, shipDao);
					if(file != null) {
						files.add(file);
						
					}
					
				}

			}
			log.info("邮件发送开始----------------------------");
			/*
			 * 邮件发送给指定人员;
			 */
			MailEntity mail = new MailEntity();

			mail.setAttachMents(files);
			mail.setHost("smtp.exmail.qq.com"); // 设置邮件服务器,qq
			mail.setSender("gene@healthlink.cn"); //发件人账号
			mail.setPassword("Yue123.com"); //发件人密码

			List<String> mailsAddr = new ArrayList<String>();
			
			if("PCT_005".equals(projectType)) {
				mailsAddr.add("nond@healthlink.cn");
				mailsAddr.add("christywang@healthlink.cn");
				mailsAddr.add("longyu@healthlink.cn");
				mailsAddr.add("caifengwang@healthlink.cn"); // add by henry.xu 20170407
			}
			
			mail.setReceiver(mailsAddr); //多人邮件地址;
			mail.setUsername("gene@healthlink.cn");

			StringBuilder buffer = new StringBuilder();// 邮件内容
			String subject;//标题
			subject="无创检测创建场次";// 邮件标题

			//日期处理;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			Date dateNowTime = new Date();
			String dateStr = sdf.format(dateNowTime);

			buffer.append(dateStr).append("\n");
			buffer.append("您好!新增场次信息为: \n");
			
			buffer.append(eventsSB);

			buffer.append("链接网址：http://gene.healthlink.cn\n");
			mail.setSubject(subject);// 邮件标题
			mail.setMessage(buffer.toString());

			MailUtil.send(mail);
			log.info("邮件发送结束----------------------------");
			ExcelCustomerUtil.deleteFile(files);
			flag = true;
			log.info("receiveExaminedObject==>处理完成!");
		} else {
			flag = false;
			log.info("Map支公司Id+检测日期+场次类型在+项目类型编码 erp_customer_receive表中没有对应数据");
		}

		return "{\"result\":\"" + flag + "\"}";
	}
	
	/**
	 * 上午场：6：00-15：00/下午场：15：00-18：00/晚场：18：00-23：00
	 */
	private String dealEventsType(String checkTime) {
		String eventsType = null;
		if (StringUtils.isNotEmpty(checkTime)) {
			String arrs[] = checkTime.split(":");
			if (null != arrs && arrs.length > 0) {
				String hour = arrs[0]; //取得小时时间;
				int hourInt = Integer.valueOf(hour);//转换为int类型;
				//上午场：6：00-15：00
				if (hourInt >= 0 && hourInt < 15) {
					eventsType = "9";
				} else if (hourInt >= 15 && hourInt < 18) {
					//下午场：15：00-18：00
					eventsType = "14";
				} else if (hourInt >= 18 && hourInt <= 23) {
					//晚场：18：00-23：00
					eventsType = "20";
				}
			}
		}

		return eventsType;
	}
	
	/**
	 * 批次号处理;
	 * create by henry.xu 2017年1月19日
	 * @param bacthNo
	 * @param num
	 * @return
	 */
	private String dealBatchNo(String bacthNo, int num, String type) {
		String result = "";
		
		if(StringUtils.isNotEmpty(bacthNo)) {
			Integer numNo = Integer.valueOf(bacthNo)+1+num;
			result = type + numNo;
			
		} else {
			result = type+"1";
			
		}
		
		return result;
	}
	
	public String getNo(String num, Date date) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("HHmmssSSS");
		String lastNumber = num.substring(num.length() - 3);
		String newNumber = String.valueOf(Integer.parseInt(lastNumber) + 1);
		if (newNumber.length() == 1) {
			newNumber = "00" + newNumber;
		}
		if (newNumber.length() == 2) {
			newNumber = "0" + newNumber;
		}
		String temp = "HL" + sf.format(date) + sf2.format(new Date()) + newNumber;
		return temp;
	}
	
	/**
	 * 保存场次时，保存ErpQRCode
	 * @param erpEvents
	 * @throws Exception
	 * @author tangxing
	 * @date 2016-8-18下午3:17:05
	 */
	public void saveErpQRCode(ErpEvents erpEvents) {
		ErpQRCode erpQRCode = new ErpQRCode();
		erpQRCode.setEventsNo(erpEvents.getEventsNo());
		erpQRCode.setEventsDate(erpEvents.getEventDate());
		
		erpQRCode.setProvinceId(erpEvents.getProvice());
		String provinceName = findNameByCode(erpEvents.getProvice());	//省份转换
		erpQRCode.setProvinceName(provinceName);
		
		erpQRCode.setCityId(erpEvents.getCity());
		String cityName = findNameByCode(erpEvents.getCity());			//城市转换
		erpQRCode.setCityName(cityName);
		
		erpQRCode.setEventsId(erpEvents.getId());
		erpQRCode.setBatchNo(erpEvents.getBatchNo());
		erpQRCode.setBanchCompanyId(erpEvents.getBranchCompanyId());
		erpQRCode.setBanchCompanyName(erpEvents.getBranchCompany());
		erpQRCode.setOwnedCompanyId(erpEvents.getOwnedCompanyId());
		erpQRCode.setOwnedCompanyName(erpEvents.getOwnedCompany());
		erpQRCode.setCombo(erpEvents.getComboName());
		erpQRCode.setLevel(erpEvents.getLevel2());
		erpQRCode.setCreateUserName(erpEvents.getCreateUserName());
		erpQRCode.setCreateTime(new Date());
		erpQRCode.setIsDelete("0");
		erpQRCode.setQRCodeStatus("0");
		if(erpEvents.getHeadcount()!=null){
			erpQRCode.setExpectNum(String.valueOf(erpEvents.getHeadcount()));
		}else{
			erpQRCode.setExpectNum("0");
		};
		erpQRCodeDao.save(erpQRCode);
	};
	

	private void updateIsMatch(String isMatch, String id, String receiveId) throws Exception {
		if (StringUtils.isNotEmpty(id)) {
			this.erpCustoemrTempWuChuangDao.updateIsMatch(isMatch, id);

		}

		if (StringUtils.isNotEmpty(receiveId)) {
			this.customerReceiveDao.updateIsMatch(isMatch, receiveId);

		}
	}
	
	/**
	 * 根据编码查询;
	 * create by henry.xu 2016年12月7日
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code) {
		String sql = "select region_name regName from HL_REGION where id='"+code+"'";
		Map<String, Object> map = this.erpQRCodeDao.getJdbcTemplate().queryForMap(sql);
		return null != map ? (String)map.get("regName") : "";
	}
}
