/**
 * @author DengYouming
 * @since 2016-6-23 下午3:35:24
 */
package org.hpin.webservice.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.DateUtil;
import org.hpin.common.util.DateUtils;
import org.hpin.common.widget.pagination.Page;
import org.hpin.webservice.bean.*;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.dao.*;
import org.hpin.webservice.mail.MailEntity;
import org.hpin.webservice.mail.MailUtil;
import org.hpin.webservice.service.hk.ErpPreCustomerService;
import org.hpin.webservice.util.Dom4jDealUtil;
import org.hpin.webservice.util.ExcelCustomerUtil;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.ReturnStringUtils;
import org.hpin.webservice.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author DengYouming
 * @since 2016-6-23 下午3:35:24
 */
@Service(value = "org.hpin.webservice.service.GeneCustomerService")
@Transactional
public class GeneCustomerService extends BaseService {

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
	
	@Autowired
	private PreSalesManDao preSalesManDao;

	/**
	 * 根据xml中的参数查询预留营销员信息;
	 * create by henry.xu 2017年2月8日
	 * @param xml
	 * @return
	 */
	public String findSalesManInfoByParams(String xml) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		String result = "";
		PreSalesMan preSalesMan = null;
		String salesManName = ""; //营销员姓名
		String salesManNo = ""; //营销员工号
		String salesChannel = ""; //渠道
		
		log.info("保险公司营销员验证xml数据: " + xml);
		
		if(StringUtils.isNotEmpty(xml)) {
			//xml字符串解析为map对象;
			Map<String, String> resultMap = Dom4jDealUtil.readStringXmlOut(xml);
			
			if(resultMap != null) {
				salesManName = resultMap.get("salesManName"); //营销员姓名
				salesManNo = resultMap.get("salesManNo"); //营销员工号
				salesChannel = resultMap.get("salesChannel"); //渠道
				//数据查询
				preSalesMan = preSalesManDao.findSalesManInfoByParams(salesManName, salesManNo, salesChannel);
				
			}
		}
		
		//返回结果处理
		result = ReturnStringUtils.validateWCString(preSalesMan, salesChannel);
		
		log.info("保险公司营销员验证result数据: " + result);
		
