package org.hpin.webservice.service.ty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.DateUtil;
import org.hpin.common.util.XmlUtils;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.hpin.webservice.bean.ErpConference;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.dao.ErpValidCodeDetailDao;
import org.hpin.webservice.dao.ty.ErpTYCustomerDao;
import org.hpin.webservice.util.Dom4jDealUtil;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


/**
 * 天津邮政业务处理service
 * 20170215--TOC业务service
 * @author machuan
 * @date 2017年1月17日
 */
@Service("org.hpin.webservice.service.ty.ErpTYCustomerService")
@Transactional
public class ErpTYCustomerService extends BaseService{
	
	@Autowired
	private ErpValidCodeDetailDao dao;
	
	@Autowired
	private ErpTYCustomerDao erpTYcustomerDao;
	
	private static final String TY = "TY";
	
	private static final String COMBONAME = "肿瘤基础0";
	
	Logger log = Logger.getLogger("getIDAuth");

    public static void main(String[] args) throws Exception {
       
    }
    /**
     * 天津邮政验证检测码
     * 1、从erp_valid_code_detail表中查询是否有该检测码 状态为已使用，未使用或者查询不存在
     * 2、生成场次 根据支公司ID  拿到支公司信息 和日期 生成场次号  如已有场次，直接返回当日场次号
     * 3、获取套餐 通过支公司ID 查询到项目相关的套餐名称，多个以+号隔开
     * @param xml
     * @return
     * @throws Exception
     * @author machuan
     * @date  2017年1月17日
     */
    public String getIDAuth(String xml) throws Exception{
        String respXml;
        String result = "0"; //默认为0 失败
        String message = "";
        //返回的数据
        String mealName = null;
        String eventNo = null;
        //XML转换
        Map<String,String> xmlMap= XmlUtils.fetchXmlValue(xml,"reqAuthInfo", new String[]{"idNum","branchCompanyId"});
        if (!CollectionUtils.isEmpty(xmlMap)) {
        	String idNum = Tools.getValTrim(xmlMap, "idNum");
            String branchCompanyId = Tools.getValTrim(xmlMap, "branchCompanyId");
            log.info("branchCompanyId: "+branchCompanyId+",idNum: "+idNum);
            //从erp_valid_code_detail表中查询是否有该检测码 状态为已使用，未使用或者查询不存在
            List<Object> list = dao.find(idNum);
            //如果返回的flag为true，表明验证成功
            if((Boolean) list.get(0)){
            	//返回结果设置为1
            	result = "1";
            	branchCompanyId = (String) list.get(2);
            	log.info("验证检测码带回的branchCompanyId："+branchCompanyId);
            	String projectId = (String) list.get(3);
            	log.info("验证检测码带回的projectId："+projectId);
            	// 生成场次 根据支公司ID  拿到支公司信息 和日期 生成场次号  如已有场次，直接返回当日场次号
            	//20170215 不生成场次  直接返回一个虚拟的场次号
            	eventNo = createOrFindEvent(TY,branchCompanyId,projectId);
            	//获取套餐 通过支公司ID 查询到项目相关的套餐名称，多个以+号隔开
            	mealName = dao.getMealNameByProId(projectId,TY);
            	log.info("验证检测码获取到的套餐名称mealName："+mealName);
            }
            message = (String) list.get(1);
            
        } else {
            log.info("从XML字符传中提取的数据为空, XML: "+xml);
        }
        respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>" + result + "</result>\n" +
                "<message>" + message + "</message>\n" +
                "<mealName>" + mealName + "</mealName>\n" +
                "<eventNo>" + eventNo + "</eventNo>\n" +
                "</respAuthInfo>";
        log.info("respXml: "+respXml);
        return respXml;
    }
    /**
	 * 生成场次 根据支公司ID  拿到支公司信息 和日期 生成场次号  如已有场次，直接返回当日场次号
	 * @param ty branchCompanyId
	 * @return
	 * @author machuan
	 * @date  2017年1月18日
	 */
	public String createOrFindEvent(String ty,String branchCompanyId,String projectId) {
		String eventsNo = ty+ DateUtil.getDateTime("yyyyMMdd", new Date());//场次号和批次号规则为TY+当日日期，如HK20161215。
		/*String sql = "select count(*) from erp_events e where e.events_no='"+eventsNo+"' and e.is_deleted='0'";
		if(this.dao.getJdbcTemplate().queryForInt(sql)<1){
			//如果数据库中不存在该场次  则生成场次
			createEvent(eventsNo,branchCompanyId,projectId);
		}*/
		return eventsNo;
	}
	
