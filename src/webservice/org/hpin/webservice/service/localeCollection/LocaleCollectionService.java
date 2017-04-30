package org.hpin.webservice.service.localeCollection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.DateUtil;
import org.hpin.common.util.DateUtils;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpConference;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.dao.CustomerRelationShipProDao;
import org.hpin.webservice.dao.CustomerRelationshipDao;
import org.hpin.webservice.dao.ErpConferenceDao;
import org.hpin.webservice.dao.ErpEventsDao;
import org.hpin.webservice.util.Dom4jDealUtil;
import org.hpin.webservice.util.ReturnStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("org.hpin.webservice.service.localeCollection.LocaleCollectionService")
@Transactional
/**
 *现场采集接口-自动建立场次Service 
 * @author Henry
 *
 */
public class LocaleCollectionService extends BaseService{
	private static Logger log = Logger.getLogger("pushCustomerGenerCode"); //日志;

	@Autowired
	private ErpEventsDao erpEventsDao; //场次Dao

	@Autowired
	private ErpEventsDao eventsDao;

	@Autowired
	private ErpConferenceDao erpConferenceDao;

	@Autowired
	private CustomerRelationshipDao  customerRelationshipDao; //支公司Dao

	@Autowired
	private CustomerRelationShipProDao customerRelationShipProDao; //项目dao