		return result;
	};
	
	/**
	 * 根据属性查找对象
	 *
	 * @param props
	 * @return
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-6-23 下午3:38:15
	 */
	public List<ErpCustomer> findByProps(Map<String, Object> props) throws Exception {
		List<ErpCustomer> list = null;
		if (!CollectionUtils.isEmpty(props)) {
			Page page = new Page();
			StringBuffer hql = new StringBuffer();
			Object[] values = new Object[props.size()];
			hql.append(" from Customer where 1=1 ");
			int num = 0;
			if (props.get(ErpCustomer.F_PHONE) != null) {
				hql.append(" and phone =? ");
				values[num] = props.get(ErpCustomer.F_PHONE);
				num++;
			}
			if (props.get(ErpCustomer.F_CODE) != null) {
				hql.append(" and code =? ");
				values[num] = props.get(ErpCustomer.F_CODE);
				num++;
			}
			if (props.get(ErpCustomer.F_NAME) != null) {
				hql.append(" and name =? ");
				values[num] = props.get(ErpCustomer.F_NAME);
				num++;
			}
			if (props.get(ErpCustomer.F_IDNO) != null) {
				hql.append(" and idno =? ");
				values[num] = props.get(ErpCustomer.F_IDNO);
				num++;
			}

			list = (List<ErpCustomer>) erpcustomerDao.findByHql(page, hql.toString(), values);
		}
		return list;
	}

	/**
	 * @param xml      xml字符串
	 * @param parent   查找属性所在的一级根目录
	 * @param destAttr 属性名
	 * @return Map
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-6-23 下午5:27:11
	 */
	public Map<String, String> fetchXmlValue(String xml, String parent, String[] destAttr) throws Exception {
		Map<String, String> result = null;
		if (null != xml && xml.trim().length() > 0) {
			result = new HashMap<String, String>();
			if (!StringUtils.containsIgnoreCase(xml, "root")) {
				if (xml.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
					xml = xml.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "<root>") + "</root>";
				} else {
					xml = "<root>" + xml + "</root>";
				}
			}
			System.out.println("new xml: " + xml);
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element eParent = root.element(parent);
			for (int i = 0; i < destAttr.length; i++) {
				Element eDest = eParent.element(destAttr[i]);
				System.out.println(destAttr + " : " + eDest.getStringValue().trim());
				result.put(destAttr[i], eDest.getStringValue().trim());
			}
		}
		return result;
	}

	/**
	 * 批量保存
	 *
	 * @param list
	 * @return Integer
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-7-29 上午10:01:06
	 */
	public Integer saveList(List<ErpCustomer> list) throws Exception {
		Integer count = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				erpcustomerDao.saveOrUpdate(list.get(i));
				count++;
			}
		}
		return count;
	}

	/**
	 * 根据条件是否精确查询
	 *
	 * @param params  查询条件
	 * @param isExact true: 精确， false:模糊
	 * @return List<ErpCustomer>
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-18 下午1:29:13
	 */
	public List<ErpCustomer> listCustomerByProps(Map<String, String> params, boolean isExact) throws Exception {
		List<ErpCustomer> list = null;
		if (!params.isEmpty()) {
			list = erpcustomerDao.listCustomerByProps(params, isExact);
		}
		return list;
	}

	/**
	 * 根据条件精确查询
	 *
	 * @param params 查询条件
	 * @return List<ErpCustomer>
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-18 下午1:30:23
	 */
	public List<ErpCustomer> listCustomerByProps(Map<String, String> params) throws Exception {
		return this.listCustomerByProps(params, true);
	}

	/**
	 * @param name
	 * @param code
	 * @return
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-30 下午8:47:24
	 */
	public List<ErpCustomer> findByProps(String name, String code) throws Exception {
		return erpcustomerDao.findByProps(name, code);
	}

	/**
	 * create by henry.xu 20161125
	 * 查询最大的maxcode做+1处理后返回,同时,保存; 写在一个方法里面,减少中间时间,尽可能避免并发可能;
	 *
	 * @param paramsObj shipProId: "项目ID";
	 *                  prefix: 前缀;
	 *                  ]
	 * @return
	 */
	public String findBarCodeMaxAndSave(Map<String, Object> paramsObj) throws Exception {
		
		String maxNumStr = "";

		String prefix = (String) paramsObj.get("prefix"); //不能为空;
		String sql = "select nvl(max(numj), 0) as maxCode from erp_bar_code_detail where is_deleted = 0 and BARCODE like '"+prefix+"%'";
		int maxNum = this.erpcustomerDao.getJdbcTemplate().queryForInt(sql);

		//字符拼接; eg: W000001
		maxNum += 1;
		maxNumStr = maxNum + "";

		while (maxNumStr.length() < 7) {
			maxNumStr = "0" + maxNumStr; //补零;
		}

		maxNumStr = prefix.toUpperCase() + maxNumStr;

		//保存条码
		//主表;
		String id = Tools.getUUID();
		String batchNo = new Date().getTime() + "";

		//通过prefix查询对应的项目线;
		String sqlsysdict = "select dictcode from sys_dicttype where parentdictid = '101010' and dictremark = '" + prefix + "' ";
		Map<String, Object> resultMap = this.erpcustomerDao.getJdbcTemplate().queryForMap(sqlsysdict);
		String projectLine = "";
		if (resultMap != null && resultMap.keySet().size() > 0) {
			projectLine = (String) resultMap.get("dictcode");
		}
		String shipProId = (String) paramsObj.get("shipProId");

		String insertSql1 = "insert into erp_bar_code_bat ("
				+ "ID, BATCH_NO,PROJECT_LINE,CUSTOMER_RELATIONSHIP_PRO_ID,"
				+ "CREATE_NUM, INSTORESTATE, REMARK, OTHER1, "
				+ "OTHER2, CREATE_TIME, CREATE_USER_ID,UPDATE_TIME,UPDATE_USER_ID,"
				+ "IS_DELETED) values ('"
				+ id + "', '" + batchNo + "', '" + projectLine + "', '" + shipProId + "', "
				+ 1 + ", '" + 1 + "', null, null, "
				+ "null, sysdate, null, null, "
				+ "null, 0)";
		this.erpcustomerDao.getJdbcTemplate().update(insertSql1);

		//detail表;
		String detailId = Tools.getUUID();

		String insertSql2 = "insert into ERP_BAR_CODE_DETAIL ("
				+ "ID, BAR_CODE_ID, BARCODE, NUMJ, IS_USED, "
				+ "CREATE_USER_ID,CREATE_TIME,UPDATE_USER_ID, UPDATE_TIME,"
				+ "IS_DELETED"
				+ ") values('"
				+ detailId + "', '" + id + "', '" + maxNumStr + "', " + maxNum + ", "
				+ "'0', null, sysdate, null, "
				+ "null, 0)";

		this.erpcustomerDao.getJdbcTemplate().update(insertSql2);
			
		return maxNumStr;

	}

	/**
	 * 保存相关场次客户信息;
	 * 1、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
	 * （条码生成表中的条码默认规则为7位，以”W”开头，后面为数字：000001、000002……..）
	 * 2、 解析XML报文中的客户信息，将解析出来的客户信息以及步骤一中自动生成的条码信息存储erp_customer表中。
	 * 3、 接口返回步骤一中生成的条形码
	 * create by henry.xu 20161125
	 *
	 * @param xml 接口返回xml数据;
	 * @return String 条形码;和结果;
	 */
	public String saveCustomerGenerCode(String xml) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		String inXml = "";
		boolean result = false;
		String message = "";
		String barCode = "";
		ErpCustomer customer = null;
		Map<String, String> customerMap = null;
		try {

			//>> 1. 处理xml
			inXml = dealStringXml(xml);
			customerMap = Dom4jDealUtil.readStringXmlOut(inXml);//dealXmlToMap(xml);

			if (null == customerMap || customerMap.isEmpty()) {
				log.info("数据没有传送过来! xml = " + xml);
				return "{\"result\":\"" + result + "\", \"barCode\":\"" + barCode + "\", \"message\":\"\"}";
			}
			
			/*
			 * 验证加入;场次号+证件号; 如果已存在则返回提示消息;
			 * modified by henry.xu 20161213;
			 */
			boolean mark = this.erpcustomerDao.findCustomerIsExits(customerMap.get("eventsNo"), customerMap.get(ErpCustomer.F_IDNO));
			if(mark) {
				result = false;
				return "{\"result\":\"" + result + "\", \"barCode\":\"" + barCode + "\", \"message\":\"该证件号信息已提交，请联系工作人员核实\"}";
			}

			//判定是否有相同人员;此处由于每次都会重新生成条形码,所以不会出现重复;
			customer = new ErpCustomer();

			//公司信息;
			customer.setBranchCompanyId(customerMap.get("companyId")); //支公司ID
			customer.setBranchCompany(customerMap.get("companyName")); //支公司名称
			customer.setOwnedCompanyId(customerMap.get("ownerCompanyId")); //总公司id
			customer.setOwnedCompany(customerMap.get("ownedCompanyName")); //总公司名称
			//家族病史重新组合
			customer.setFamilyHistory(customerMap.get("familyHistory")); //家族疾病史
			customer.setCustomerHistory(customerMap.get("customerHistory")); //既往病史

			customer.setGuardianName(customerMap.get("guardianName")); //监护人姓名
			customer.setGuardianPhone(customerMap.get("guardianPhone"));  //监护人电话
			customer.setNote(customerMap.get("note")); //备注
			customer.setSalesManNo(customerMap.get("salesManNo"));//销售编号;
			customer.setSalesMan(customerMap.get("salesMan")); //销售人
			customer.setSetmealName(customerMap.get("setmealName"));//套餐名
			customer.setName(customerMap.get(ErpCustomer.F_NAME)); //姓名
			customer.setAge(customerMap.get(ErpCustomer.F_AGE)); //年龄

			customer.setIdno(customerMap.get(ErpCustomer.F_IDNO)); //身份证

			customer.setSex(customerMap.get(ErpCustomer.F_SEX)); //性别
			customer.setPhone(customerMap.get(ErpCustomer.F_PHONE)); //手机
			customer.setHeight(customerMap.get("height"));
			customer.setWeight(customerMap.get("weight"));
			//默认数据
			customer.setIsDeleted(0);
			customer.setTestInstitution(customerMap.get("testCompany"));
			customer.setStatus("0");//默认未结算
			customer.setStatusYm(PropertiesUtils.getInt("status","statusYm.yhq"));// 新增客户状态为 样本已获取 add by Dayton 2016-12-21
			customer.setSamplingDate(new Date());//采样时间为当前

			//根据场次号获取场次信息
			Map<String, String> params = new HashMap<String, String>();
			params.put(ErpEvents.F_EVENTSNO, customerMap.get("eventsNo"));

			List<ErpEvents> eventsList = eventsDao.listEventsByProps(params);
			ErpEvents events = null;
			if (!CollectionUtils.isEmpty(eventsList)) {
				events = eventsList.get(0);
				//根据支公司名称获取 HL_CUSTOMER_RELATIONSHIP的中省，市信息
				String company = events.getBranchCompany();
				CustomerRelationShip ship = shipDao.findByCompanyName(company);
				if (ship != null) {
					customer.setProvice(ship.getProvince());
					customer.setCity(ship.getCity());
				}

				customer.setEventsNo(events.getEventsNo());
				customer.setBranchCompany(events.getBranchCompany());
				customer.setBranchCompanyId(events.getBranchCompanyId() == null ? "" : events.getBranchCompanyId());
				customer.setOwnedCompany(events.getOwnedCompany());
				customer.setOwnedCompanyId(events.getOwnedCompanyId() == null ? "" : events.getOwnedCompanyId());

				customer.setEventsTime(events.getEventDate() == null ? events.getCreateTime() : events.getEventDate());

				customer.setYmSalesman(events.getYmSalesman());
				customer.setCreateTime(new Date());
				customer.setCreateUserName(events.getCreateUserName());
				customer.setCreateUserId(events.getCreateUserId() == null ? "" : events.getCreateUserId());

				/*
				 * 2、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
				 * （条码生成表中的条码默认规则为7位，以”W”开头，后面为数字：000001、000002……..）
				 */
				Map<String, Object> paramsObj = new HashMap<String, Object>();
				
				paramsObj.put("prefix", "W");
				
				paramsObj.put("shipProId", events.getCustomerRelationShipProId());

				barCode = this.findBarCodeMaxAndSave(paramsObj);

				customer.setCode(barCode); //条码

				/*  业务追加;  */
				String idcard = customer.getIdno();
				String year = idcard.substring(6, 10);
				String month = idcard.substring(10, 12);
				String day = idcard.substring(12, 14);
				String birthday = year + "-" + month + "-" + day + " 00:00:00";
				String reportType = ""; //默认为空;
				String weight = customer.getWeight();
				String height = customer.getHeight();
				String companyId = customer.getBranchCompanyId();
				String companyName = customer.getBranchCompany();
				String ownerCompanyId = customer.getOwnedCompanyId();
				String ownerCompanyName = customer.getOwnedCompany();
				String returnFlag = "No";
				String userName = customer.getName();
				String code = customer.getCode();
				String sex = customer.getSex();
				//“returnFlag”：“Yes”—信息是否回传（Yes：通过支公司二维码扫描进入的客户需要晚上回推给远盟，No：通过场次二维码扫描进入的客户信息不需要晚上回推给远盟/通过知情同意书直接在无创检测设备）
				/*json拼接.*/
				String json = "{"
						+ "\"companyId\":\"" + companyId + "\""
						+ ",\"companyName\":\"" + companyName + "\""
						+ ",\"ownerCompanyId\":\"" + ownerCompanyId + "\""
						+ ",\"ownerCompanyName\":\"" + ownerCompanyName + "\""
						+ ",\"birthday\":\"" + birthday
						+ "\", \"reportType\":\"" + reportType
						+ "\", \"serviceCode\":\"" + code
						+ "\", \"sex\":\"" + sex
						+ "\", \"weight\":\"" + weight
						+ "\", \"userName\":\"" + userName
						+ "\", \"height\":\"" + height
						+ "\", \"returnFlag\":\"" + returnFlag + "\"}";
				
				String url = "http://www.zhikangkeji.com:8888/exam/yuanmeng/ymSaveUser.action"; //生产环境;
				message = getHbsHttp(url, json);
				this.save(customer);
				result = true;
				
				if(StringUtils.isEmpty(message)) {
					result = false;
				}

			} else {
				log.info("场次号查询场次为空!");
			}
		} catch (Exception e) {
			log.info("saveCustomerGenerCode数据处理异常!", e);
		}
		/*
		 * 3、成功的时候接口返回步骤一中生成的条形码
		 */
		return "{\"result\":\"" + result + "\", \"barCode\":\"" + barCode + "\", \"message\":\""+message+"\"}";
	}
	
	/**
	 * 保存支公司数据;
	 * create by henry.xu 2016年12月1日
	 *
	 * @param xml
	 * @return
	 */
	public String saveCustomerGenerCodeByCompany(String xml) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		boolean result = false;
		String url = ""; //访问路径
		String barCode = "";
		String message = "";
		ErpCustomerTempWuChuang customer = null;
		Map<String, String> customerMap = null;
		try {

			//>> 1. 处理xml
			String inXml = dealStringXml(xml);
			customerMap = Dom4jDealUtil.readStringXmlOut(inXml);//dealXmlToMap(xml);

			if (null == customerMap || customerMap.isEmpty()) {
				log.info("数据格式不对! xml = " + xml);
				return returnResult(result+"", barCode, message);
			}
			
			/*
			 * --验证加入;支公司id+证件号; 如果已存在则返回提示消息; modified by henry.xu 20161213;--
			 * 验证当前支公司ID+用户证件号码+项目类别  modified by henry.xu 20170109 补充微磁多了项目类别;
			 * 
			 */
			String projectType = customerMap.get("projectType");
			boolean mark = this.erpCustoemrTempWuChuangDao.findCustomerIsExits(customerMap.get("companyId"), customerMap.get(ErpCustomer.F_IDNO), projectType);
			if(mark) {
				result = false;
				message = "该证件号信息已提交，请联系工作人员核实";
				return returnResult(result+"", barCode, message);
			}

			//>> 2. map对象转换为customer对象;
			customer = dealCustomerWuchuang(customerMap);

			//code;
			/* 3、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
			 * （条码生成表中的条码默认规则为7位，以”W”/"C"开头，后面为数字：000001、000002……..）
			 */
			Map<String, Object> paramsObj = new HashMap<String, Object>();
			
			/*
			 * modified by henry.xu 20170329
			 * 加入年龄限制:
			 * 邀约接口话术：
			 * 微磁：该检测仅适用年龄12-80岁之间的人群，您输入年龄不在检测范围内，请重新输入！
			 * 生物电：该检测仅适用年龄6-90岁之间的人群，您输入年龄不在检测范围内，请重新输入！
			 */
			 
			String age = customer.getAge();
			
			if("PCT_005".equals(projectType)) {
				url = "http://www.jsrom.cn/index.php/Api/ymSaveUser"; //吉思朗
				//url = "http://139.129.193.80/index.php/Api/ymSaveUser";
				paramsObj.put("prefix", "C");
				
				if(NumberUtils.isNumber(age)) {
					int ageInt = Integer.parseInt(age);
					
					if(ageInt < 12 || ageInt > 80) {
						message = "该检测仅适用年龄12-80岁之间的人群，您输入年龄不在检测范围内，请重新输入！";
						return returnResult(result+"", "", message);
					}
				}
				
				
			} else {
				
				if(NumberUtils.isNumber(age)) {
					int ageInt = Integer.parseInt(age);
					
					if(ageInt < 6 || ageInt > 90) {
						message = "该检测仅适用年龄6-90岁之间的人群，您输入年龄不在检测范围内，请重新输入！";
						return returnResult(result+"", "", message);
					}
				}
				url = "http://www.zhikangkeji.com:8888/exam/yuanmeng/ymSaveUser.action"; //生产环境;
				paramsObj.put("prefix", "W");
			}
			paramsObj.put("shipProId", null);
			barCode = this.findBarCodeMaxAndSave(paramsObj);
			customer.setCode(barCode); //条码

			//>> 4. 信息推送给无创;
			String json = jsonStringDeal(customer, "Yes");
			message = getHbsHttp(url, json);
			
			message = "success";
			if(StringUtils.isEmpty(message)) {
				result = false;
				barCode = "";
				message = "网络服务异常，请点击“确定”后重新提交";
				return returnResult(result+"", barCode, message);
			} else if("数据不合法".equals(message)) {
				result = false;
				barCode = "";
				message = "网络服务异常，请点击“确定”后重新提交";
				return returnResult(result+"", barCode, message);
			}
			
			//>> 3. 保存值临时表;
			this.erpCustoemrTempWuChuangDao.saveObject(customer);
			result = true;
			
			
		} catch (Exception e) {
			result = false;
			barCode = "";
			message = "网络服务异常，请点击“确定”后重新提交";
			log.info("saveCustomerGenerCodeByCompany数据处理异常!", e);
		}

		// * 3、成功的时候接口返回步骤一中生成的条形码

		return returnResult(result+"", barCode, message);
	}

	/**
	 * 保存支公司数据;太平微磁使用该方法.暂时使用1个月;由于业务区别不影响之前的所以copy出来;
	 * create by henry.xu 2016年12月1日
	 *
	 * @param xml
	 * @return
	 */
	public String saveCustomerGenerCodeByCompanyTaiPing(String xml) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		boolean result = false;
		String url = ""; //访问路径
		String barCode = "";
		String message = "";
		ErpCustomerTempWuChuang customer = null;
		Map<String, String> customerMap = null;
		try {

			//>> 1. 处理xml
			String inXml = dealStringXml(xml);
			customerMap = Dom4jDealUtil.readStringXmlOut(inXml);//dealXmlToMap(xml);

			if (null == customerMap || customerMap.isEmpty()) {
				log.info("数据格式不对! xml = " + xml);
				return returnResult(result+"", barCode, message);
			}
			
			/*
			 * --验证加入;支公司id+证件号; 如果已存在则返回提示消息; modified by henry.xu 20161213;--
			 * 验证当前支公司ID+用户证件号码+项目类别  modified by henry.xu 20170109 补充微磁多了项目类别;
			 * 
			 */

			//>> 2. map对象转换为customer对象;
			customer = dealCustomerWuchuang(customerMap);

			//code;
			/* * 3、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
			 * （条码生成表中的条码默认规则为7位，以”W”/"C"开头，后面为数字：000001、000002……..）
			 */
			Map<String, Object> paramsObj = new HashMap<String, Object>();
			
			url = "http://www.jsrom.cn/index.php/Api/ymSaveUser"; //吉思朗
			paramsObj.put("prefix", "C");
			
			paramsObj.put("shipProId", null);
			barCode = this.findBarCodeMaxAndSave(paramsObj);
			customer.setCode(barCode); //条码

			//>> 4. 信息推送给无创;
			String json = jsonStringDeal(customer, "Yes");
			message = getHbsHttp(url, json);
			//>> 3. 保存值临时表;
			this.erpCustoemrTempWuChuangDao.saveObject(customer);
			result = true;
			
			if(StringUtils.isEmpty(message)) {
				result = false;
				barCode = "";
				message = "网络服务异常，请点击“确定”后重新提交";
				return returnResult(result+"", barCode, message);
			} else if("数据不合法".equals(message)) {
				result = false;
				barCode = "";
				message = "网络服务异常，请点击“确定”后重新提交";
				return returnResult(result+"", barCode, message);
			}
			
			
		} catch (Exception e) {
			result = false;
			barCode = "";
			message = "网络服务异常，请点击“确定”后重新提交";
			log.info("saveCustomerGenerCodeByCompany数据处理异常!", e);
		}

		// * 3、成功的时候接口返回步骤一中生成的条形码

		return returnResult(result+"", barCode, message);
	}
	
	/**
	 * 处理客户信息封装;
	 * create by henry.xu 2017年2月8日
	 * @param customerMap
	 * @return
	 */
	private ErpCustomerTempWuChuang dealCustomerWuchuang(Map<String, String> customerMap) {
		ErpCustomerTempWuChuang customer = new ErpCustomerTempWuChuang();

		//公司信息;
		customer.setBranchCompanyId(customerMap.get("companyId")); //支公司ID
		customer.setBranchCompany(customerMap.get("companyName")); //支公司名称
		customer.setOwnedCompanyId(customerMap.get("ownerCompanyId")); //总公司id
		customer.setOwnedCompany(customerMap.get("ownedCompanyName")); //总公司名称

		//家族病史重新组合
		customer.setFamilyHistory(customerMap.get("familyHistory")); //家族疾病史
		customer.setCustomerhistory(customerMap.get("customerHistory")); //既往病史

		customer.setGuardianname(customerMap.get("guardianName")); //监护人姓名
		customer.setGuardianphone(customerMap.get("guardianPhone"));  //监护人电话
		customer.setSalesManNo(customerMap.get("salesManNo"));//销售编号;
		customer.setSalesMan(customerMap.get("salesMan")); //销售人
		customer.setName(customerMap.get(ErpCustomer.F_NAME)); //姓名
		customer.setAge(customerMap.get(ErpCustomer.F_AGE)); //年龄
		String idno = customerMap.get(ErpCustomer.F_IDNO);
		customer.setIdno(idno); //身份证
		String birthDay = "";
		if(StringUtils.isNotEmpty(idno)) {
			birthDay = dealIdNoToBirthday(customer.getIdno());
		} else {
			birthDay = customerMap.get("birthDay") + " 00:00:00";
		}
		
		customer.setBirthday(birthDay);

		customer.setSex(customerMap.get(ErpCustomer.F_SEX)); //性别
		customer.setPhone(customerMap.get(ErpCustomer.F_PHONE)); //手机
		String projectType = customerMap.get("projectType");
		String testCompany = "";
		//判断当projectType不为指定值时,默认为PCT_004
		if("PCT_005".equals(projectType)) {
			projectType = "PCT_005";
			testCompany = "吉思朗";
		} else {
			projectType = "PCT_004";
			testCompany = "知康";
		}
		customer.setProjectType(projectType);
		customer.setHeight(customerMap.get("height"));
		customer.setWeight(customerMap.get("weight"));
		customer.setBloodType(customerMap.get("bloodType"));
		
		customer.setOpenId(customerMap.get("openId")); //微信标识
		customer.setSalesChannel(customerMap.get("salesChannel")); //渠道

		//默认数据
		customer.setTestInstitution(testCompany);
		customer.setIsMatch("-1"); //默认不匹配;
		customer.setCreateTime(new Date());
		
		return customer;
	}
	
	/**
	 * 在身份证中获取出生日期;
	 * create by henry.xu 2017年2月8日
	 * @param idcard
	 * @return
	 */
	private String dealIdNoToBirthday(String idcard) {
		String year = idcard.substring(6, 10);
		String month = idcard.substring(10, 12);
		String day = idcard.substring(12, 14);
		return year + "-" + month + "-" + day + " 00:00:00";
	}
	
	/**
	 * 拼接返回值;
	 * create by henry.xu 2017年2月8日
	 * @param result
	 * @param barCode
	 * @param message
	 * @return
	 */
	private String returnResult(String result, String barCode, String message) {
		return "{\"result\":\"" + result + "\", \"barCode\":\"" + barCode + "\", \"message\":\""+message+"\"}";
	}
	
	/**
	 * 处理要推送的json字符;
	 * create by henry.xu 2017年2月8日
	 * @param customer
	 * @param flag
	 * @return
	 */
	private String jsonStringDeal(ErpCustomerTempWuChuang customer, String flag) {

		String reportType = ""; //套餐名称(默认为空)
		String weight = customer.getWeight();
		String height = customer.getHeight();
		String companyId = customer.getBranchCompanyId();
		String companyName = customer.getBranchCompany();
		String ownerCompanyId = customer.getOwnedCompanyId();
		String ownerCompanyName = customer.getOwnedCompany();
		//“returnFlag”：“Yes”—信息是否回传（Yes：通过支公司二维码扫描进入的客户需要晚上回推给远盟，No：通过场次二维码扫描进入的客户信息不需要晚上回推给远盟/通过知情同意书直接在无创检测设备）
		String returnFlag = flag;
		String userName = customer.getName();
		String code = customer.getCode();
		String sex = customer.getSex();
		String salesMan = customer.getSalesMan();
		String bloodType = customer.getBloodType();
		
		String projectType = customer.getProjectType();
		
		String json = "";
		if("PCT_005".equals(projectType)) {
			//json拼接.
			json = "{"
					+ "\"companyId\":\"" + companyId + "\""
					+ ",\"companyName\":\"" + companyName + "\""
					+ ",\"ownerCompanyId\":\"" + ownerCompanyId + "\""
					+ ",\"ownerCompanyName\":\"" + ownerCompanyName + "\""
					+ ",\"birthday\":\"" + customer.getBirthday()
					+ "\", \"reportType\":\"" + reportType
					+ "\", \"serviceCode\":\"" + code
					+ "\", \"sex\":\"" + sex
					+ "\", \"bloodType\":\"" + bloodType
					+ "\", \"weight\":\"" + weight
					+ "\", \"userName\":\"" + userName
					+ "\", \"salesManName\":\"" + salesMan
					+ "\", \"height\":\"" + height
					+ "\", \"returnFlag\":\"" + returnFlag + "\"}";
			
		} else {
			//json拼接.
			json = "{"
					+ "\"companyId\":\"" + companyId + "\""
					+ ",\"companyName\":\"" + companyName + "\""
					+ ",\"ownerCompanyId\":\"" + ownerCompanyId + "\""
					+ ",\"ownerCompanyName\":\"" + ownerCompanyName + "\""
					+ ",\"birthday\":\"" + customer.getBirthday()
					+ "\", \"reportType\":\"" + reportType
					+ "\", \"serviceCode\":\"" + code
					+ "\", \"sex\":\"" + sex
					+ "\", \"weight\":\"" + weight
					+ "\", \"userName\":\"" + userName
					+ "\", \"height\":\"" + height
					+ "\", \"returnFlag\":\"" + returnFlag + "\"}";
		}
		
		
		return json;
	}

	/**
	 * 处理接口接收的字符串; create by henry.xu 2016年12月1日
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	private String dealStringXml(String xml) throws Exception {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		String inXml = "";

		if (StringUtils.isEmpty(xml)) {
			return null;
		}

		/* 1、 解析XML报文中的客户信息，将解析出来的客户信息以及步骤一中自动生成的条码信息存储erp_customer表中。*/
		inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
		log.info("手机端请求返回数据： " + inXml);

		return inXml;
	}

	/**
	 * 请求无创url传送数据;
	 * create by henry.xu 2016年12月20日
	 * @param url
	 * @param param
	 * @param code
	 * @param userName
	 * @return
	 */
	@SuppressWarnings({"resource", "deprecation"})
	private String getHbsHttp(String url, String json) throws Exception {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		String result = null; //当出现异常时返回结果;
		log.info("数据推送地址url: " + url);
		log.info("Base64加密前: " + json);
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//Base64加密
		String param = Base64.encodeBase64String(json.getBytes("utf-8"));
		log.info("Base64加密后: " + param);
		params.add(new BasicNameValuePair("userInfo", param));
		HttpEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(formEntity);

		HttpResponse response = client.execute(post);
		log.info("请求返回状态码statusCode:" + response.getStatusLine().getStatusCode());
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} else {
			result = "";
		}
		log.info("返回结果result: " + result);

		return result;
	}


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
				if(markFlag) {
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
		List<Map<String, Object>> listMaps = this.customerReceiveDao.findGroupByTime(); //Map支公司Id+检测日期+场次类型
		StringBuilder eventsSB = new StringBuilder();
		if (null != listMaps && listMaps.size() > 0) {

			List<File> files = new ArrayList<File>();
			ErpEvents events = null;
			String dataBacthNo = this.eventsDao.findEventsBatchNo(); //在循环开始获取最大WD批次号;避免事务问题;
			int i=0; 
			for (Map<String, Object> map : listMaps) {
				events = new ErpEvents();
				String branchCompanyId = (String) map.get("branchCompanyId");
				String examDate = (String) map.get("examDate");
				String eventsType = (String) map.get("eventsType");
				String eventsCount = null != map.get("eventsCount") ? map.get("eventsCount").toString() : "0";
				
				String batchNo = this.dealBatchNo(dataBacthNo, i++);

				Date date = new Date();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String eventDate = sf.format(date);
				String eventsNo = this.eventsDao.maxNo(eventDate);
				String data = null;
				data = this.getNo(eventsNo, date);
				log.info("receiveExaminedObject==>Events createNo : " + data);
				events.setEventsNo(data);

				eventsSB.append(data).append(", \n");

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
				Map<String, Object> mapQuery = this.shipDao.findMapByShipId(events.getBranchCompanyId());
				if (null != mapQuery) {
					events.setCustomerRelationShipProId((String) mapQuery.get("proId"));
					//远盟营销员：项目负责人
					events.setYmSalesman((String) mapQuery.get("projectOwner"));
				}

				//入门检测礼：业务稍后给出套餐名称（待定）
				events.setComboName("无创生物电套餐一");
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
				List<ErpCustomerReceive> customReceives = this.customerReceiveDao.findObjectsByConditions(branchCompanyId, examDate, eventsType);

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
						erpCustomer.setSetmealName("无创生物电套餐一");
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

			/*
			 * 邮件发送给指定人员;
			 */
			MailEntity mail = new MailEntity();

			mail.setAttachMents(files);
			mail.setHost("smtp.exmail.qq.com"); // 设置邮件服务器,qq
			mail.setSender("gene@healthlink.cn"); //发件人账号
			mail.setPassword("Yue123.com"); //发件人密码

			List<String> mailsAddr = new ArrayList<String>();
			mailsAddr.add("longyu@healthlink.cn");
			mailsAddr.add("yumingwang@healthlink.cn");
			mailsAddr.add("nond@healthlink.cn");
			mailsAddr.add("christywang@healthlink.cn");
			mailsAddr.add("caifengwang@healthlink.cn"); //add by henry.xu 20170407
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
			buffer.append("无创检测创建场次如下: \n");
			buffer.append(eventsSB);

			buffer.append("链接网址：http://gene.healthlink.cn\n");
			mail.setSubject(subject);// 邮件标题
			mail.setMessage(buffer.toString());

			MailUtil.send(mail);
			
			//ExcelCustomerUtil.deleteFile(files);

			flag = true;
			log.info("receiveExaminedObject==>处理完成!");
		} else {
			flag = false;
			log.info("Map支公司Id+检测日期+场次类型在erp_customer_receive表中没有对应数据");
		}

		return "{\"result\":\"" + flag + "\"}";
	}
	
	private void updateIsMatch(String isMatch, String id, String receiveId) throws Exception {
		if (StringUtils.isNotEmpty(id)) {
			this.erpCustoemrTempWuChuangDao.updateIsMatch(isMatch, id);

		}

		if (StringUtils.isNotEmpty(receiveId)) {
			this.customerReceiveDao.updateIsMatch(isMatch, receiveId);

		}
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
				if (hourInt >= 6 && hourInt < 15) {
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
	 * @param params 客户信息
	 * @return String
	 * @description 根据客户信息查询其所在的支公司项目信息
	 * @author YoumingDeng
	 * @since: 2016/12/6 14:05
	 */
	public String findProjectNoByProps(Map<String, String> params) throws Exception {
		String projectNo = null;
		List<CustomerRelationShipPro> shipProList = null;
		if (!params.isEmpty()) {
			List<ErpCustomer> customerList = erpcustomerDao.listCustomerByProps(params);
			if (!CollectionUtils.isEmpty(customerList)) {
				ErpCustomer customer = customerList.get(0);
				//获取ID
				String shipId = null;
				Map<String, String> queryMap = new HashMap<String, String>();
				queryMap.put(ErpEvents.F_EVENTSNO, customer.getEventsNo());
				queryMap.put(ErpEvents.F_ISDELETED, "0");
				List<ErpEvents> eventsList = eventsDao.listEventsByProps(queryMap);
				//根据场次获取ID
				if (!CollectionUtils.isEmpty(eventsList)) {
					shipId = eventsList.get(0).getCustomerRelationShipProId();
					//清除之前的数据
					queryMap.clear();
					queryMap.put(CustomerRelationShipPro.F_ID, shipId);
					queryMap.put(CustomerRelationShipPro.F_ISDELETED, "0");
					shipProList = shipProDao.listByProps(queryMap);
					if (!CollectionUtils.isEmpty(shipProList)) {
						projectNo = shipProList.get(0).getProjectCode();
					}
				}
			}
		}
		return projectNo;
	}

	/**
	 * 基因系统验证方案：
		1、	基因系统收到【输入报文】后，解析出场次号。
		2、	查询该场次对应的二维码状态是失效还是有效：
		a)	如果失效则直接返回0
		b)	如果未失效，则用当前系统时间与二维码的有效期字段进行比较：
		如果当前系统时间已经超过二维码的有效期（例如：二维码有效期是2016-11-01 9:00:00，系统当前时间为2016-11-02，
		注意此处比较只需要用年月日进行比较即可），则返回0；如果当前系统时间还未到二维码的有效期，则返回1.

	 * create by henry.xu 2016年12月5日
	 * @param xml
	 * @return
	 */
	public String validateQRCode(String xml) {
		Logger log = Logger.getLogger("pushCustomerGenerCode");
		String result = "{\"result\":\"0\", \"message\":\"该场次不存在。\"}";;
		try {
			String inXml = "";

			if(StringUtils.isEmpty(xml)) {
				return result;
			}

			/*
			 * 1、 解析XML报文中的客户信息，将解析出来的客户信息以及步骤一中自动生成的条码信息存储erp_customer表中。
			 */
			inXml = new String(xml.getBytes("utf-8"), Charset.forName("utf-8"));
			log.info("手机端请求返回数据： "+inXml);

			if(StringUtils.isEmpty(inXml)) {
				return result;
			}
			
			int eventsNoStart = inXml.indexOf("eventsNo");
			int eventsNoEnd = inXml.lastIndexOf("eventsNo");

			String  eventsNo = inXml.substring(eventsNoStart+9, eventsNoEnd-2);
			/*
			 * 如果未失效，则用当前系统时间与二维码的有效期字段进行比较：
				如果当前系统时间已经超过二维码的有效期（例如：二维码有效期是2016-11-01 9:00:00，系统当前时间为2016-11-02，
				注意此处比较只需要用年月日进行比较即可），则返回0；如果当前系统时间还未到二维码的有效期，则返回1.
			 */
			Map<String, Object> obj = this.eventsDao.findQRCodeMapByEventsNo(eventsNo);
			if(null != obj) {
				String expiryDate = (String) obj.get("expiryDate");
				String eventsDate = (String) obj.get("eventsDate"); //场次开始日期;

				if(StringUtils.isEmpty(expiryDate)) {
					return result;
				}
				
				Date dateEvents = DateUtil.convertStringToDate("yyyy-MM-dd", eventsDate);

				Date dateExpiry = DateUtil.convertStringToDate("yyyy-MM-dd", expiryDate);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String nowDate = df.format(new Date());
				Date nowDate_Date = DateUtil.convertStringToDate("yyyy-MM-dd", nowDate);
				
				if(nowDate_Date.before(dateEvents)) { //如果场次日期小于当前日期 则表示场次还没有开始;
					return "{\"result\":\"0\",\"message\":\"您好，您的高端客户答谢活动尚未开始。\"}";
				}

				if(dateExpiry.before(nowDate_Date)) { //dateExpiry小于dateNow
					//早
					return "{\"result\":\"0\",\"message\":\"此二维码已经失效\"}";
				} else { //相等或大于为false
					//晚
					return "{\"result\":\"1\",\"message\":\"\"}";
				}
			}


		} catch(Exception e) {
			log.error("二维码验证错误", e);
		}

		return result;
	}
	
	/**
	 * 批次号处理;
	 * create by henry.xu 2017年1月19日
	 * @param bacthNo
	 * @param num
	 * @return
	 */
	private String dealBatchNo(String bacthNo, int num) {
		String result = "";
		
		if(StringUtils.isNotEmpty(bacthNo)) {
			Integer numNo = Integer.valueOf(bacthNo)+1+num;
			result = "WD" + numNo;
			
		} else {
			result = "WD1";
			
		}
		
		return result;
	}

	/**
	 *  删除erp_custoemr_temp_wuchuang表中数据;
	 * create by henry.xu 2016年12月20日
	 * @param code
	 * @param userName
	 */
	private void deleteByCodeName(String code, String userName) {
		String sql = "delete from erp_customer_temp_wuchuang where CODE= '"+code+"' and NAME='"+userName+"' ";
		this.erpCustoemrTempWuChuangDao.getJdbcTemplate().update(sql);
	}

	/**
	 * @description 根据会员条码修改其报告状态
	 * @author YoumingDeng
	 * @since: 2016/12/16 2:32
	 */
	public void updateStatusYmByCode(Integer statusYm, String code){
		erpcustomerDao.updateStatusYmByCode(statusYm, code);

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
	

	/**
	 * @description 保存场次扫描二维码传过来的信息
	 * @param map 场次扫描二维码传过来的信息
	 * @return boolean
     * @author Dayton
	 * @since 2016/12/16 14:41
	 */
	public boolean saveFromEvent(Map<String,String> map) throws Exception{
		boolean flag = false;
		ErpCustomer obj;
		// 1.调用微信接口，获取生成的二维码信息
		// 2.把二维码数据生成图片，保存到本地，返回二维码图片地址
		// 3.把二维码的相关信息保存到数据库
		if(!CollectionUtils.isEmpty(map)) {
			//20170227新增需求 判断身份证是否是15位或者18位
			String idNo = map.get(ErpCustomer.F_IDNO);
//			if(!(StringUtils.isNotBlank(idNo)&&(idNo.trim().length()==15||idNo.trim().length()==18))){
//				return flag;
//			}
			//重新组合销售人员名称的格式
			String salesManNo = map.get("salesManNo");
			String salesMan = map.get("salesMan");
			//监护人相关信息
			String guardianName = map.get("guardianName");
			String guardianPhone = map.get("guardianPhone");
			//家族病史重新组合
			String customerHistory = map.get("customerHistory");
			String familyHistory = map.get("familyHistory");
			//套餐
			String setmealName = map.get("setmealName");

			//查找是否有重复人员
			Map<String, String> cParams = new HashMap<String, String>();
			cParams.put(ErpCustomer.F_NAME, map.get("name"));
			cParams.put(ErpCustomer.F_CODE, map.get("code"));
			List<ErpCustomer> customerList = this.listCustomerByProps(cParams);
			//判定是否有相同人员
			if (customerList != null && customerList.size() > 0) {
				obj = customerList.get(0);
			} else {
				obj = new ErpCustomer();
			}

			obj.setSalesMan(salesMan);
			obj.setSalesManNo(salesManNo);
			obj.setGuardianName(guardianName);
			obj.setGuardianPhone(guardianPhone);
			obj.setCustomerHistory(customerHistory);
			obj.setFamilyHistory(familyHistory);
			obj.setSetmealName(setmealName);//套餐名 modify by YoumingDeng 2016-11-09

			obj.setName(map.get(ErpCustomer.F_NAME));
			obj.setAge(map.get(ErpCustomer.F_AGE));
			obj.setCode(map.get(ErpCustomer.F_CODE));
			obj.setIdno(map.get(ErpCustomer.F_IDNO).trim());
			obj.setSex(map.get((ErpCustomer.F_SEX)));

			obj.setPhone(map.get(ErpCustomer.F_PHONE));

			//根据场次号获取场次信息
			Map<String, String> params = new HashMap<String, String>();
			params.put(ErpEvents.F_EVENTSNO, map.get("eventsNo"));
			List<ErpEvents> eventsList = eventsService.listEventsByProps(params);
			ErpEvents events;
			if (!CollectionUtils.isEmpty(eventsList)) {
				events = eventsList.get(0);
				//根据支公司名称获取 HL_CUSTOMER_RELATIONSHIP的中省，市信息
				String company = events.getBranchCompany();
				CustomerRelationShip ship = shipDao.findByCompanyName(company);
				if (ship != null) {
					obj.setProvice(ship.getProvince());
					obj.setCity(ship.getCity());
				}
				obj.setEventsNo(events.getEventsNo());
				obj.setBranchCompany(events.getBranchCompany());
				obj.setBranchCompanyId(events.getBranchCompanyId() == null ? "" : events.getBranchCompanyId());
				obj.setOwnedCompany(events.getOwnedCompany());
				obj.setOwnedCompanyId(events.getOwnedCompanyId() == null ? "" : events.getOwnedCompanyId());

				Date currDate = Calendar.getInstance().getTime();

				if (customerList != null && customerList.size() > 0) {
					obj.setUpdateTime(currDate);
					obj.setUpdateUserName(events.getCreateUserName());
					obj.setUpdateUserId(events.getCreateUserId());
					this.update(obj);
				} else {
					//默认数据
					obj.setIsDeleted(0);
					obj.setTestInstitution(PropertiesUtils.getString("foreign","detection.southern"));
					obj.setStatus(PropertiesUtils.getString("status","status.wjs"));//默认未结算
					obj.setSamplingDate(currDate);//采样时间为当前
					obj.setEventsTime(events.getEventDate() == null ? events.getCreateTime() : events.getEventDate());

					obj.setYmSalesman(events.getYmSalesman());
					obj.setCreateTime(currDate);
					obj.setCreateUserName("-1"); //modified by henry.xu 根据需求修改为系统处理都为默认-1;
					obj.setCreateUserId(events.getCreateUserId() == null ? "" : events.getCreateUserId());

					obj.setStatusYm(PropertiesUtils.getInt("status", "statusYm.yhq")); //add 2016-12-23 默认的客户状态
					this.save(obj);
				}
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 保存弘康的数据
	 * @param map
	 * @return Boolean
	 * @throws Exception
	 */
	public String saveFromHK(Map<String,String> map, ErpPreCustomer preCustomer) throws Exception{
		Logger log = Logger.getLogger("pushCustomerInfoHK");
		String msg = "保存失败";
		if(!CollectionUtils.isEmpty(map)){
		    ErpCustomer obj = this.convertHK(map);
		    log.info("要保存的Customer: "+obj.toString());
		    if(obj!=null) {
		    	Map<String,String> props = new HashMap<String, String>();
		    	props.put(ErpCustomer.F_NAME, obj.getName());
		    	props.put(ErpCustomer.F_CODE, obj.getCode());
		    	props.put(ErpCustomer.F_EVENTSNO, obj.getEventsNo());
				List<ErpCustomer> list = this.listCustomerByProps(props);
				log.info("list size: "+list.size());
				if(CollectionUtils.isEmpty(list)) {
					//保存年龄
					obj.setAge(preCustomer.getWereAge());
					obj.setStatusYm(PropertiesUtils.getInt("status","statusYm.cjz"));//样本采集中
					log.info("保存的Customer: " + obj.toString());
					//保存年龄
					erpcustomerDao.save(obj);
					log.info("保存成功！！！");
					log.info("客户表id: "+obj.getId()+", code: "+obj.getCode());
					preCustomer.setErpCustomerId(obj.getId());
					preCustomer.setCode(obj.getCode());
					//更新预备导入表
					log.info("更新预导入表...");
					log.info("preCustomer: "+preCustomer.toString());
					erpPreCustomerService.update(preCustomer);
					//erpPreCustomerService.updateByCustomer(preCustomer);
					log.info("更新成功！！！");
					msg = "保存成功";
				}else {
		    	    obj = list.get(0);
					log.info("客户表id: "+obj.getId()+", code: "+obj.getCode());
					preCustomer.setErpCustomerId(obj.getId());
					preCustomer.setCode(obj.getCode());
					//更新预备导入表
					log.info("更新预导入表...");
					log.info("preCustomer: "+preCustomer.toString());
					erpPreCustomerService.update(preCustomer);
					//erpPreCustomerService.updateByCustomer(preCustomer);
					log.info("更新成功！！！");
		    	    msg = "用户信息重复!已更新寄送地址信息！";
				}
			}
		}
		return msg;
	}
	
	
	/**
	 * 保存天津邮政客户数据
	 * @param map
	 * @return
	 * @throws Exception
	 * @author machuan
	 * @date  2017年1月18日
	 */
	public boolean saveFromTY(Map<String,String> map) throws Exception{
		boolean flag = false;
		ErpCustomer obj;
		if(!CollectionUtils.isEmpty(map)) {
			//重新组合销售人员名称的格式
//			String salesManNo = map.get("salesManNo");
//			String salesMan = map.get("salesMan");
			//监护人相关信息--edit by machuan
			String parentName = map.get("parentName");
			String relationship = map.get("relationship");
			//家族病史重新组合
			String customerHistory = map.get("customerHistory");
			String familyHistory = map.get("familyHistory");
			//套餐
			String mealName = map.get("mealName");

			//查找是否有重复人员
			Map<String, String> cParams = new HashMap<String, String>();
			cParams.put(ErpCustomer.F_NAME, map.get("name"));
			cParams.put(ErpCustomer.F_CODE, map.get("barCode"));
			List<ErpCustomer> customerList = this.listCustomerByProps(cParams);
			//判定是否有相同人员
			if (customerList != null && customerList.size() > 0) {
				obj = customerList.get(0);
			} else {
				obj = new ErpCustomer();
			}
			//TODO 营销员及工号字段 是否必须
			//营销员
//			obj.setSalesMan(salesMan);
//			//营销员工号
//			obj.setSalesManNo(salesManNo);
			//2017-01-18 machuan 监护人姓名，关系
			obj.setGuardianName(parentName);
			obj.setRelationship(relationship);
			
			obj.setCustomerHistory(customerHistory);
			obj.setFamilyHistory(familyHistory);
			obj.setSetmealName(mealName);//套餐名 modify by machuan

			obj.setName(map.get(ErpCustomer.F_NAME));
			obj.setAge(map.get(ErpCustomer.F_AGE));
			obj.setCode(map.get("barCode"));
			obj.setIdno(map.get("idNum"));
			obj.setSex(map.get((ErpCustomer.F_SEX)));

			obj.setPhone(map.get(ErpCustomer.F_PHONE));

			//根据场次号获取场次信息
			Map<String, String> params = new HashMap<String, String>();
			params.put(ErpEvents.F_EVENTSNO, map.get("eventNo"));
			List<ErpEvents> eventsList = eventsService.listEventsByProps(params);
			ErpEvents events;
			if (!CollectionUtils.isEmpty(eventsList)) {
				events = eventsList.get(0);
				//根据支公司名称获取 HL_CUSTOMER_RELATIONSHIP的中省，市信息
				String company = events.getBranchCompany();
				CustomerRelationShip ship = shipDao.findByCompanyName(company);
				if (ship != null) {
					obj.setProvice(ship.getProvince());
					obj.setCity(ship.getCity());
				}
				obj.setEventsNo(events.getEventsNo());
				obj.setBranchCompany(events.getBranchCompany());
				obj.setBranchCompanyId(events.getBranchCompanyId() == null ? "" : events.getBranchCompanyId());
				obj.setOwnedCompany(events.getOwnedCompany());
				obj.setOwnedCompanyId(events.getOwnedCompanyId() == null ? "" : events.getOwnedCompanyId());

				Date currDate = Calendar.getInstance().getTime();

				if (customerList != null && customerList.size() > 0) {
					obj.setUpdateTime(currDate);
					obj.setUpdateUserName(events.getCreateUserName());
					obj.setUpdateUserId(events.getCreateUserId());
					this.update(obj);
				} else {
					//默认数据
					obj.setIsDeleted(0);
					obj.setTestInstitution(PropertiesUtils.getString("foreign","detection.TY"));
					
					obj.setStatus(PropertiesUtils.getString("status","status.wjs"));//默认未结算
					obj.setSamplingDate(currDate);//采样时间为当前
					obj.setEventsTime(events.getEventDate() == null ? events.getCreateTime() : events.getEventDate());

					obj.setYmSalesman(events.getYmSalesman());
					obj.setCreateTime(currDate);
					//TODO  创建人  创建人ID
					obj.setCreateUserName(events.getCreateUserName());
					obj.setCreateUserId(events.getCreateUserId() == null ? "" : events.getCreateUserId());
					//TODO 沟通初始的客户状态 ybcjz 样本采集中
					obj.setStatusYm(PropertiesUtils.getInt("status", "statusYm.cjz")); 
					this.save(obj);
				}
				flag = true;
			}
		}
		return flag;
	}


	/**
	 * 弘康数据转换成ErpCustomer对象
	 * @param map
	 * @return ErpCustomer
	 * @throws Exception
	 */
	private ErpCustomer convertHK(Map<String,String> map) throws Exception {
		ErpCustomer obj = new ErpCustomer();

		obj.setName(Tools.getValTrim(map,"name"));
		obj.setEventsNo(Tools.getValTrim(map,"eventNo"));
		obj.setIdno(Tools.getValTrim(map,"idNum"));
		obj.setPhone(Tools.getValTrim(map,"phone"));
		obj.setCode(Tools.getValTrim(map,"barCode"));

		obj.setSetmealName(Tools.getValTrim(map,"mealName"));
		obj.setFamilyHistory(Tools.getValTrim(map,"familyHistory"));
		obj.setCustomerHistory(Tools.getValTrim(map,"customerHistory"));
		obj.setSex(Tools.getValTrim(map,"sex"));

		//obj.setAge(Tools.getValTrim(map,"age"));

		obj.setHeight(Tools.getValTrim(map,"height"));
		obj.setWeight(Tools.getValTrim(map,"weight"));

		obj.setDepartment(PropertiesUtils.getString("foreign","hk.department"));//默认值

		//根据场次号获取场次信息
		Map<String, String> params = new HashMap<String, String>();
		params.put(ErpEvents.F_EVENTSNO, Tools.getValTrim(map,"eventNo"));
		params.put(ErpEvents.F_ISDELETED, "0");
		List<ErpEvents> eventsList = eventsService.listEventsByProps(params);
		ErpEvents events;

		if (!CollectionUtils.isEmpty(eventsList)) {
			events = eventsList.get(0);
			//根据支公司名称获取 HL_CUSTOMER_RELATIONSHIP的中省，市信息
			String company = events.getBranchCompany();
			CustomerRelationShip ship = shipDao.findByCompanyName(company);
			if (ship != null) {
				obj.setProvice(ship.getProvince());
				obj.setCity(ship.getCity());
			}
			obj.setEventsNo(events.getEventsNo());
			obj.setBranchCompany(events.getBranchCompany());
			obj.setBranchCompanyId(events.getBranchCompanyId() == null ? "" : events.getBranchCompanyId());
			obj.setOwnedCompany(events.getOwnedCompany());
			obj.setOwnedCompanyId(events.getOwnedCompanyId() == null ? "" : events.getOwnedCompanyId());

			obj.setEventsTime(events.getEventDate() == null ? events.getCreateTime() : events.getEventDate());
			obj.setYmSalesman(events.getYmSalesman());
			obj.setCreateUserName(events.getCreateUserName());
			obj.setCreateUserId(events.getCreateUserId() == null ? "" : events.getCreateUserId());
		}

		Date currDate = Calendar.getInstance().getTime();

		//默认数据
		obj.setIsDeleted(0);
		obj.setTestInstitution(PropertiesUtils.getString("foreign","detection.HK"));
		obj.setStatus(PropertiesUtils.getString("status","status.wjs"));//默认未结算
		obj.setSamplingDate(currDate);//采样时间为当前
		obj.setCreateTime(currDate);

		obj.setStatusYm(PropertiesUtils.getInt("status", "statusYm.yhq")); //add 2016-12-23 默认的客户状态

		return obj;
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
	}


}