	public String createEvent(String eventsNo,String branchCompanyId,String projectId){
		ErpEvents events = new ErpEvents();
		log.info("createEvent场次号: " + eventsNo+",branchCompanyId : "+branchCompanyId+", projectId : "+projectId);
		CustomerRelationShip ship = new CustomerRelationShip();
		CustomerRelationShipPro shipPro = new CustomerRelationShipPro();
		ship = (CustomerRelationShip) this.dao.findById(CustomerRelationShip.class, branchCompanyId);
		shipPro =  this.dao.findShipProById(projectId);
		events.setEventsNo(eventsNo); //场次号;
		events.setEventDate(new Date()); //场次日期
		//支公司名称：支公司ID
		events.setBranchCompanyId(ship.getId());
		//场次地址：支公司名称
		events.setBranchCompany(ship.getBranchCommany());
		//总公司名称：总公司Id
		events.setOwnedCompanyId(ship.getOwnedCompany());

		events.setOwnedCompany(ship.getCustomerNameSimple());
		//总公司名称;ship中没有总公司名称
		//省份：当前支公司的省份
		events.setAddress(ship.getAddress());//地址;
		events.setProvice(ship.getProvince());
		//城市：当前支公司的城市
		events.setCity(ship.getCity());
		events.setLevel2("1010301"); //用户级别
		events.setIsDeleted(0);
		//项目ID
		events.setCustomerRelationShipProId(projectId);
		//远盟营销员：项目负责人
		events.setYmSalesman(shipPro.getProjectOwner());
		//入门检测礼：业务给出的套餐名称
		events.setComboName(COMBONAME);
		//批次号：
		events.setBatchNo(eventsNo);
		events.setCreateTime(new Date());
		events.setCreateUserName(shipPro.getProjectOwner());
		events.setCreateUserId(shipPro.getCreateUserId());
		this.dao.save(events);
		// 推送到会场管理;
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
		conference.setProUser(shipPro.getProjectOwner());
		conference.setIsDeleted(0);
		conference.setIsExpress(0);
		conference.setHeadcount(events.getHeadcount());
		conference.setCreateTime(new Date());
		conference.setCreateUserName("-1");
		conference.setCustomerRelationShipProId(events.getCustomerRelationShipProId()); 

		this.dao.save(conference);
		log.info("==>会场推送成功!");
		return null;
	}
	