	/**
	 * 根据微服务提供的支公司信息，基因系统自动生成场次信息。
	 *
	 * <?xml version="1.0" encoding="utf-8"?>
		<customer>
		<branchCompanyId>支公司ID</branchCompanyId>
		<projectType>项目类型</projectType>
		</customer>
	 * @param xml
	 * @return
	 * @throws Exception 
	 */
	public String pushBranchInfoAutoEvents(String xml) {
		log.info("pushBranchInfoAutoEvents-->请求接收xml数据: "+ xml);
		//判断是否为空, 为空则返回null字符串;
		if(StringUtils.isEmpty(xml)) {
			return null;
		}

		/*
		 * 匹配规则为：支公司+项目信息+场次时间
		 * 支公司：通过xml中的branchCompanyId
		 * 项目信息：通过xml中的branchCompanyId+ projectType 到支公司项目关系表中获取id
		 * 场次时间：根据当前收到客户信息的系统时间，年月日取当前系统的年月日，时分秒通过如下规则确认：上午场：6：00-15：00/下午场：15：00-18：00/晚场：18：00-23：00
		 */
		Map<String, String> xmlMap = Dom4jDealUtil.readStringXmlOut(xml);
		//场次时间,默认为系统时间;

		String branchCompanyId = xmlMap.get("branchCompanyId");
		String projectType = xmlMap.get("projectType");
		String eventsDate = dealEventsType();

		//查询支公司和项目类型对应的项目;
		CustomerRelationShipPro shipPro = this.customerRelationShipProDao.findByCompanyIdAndProjectType(branchCompanyId, projectType);
		
		if(shipPro == null){
			return null;
		}
		
		String proId = shipPro.getId();
		
		//首先查询是否有匹配的场次; 
		ErpEvents events = this.erpEventsDao.findByConditions(branchCompanyId, proId, eventsDate);
		
		//查询套餐;
		List<Map<String, Object>> mapList = customerRelationShipProDao.findComboListByShipId(proId);
		
		if(mapList == null || mapList.isEmpty()) {
			return null;
		}
		
		try {
			//其次如果存在就不在生成场次;
			if(events == null) {
				//否则生成场次并返回;
				events = new ErpEvents();
				/*
				 * a：建场规则如下，
				 */

				Date date = new Date();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String eventDate = sf.format(date);
				String eventsNo = this.eventsDao.maxNo(eventDate);
				String data = ReturnStringUtils.getEventsNo(eventsNo, date, "HL");
				log.info("pushBranchInfoAutoEvents==>Events createNo : " + data);
				events.setEventsNo(data);

				//场次日期：检测日期
				//上午场下午场还是晚场根据场次类型确定
				events.setEventDate(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", eventsDate));
				//支公司名称：支公司ID
				events.setBranchCompanyId(branchCompanyId);
				//查询支公司;
				CustomerRelationShip ship = (CustomerRelationShip) this.customerRelationshipDao.findById(CustomerRelationShip.class, branchCompanyId);
				if (null != ship) {
					events.setAddress(ship.getBranchCommany());
					//场次地址：支公司名称
					events.setBranchCompany(ship.getBranchCommany());
					//总公司名称：总公司Id
					events.setOwnedCompanyId(ship.getOwnedCompany());

					String OwnedCompany = this.customerRelationshipDao.findOwnedCompanyName(ship.getOwnedCompany());
					events.setOwnedCompany(OwnedCompany);
					//总公司名称;ship中没有总公司名称
					//省份：当前支公司的省份
					events.setProvice(ship.getProvince());
					//城市：当前支公司的城市
					events.setCity(ship.getCity());
				}
				//预计人数：当前分为一个场次的人数
				events.setHeadcount(0);
				events.setNowHeadcount(0);
				events.setLevel2("1010301"); //低端
				events.setIsDeleted(0);

				//项目编码ID：根据当前支公司，获取此支公司下项目类型为【无创生物电检测】类型的项目编码以及项目名称、项目负责人
				events.setCustomerRelationShipProId(proId);
				//远盟营销员：项目负责人
				events.setYmSalesman(shipPro.getProjectOwner());

				//入门检测礼：业务稍后给出套餐名称（待定）
				events.setComboId((String)mapList.get(0).get("comboId"));
				events.setComboName((String)mapList.get(0).get("comboName"));
				//批次号：
				events.setBatchNo("AB");
				events.setCreateTime(new Date());
				events.setCreateUserName("-1");
				events.setEventsType("启动会"); //默认启动会;

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
				conference.setConferenceType("1010903"); //默认说明会;
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
				log.info("pushBranchInfoAutoEvents==>会场推送成功!");

			}
		} catch(Exception e) {
			log.error("场次创建异常: ", e);
		}

		StringBuilder xmlResult = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlResult.append("<eventQRCodeInfo>")
		.append("<eventNO>" + events.getEventsNo() + "</eventNO>")
		.append("<eventTime>" + DateUtils.DateToStr(events.getEventDate(), "yyyy-MM-dd HH:mm:ss") + "</eventTime>")
		.append("<ownedCompany>" + events.getOwnedCompany() + "</ownedCompany>")
		.append("<branchCompany>" + events.getBranchCompany() + "</branchCompany>")
		.append("<issueNo>"+events.getBatchNo()+"</issueNo>") //批次号
		.append("<keyword>" + events.getBranchCompany() + "</keyword>")//关键字 默认为支公司名称
		.append("<validHour>72</validHour>")//二维码(场次二维码)时间 默认为最大时间.
		.append("<companyComboList>");

		for (Map<String, Object> map : mapList) {
			xmlResult.append("<comboItem>");
			xmlResult.append("<comboId>"+map.get("comboId")+"</comboId>");
			xmlResult.append("<comboName>"+map.get("comboName")+"</comboName>");
			xmlResult.append("<comboDisName>"+map.get("comboDisName")+"</comboDisName>");
			xmlResult.append("<comboType>"+map.get("comboType")+"</comboType>");
			xmlResult.append("</comboItem>");
		}

		xmlResult.append("</companyComboList>")	    
		.append("</eventQRCodeInfo>");	    

		return xmlResult.toString();
	}

	/**
	 * 上午场：1：00-14：30/下午场：14：30-23：59
	 */
	private String dealEventsType() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String hourTime = formatter.format(new Date());
		String eventsType = null;

		String arrsDay[] = hourTime.split(" ");
		String arrs[] = arrsDay[1].split(":");
		if (null != arrs && arrs.length > 0) {
			String hour = arrs[0]; //取得小时时间;
			int hourInt = Integer.valueOf(hour);//转换为int类型;
			//上午场：6：00-15：00
			if (hourInt >= 1 && hourInt < 14) {
				eventsType = "9:00:00";
			} else if(hourInt <= 23 && hourInt >= 14) {
				eventsType = "20:00:00";
			}
		}

		return arrsDay[0] + " " + eventsType;
	}
}