    /**
     *  根据微服务提供的客户信息写入基因系统数据库
     * @param xml
     * @return
     * @throws Exception
     * @author machuan
     * @date  2017年1月18日
     */
    public String pushCustomerInfo(String xml) throws Exception{
        String respXml;
        Logger tyLog = Logger.getLogger("pushCustomerInfo");
        String result = "0";//默认为失败
        String message = "保存失败";
        //XML转换
        Map<String, String> xmlMap = Dom4jDealUtil.readStringXmlOut(xml);
//        Map<String,String> xmlMap= XmlUtils.fetchXmlValue(xml,"reqCustomer",
//                new String[]{"eventNo","name","idNum","phone","barCode","mealName","familyHistory","customerHistory"
//                            ,"sex","age","height","weight","createTime","parentName","relationship","authID"});
        if (!CollectionUtils.isEmpty(xmlMap)) {
        	//验证
        	Map<String, Object> map = saveFromCustomer(xmlMap);
            if((Boolean) map.get("flag")) {
            	result = "1";
            	//把该检测码的状态置为1--当authId不为空
            	if(StringUtils.isNotBlank(xmlMap.get("authID"))){
            		dao.editIsUsedStatus(xmlMap.get("authID"));
            	}
			}
            message = (String) map.get("message");
            log.info("message: "+message);
			log.info("result: " + result);
        }else {
        	tyLog.info("从XML字符传中提取的数据为空, XML: "+xml);
        }
        respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respCustomer>\n" +
                "<result>" + result + "</result>\n" +
                "<message>" + message + "</message>\n" +
                "</respCustomer>";
        tyLog.info("respXml: "+respXml);
        return respXml;
    }
    
    
    /**
	 * 保存天津邮政客户数据
	 * 20170215  edit by machuan  通用C端保存客户信息  只保存进入 预导入表
	 * @param map
	 * @return
	 * @throws Exception
	 * @author machuan
	 * @date  2017年1月18日
	 */
	public Map<String, Object> saveFromCustomer(Map<String,String> map) throws Exception{
		Logger tyLog = Logger.getLogger("pushCustomerInfo");
		Map<String, Object> reMap = new HashMap<String, Object>();
		boolean flag = false;
		try{
			String authID = map.get("authID");//检测码
			String eventNo = map.get("eventNo");//场次号
			String name = map.get("name");//姓名
			String idNum = map.get("idNum");//有效证件号码
			//20170227新增需求 判断身份证是否是15位或者18位
//			if(!(StringUtils.isNotBlank(idNum)&&(idNum.trim().length()==15||idNum.trim().length()==18))){
//				reMap.put("flag", flag);
//				reMap.put("message", "身份证号不是 15位或者18位！");
//				return reMap;
//			}
			String phone = map.get("phone");//手机号码
			String barCode = map.get("barCode");//条形码
			String mealName = map.get("mealName");//套餐
			//家族病史重新组合
			String customerHistory = map.get("customerHistory");
			String familyHistory = map.get("familyHistory");
			String sex = map.get(("sex"));//性别
			String age = map.get("age");//年龄
			String height =  map.get("height");//身高
			String weight =  map.get("weight");//体重
//			String createTime =  map.get("createTime");//创建时间
			//监护人相关信息--edit by machuan
			String parentName = map.get("parentName");
			String relationship = map.get("relationship");
			ErpPreCustomer preCustomer;
			boolean isUpdated = true;
			//是否是阳光
			boolean isYg = "YG".equals(authID);
			Map<String,Object> ygInfo = null;
			//判断检测码是否为空并且未使用
			if(StringUtils.isNotBlank(authID)&&erpTYcustomerDao.findAuthIDForUsed(authID)){
				//保存该数据到预导入客户信息表
				preCustomer = new ErpPreCustomer();
				preCustomer.setAuthId(authID);
				isUpdated = false;
			}else{
				//判断 姓名、身份证号是否为空，任意为空返回失败，并且提示“请填写姓名和身份证信息”。
				if(StringUtils.isBlank(name)||StringUtils.isBlank(idNum)){
					reMap.put("flag", flag);
					reMap.put("message", "请填写姓名和身份证信息！");
					return reMap;
				}
				//如果authID为空或者无法使用，则根据场次号，姓名，身份证判断在erp_pre_customer表中是否存在该客户
				List<ErpPreCustomer> list  = erpTYcustomerDao.getPreCustomerByParams(eventNo,name,idNum,barCode);
				if(list!=null&&list.size()>0){
					preCustomer = list.get(0);
				}else{
					if(isYg){
						//如果传过来的是阳光保险
						ygInfo = erpTYcustomerDao.getYGInFo(eventNo,parentName,relationship);
						if(ygInfo!=null){
							preCustomer = new ErpPreCustomer();
							isUpdated = false;
						}else{
							reMap.put("flag", flag);
							reMap.put("message", "会员信息错误，请核实后填入！");
							return reMap;
						}
					}else{
						reMap.put("flag", flag);
						reMap.put("message", "会员信息错误，请核实后填入！");
						return reMap;
					}
				}
			}
			//场次号
			preCustomer.setEventsNo(eventNo);
			//姓名
			preCustomer.setWereName(name);
			//有效证件号码
			preCustomer.setWereIdcard(idNum);
			//套餐
			preCustomer.setCheckCobmo(mealName);
			//性别
			preCustomer.setWereSex(sex);
			//年龄
			preCustomer.setWereAge(age);
			//监护人
			preCustomer.setGuardianName(parentName);
			//监护人关系
			preCustomer.setRelationship(relationship);
			//电话
			preCustomer.setWerePhone(phone);
			//条形码
			preCustomer.setCode(barCode);
			//被检测人身高
			preCustomer.setWereHeight(height);
			//体重
			preCustomer.setWereWeight(weight);
			//家族病史
			preCustomer.setFamilyHistory(familyHistory);
			//既往病史
			preCustomer.setCustomerHistory(customerHistory);
			//创建时间--即订单生成日期
			preCustomer.setOrderCreateDate(new Date());
			//TODO 客户ID
//			preCustomer.setErpCustomerId(obj.getId());
			//客户状态置为样本采集中
			preCustomer.setStatusYm(PropertiesUtils.getInt("status", "statusYm.cjz"));
			if(isUpdated){
				erpTYcustomerDao.update(preCustomer);
			}else{
				//支公司ID 项目ID  根据authID 查询出来 
				if(isYg){
					preCustomer.setCompanyId((String) ygInfo.get("customer_relationship_id"));
					//项目ID
					preCustomer.setShipPorId((String) ygInfo.get("id"));
				}else{
					Map<String, Object> codeMap = erpTYcustomerDao.findCompanyIdByAuthId(authID);
					preCustomer.setCompanyId((String) codeMap.get("branch_id"));
					//项目ID
					preCustomer.setShipPorId((String) codeMap.get("project_id"));
				}
				//创建时间
				preCustomer.setCreateTime(new Date());
				//创建人ID
				preCustomer.setCreateUserId("");
				//状态置为0
				preCustomer.setIsDeleted(0);
				this.save(preCustomer);	
			}
			flag = true;
			reMap.put("flag", flag);
			reMap.put("message", "保存成功！");
		}catch(Exception e){
			tyLog.error("保存该数据到预导入客户信息表失败：",e);
			reMap.put("flag", flag);
			reMap.put("message", "保存失败！");
		}
		
		return reMap;
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
			list = erpTYcustomerDao.listCustomerByProps(params, isExact);
		}
		return list;
	}
	
	/**
	 * 根据参数查询对应的场次信息
	 * @param params Map
	 * @return List 场次列表
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-4 下午12:36:24
	 */
	public List<ErpEvents> listEventsByProps(Map<String, String> params) throws Exception{
		List<ErpEvents> list = null;
		if(!params.isEmpty()){
			list = erpTYcustomerDao.listEventsByProps(params);
		}
		return list;
	}
	

	/**
	 * 根据	branchCompanyId projectType查询hl_customer_ship_code表  是否重复
	 * @param string
	 * @param string2
	 * @return
	 * @author machuan
	 * @date  2017年2月15日
	 */
	public boolean checkRepeat(String branchCompanyId, String projectType) {
		String sql = "select count(*) from hl_customer_ship_code c where c.company_id=? and c.project_type=?";
		return this.dao.getJdbcTemplate().queryForInt(sql, branchCompanyId,projectType)>0;
	}
}




































