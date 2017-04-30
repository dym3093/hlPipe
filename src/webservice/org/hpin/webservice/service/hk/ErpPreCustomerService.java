package org.hpin.webservice.service.hk;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.XmlUtils;
import org.hpin.webservice.bean.CustomerRelationShip;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpEvents;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.hpin.webservice.dao.CustomerRelationshipDao;
import org.hpin.webservice.dao.ErpPreCustomerDao;
import org.hpin.webservice.service.ErpEventsService;
import org.hpin.webservice.service.GeneCustomerService;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 弘康客户信息预存表的Service
 * Created by root on 16-12-29.
 */
@Service("org.hpin.webservice.service.hk.ErpPreCustomerService")
@Transactional
public class ErpPreCustomerService extends BaseService{

    @Autowired
    private ErpPreCustomerDao dao ;

    @Autowired
    private GeneCustomerService customerService;

    @Autowired
    private CustomerRelationshipDao  shipDao;

    @Autowired
    private ErpEventsService eventsService;

    /**
     * 根据条件查询
     * @param props 条件集
     * @param isExact true:等值(equal)查询, false:like查询
     * @return List
     * @throws Exception
     * @author Damian
     * @since 2016-12-29
     */
    public List<ErpPreCustomer> listByProps(Map<String,String> props, Boolean isExact) throws Exception{
        return dao.listByProps(props,isExact);
    }

    /**
     *
     * 根据条件查询(equal查询）
     * @param props 条件集
     * @return List
     * @throws Exception
     * @author Damian
     * @since 2016-12-29
     */
    public List<ErpPreCustomer> listByProps(Map<String,String> props) throws Exception{
        return dao.listByProps(props);
    }

    public List<ErpPreCustomer> find(String name, String phone, String idCard) throws Exception {
        return dao.find(name, phone, idCard);
    }

    /**
     * 弘康客户信息验证
     * 在客户预导入表中做以下查询工作。
     * 1. 查询成功。查找到客户的姓名，手机号，身份证号匹配成功，并且条形码为空的数据。
     * 2. 查询失败。包括客户信息匹配失败和客户已绑定（客户已有条形码）两种情况。
     * 情况一，被检人“姓名”、“手机号”和“身份证号”没有完全匹配的数据。
     * <message>您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。</message>
     * 情况二，客户信息匹配，但是已存在条形码。多条匹配数据时，无条形码为空的匹配数据。
     * <message>您好，您已成功绑定，请勿重复绑定。</message>
     * 3. 当客户信息，同时存在匹配成功和已绑定的情况，按照查询成功处理。
     * @param xml 弘康客户信息
     * @return String xml格式字符串
     * @throws Exception
     * @author Damian
     * @since 2016-12-28 17:31
     */
    public String getCustomerAuth(String xml) throws Exception{
        Logger log = Logger.getLogger("getCustomerAuth");
        String respXml;
        //默认： 情况一，被检人“姓名”、“手机号”和“身份证号”没有完全匹配的数据。
        String result = "0";//默认为失败
        String message = "您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。";
        //返回的数据
        String mealName = null;
        String eventNo = null;
        String reportReceiveName = null;
        String reportReceivePhone = null;
        String reportReceiveAddress = null;
        //XML转换
        Map<String,String> xmlMap= XmlUtils.fetchXmlValue(xml,"reqAuthInfo", new String[]{"name","phone","idNum"});
        if (!CollectionUtils.isEmpty(xmlMap)) {
            String name = Tools.getValTrim(xmlMap,"name");
            String phone = Tools.getValTrim(xmlMap, "phone");
            String idNum = Tools.getValTrim(xmlMap, "idNum");
            log.info("name: "+name+", phone: "+phone+", idNum: "+idNum);
            //客户预处理表
            List<ErpPreCustomer> preList = this.find(name, phone, idNum);
            log.info("size: "+preList.size());
            //预导入表中有客户信息，进行判定，没有信息走默认值
            // 1. 查询成功。查找到客户的姓名，手机号，身份证号匹配成功，并且条形码为空的数据。
            // 情况二，客户信息匹配，但是已存在条形码。多条匹配数据时，无条形码为空的匹配数据。
            if(!CollectionUtils.isEmpty(preList)){
                for (ErpPreCustomer obj : preList) {
                    log.info("preCustomer: "+obj.toString());
                    mealName = obj.getCheckCobmo();//检测套餐
                    eventNo = obj.getEventsNo();
                    reportReceiveName = obj.getReportReceiveName(); //报告接收人
                    reportReceivePhone = obj.getPhone();
                    reportReceiveAddress = obj.getReportSendAddr(); //报告接收地址
                    //条码为空
                    if(StringUtils.isEmpty(obj.getCode())) {
                        result = "1";
                        message = "查询成功";
                        break;
                    }else{
                        result = "0";
                        message = "您好，您已成功绑定，请勿重复绑定。";
                    }
                }
            }
        } else {
            log.info("从XML字符传中提取的数据为空, XML: "+xml);
        }
        respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>" + result + "</result>\n" +
                "<message>" + message + "</message>\n" +
                "<mealName>" + mealName + "</mealName>\n" +
                "<eventNo>" + eventNo + "</eventNo>\n" +
                "<reportReceiveName>" + reportReceiveName + "</reportReceiveName>\n" +
                "<reportReceivePhone>" + reportReceivePhone + "</reportReceivePhone>\n" +
                "<reportReceiveAddress>" + reportReceiveAddress + "</reportReceiveAddress>\n" +
                "</respAuthInfo>";
        log.info("respXml: "+respXml);
        return respXml;
    }

    /**
     * 根据微服务提供的客户信息写入基因系统数据库
     * @param xml
     * @return String
     * @throws Exception
     * @author Damian
     * @since 2016-12-29 15:31
     */
    public String pushCustomerInfoHK(String xml) throws Exception{
        String respXml;
        Logger log = Logger.getLogger("pushCustomerInfoHK");
        String result = "0";//默认为失败
        String message = "保存失败";
        //XML转换
        Map<String,String> xmlMap= XmlUtils.fetchXmlValue(xml,"reqCustomer",
                new String[]{"eventNo","name","idNum","phone","barCode","mealName","familyHistory","customerHistory"
                            ,"sex","age","height","weight","createTime","reportReceiveName","reportReceivePhone","reportReceiveAddress"});
        if (!CollectionUtils.isEmpty(xmlMap)) {
            //验证
            ErpPreCustomer preCustomer = this.validPreCustomer(xmlMap);
            log.info("validPreCustomer: "+preCustomer.toString());
            if(preCustomer!=null){
                //根据禅道需求173进行修改 add by Damian 2017-02-16 start
                preCustomer.setStatusYm(PropertiesUtils.getInt("status","statusYm.cjz"));
                preCustomer.setUpdateTime(Calendar.getInstance().getTime());
                log.info(preCustomer.toString());
                log.info("开始更新preCustomer...");
                int num = dao.updateEntity(preCustomer);
                log.info("更新preCustomer结束!!!");
                //保存到Erp_Customer表
//                message = this.saveFromHK(xmlMap, preCustomer);
                //根据禅道需求173进行修改 add by Damian 2017-02-16 end
                if (num==1) {
                    message = "保存成功";
                    result = "1";
                }
                log.info("message: "+message+", result: "+result);
//                if("保存成功".equals(message)){
//                    result = "1";
//                }
            }
        }else {
           log.info("从XML字符传中提取的数据为空, XML: "+xml);
        }
        respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respCustomer>\n" +
                "<result>" + result + "</result>\n" +
                "<message>" + message + "</message>\n" +
                "</respCustomer>";
        log.info("respXml: "+respXml);
        return respXml;
    }

    /**
     * 验证预导入表的信息
     * @param xmlMap
     * @return Boolean
     * @throws Exception
     */
    private ErpPreCustomer validPreCustomer(Map<String,String> xmlMap) throws Exception {
        ErpPreCustomer preCustomer = null;
        Logger log = Logger.getLogger("pushCustomerInfoHK");
        //客户预处理表
        List<ErpPreCustomer> preList = this.find(Tools.getValTrim(xmlMap,"name"),
                                                Tools.getValTrim(xmlMap,"phone"),
                                                Tools.getValTrim(xmlMap,"idNum") );
        log.info("preList size: "+preList.size());
        if(!CollectionUtils.isEmpty(preList)){
            //姓名和身份证号符合，并且条码为空
            for (int i=0; i<preList.size(); i++) {
                String code = preList.get(i).getCode();
                log.info("preCustomer["+i+"]: " + preList.get(i).toString());
                log.info("code: "+code);
                if (StringUtils.isEmpty(code)){
                    preCustomer = preList.get(i);

                    preCustomer.setEventsNo(Tools.getValTrim(xmlMap, "eventNo"));

                    preCustomer.setCode(Tools.getValTrim(xmlMap, "barCode"));
                    preCustomer.setCheckCobmo(Tools.getValTrim(xmlMap, "mealName"));
                    preCustomer.setFamilyHistory(Tools.getValTrim(xmlMap, "familyHistory"));
                    preCustomer.setCustomerHistory(Tools.getValTrim(xmlMap, "customerHistory"));

                    String sex = Tools.getValTrim(xmlMap, "sex");
                    if (StringUtils.isNotEmpty(sex)) {
                        preCustomer.setWereSex(sex);
                    }

                    String age = Tools.getValTrim(xmlMap, "age");
                    if (StringUtils.isNotEmpty(age)) {
                        preCustomer.setWereAge(age);
                    }

                    preCustomer.setWereHeight(Tools.getValTrim(xmlMap, "height"));
                    preCustomer.setWereWeight(Tools.getValTrim(xmlMap, "weight"));
                    Date currDate = Calendar.getInstance().getTime();
                    preCustomer.setUpdateTime(currDate);

                    preCustomer.setReportReceiveName(Tools.getValTrim(xmlMap, "reportReceiveName"));
                    preCustomer.setPhone(Tools.getValTrim(xmlMap, "reportReceivePhone"));
                    preCustomer.setReportSendAddr(Tools.getValTrim(xmlMap, "reportReceiveAddress"));

                    log.info("preCustomer: " + preCustomer.toString());
                }
            }
        }
        return preCustomer;
    }

    /**
     * 共通的验证接口，可用的业务有：华夏，易安，天津邮政
     * 查询预导入表进行验证
     * 验证逻辑如下：
     * 在客户预导入表中做以下查询工作。
     * 1. 查询成功。查找到客户的姓名，手机号，身份证号(弘康以外的业务需要支公司ID进行匹配)匹配成功，并且条形码为空的数据。
     * 2. 查询失败。包括客户信息匹配失败和客户已绑定（客户已有条形码）两种情况。
     * 情况一，被检人“姓名”、“手机号”和“身份证号”没有完全匹配的数据。
     * <message>您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。</message>
     * 情况二，客户信息匹配，但是已存在条形码。多条匹配数据时，无条形码为空的匹配数据。
     * <message>您好，您已成功绑定，请勿重复绑定。</message>
     * 3. 当客户信息，同时存在匹配成功和已绑定的情况，按照查询成功处理。
     * @param xml XML格式的字符串
     * @return String
     * @throws Exception
     */
    public String getCustAuth(String xml) throws Exception{
        Logger log = Logger.getLogger("getCustAuth");
        String respXml;
        //默认： 情况一，被检人“姓名”、“手机号”和“身份证号”没有完全匹配的数据。
        String result = "0";//默认为失败
        String message = "您好，未查到您的信息，请核对信息后再提交，谢谢!";
        //返回的数据
        String mealName = null;
        String eventNo = null;
        String reportReceiveName = null;
        String reportReceivePhone = null;
        String reportReceiveAddress = null;
        //XML转换
        Map<String,String> xmlMap;
        //查询条件
        String name;
        String phone;
        String idNum;
        String branchCompanyId;
        //查询结果
        List<ErpPreCustomer> preList = null;

        if(StringUtils.containsIgnoreCase(xml,"branchCompanyId")) {
            //华夏，易安，天津邮政
            xmlMap = XmlUtils.fetchXmlValue(xml, "reqAuthInfo", new String[]{"name", "phone", "idNum", "branchCompanyId"});
        }else{
            //弘康提取数据
            xmlMap = XmlUtils.fetchXmlValue(xml, "reqAuthInfo", new String[]{"name", "phone", "idNum"});
        }

        if(!CollectionUtils.isEmpty(xmlMap)){
            name = Tools.getValTrim(xmlMap,"name");
            phone = Tools.getValTrim(xmlMap, "phone");
            idNum = Tools.getValTrim(xmlMap, "idNum");
            branchCompanyId = Tools.getValTrim(xmlMap, "branchCompanyId");
            log.info("name: "+name+", phone: "+phone+", idNum: "+idNum+", branchCompanyId: "+branchCompanyId);
            //添加查询条件
            if (StringUtils.isNotEmpty(branchCompanyId)) {
                //TOC业务
                preList = this.findForToc(name, phone, idNum, branchCompanyId);
            } else {
                //弘康业务
                preList = this.findForHK(name, phone, idNum);
            }

        }
        if (!CollectionUtils.isEmpty(preList)) {
            log.info("查询到的预导入表数目 size: "+preList.size());
            //预导入表中有客户信息，进行判定，没有信息走默认值
            // 1. 查询成功。查找到客户的姓名，手机号，身份证号匹配成功，并且条形码为空的数据。
            // 情况二，客户信息匹配，但是已存在条形码。多条匹配数据时，无条形码为空的匹配数据。
            ErpPreCustomer preCustomer;
            for (int i=0; i<preList.size(); i++) {
                preCustomer = preList.get(i);
                log.info("preCustomer: "+preCustomer.toString());
                mealName = preCustomer.getCheckCobmo();//检测套餐
                eventNo = preCustomer.getEventsNo();
                reportReceiveName = preCustomer.getReportReceiveName(); //报告接收人
                reportReceivePhone = preCustomer.getPhone();
                reportReceiveAddress = preCustomer.getReportSendAddr(); //报告接收地址
                //条码为空
                if(StringUtils.isEmpty(preCustomer.getCode())) {
                    result = "1";
                    message = "查询成功";
                    break;
                }else{
                    result = "0";
                    message = "您好，您已成功绑定，请勿重复绑定。";
                }
            }
        } else {
            log.info("从XML字符传中提取的数据为空, XML: "+xml);
        }
        respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>" + result + "</result>\n" +
                "<message>" + message + "</message>\n" +
                "<mealName>" + mealName + "</mealName>\n" +
                "<eventNo>" + eventNo + "</eventNo>\n" +
                "<reportReceiveName>" + reportReceiveName + "</reportReceiveName>\n" +
                "<reportReceivePhone>" + reportReceivePhone + "</reportReceivePhone>\n" +
                "<reportReceiveAddress>" + reportReceiveAddress + "</reportReceiveAddress>\n" +
                "</respAuthInfo>";
        log.info("respXml: "+respXml);
        return respXml;
    }

    /**
     *
     * 根据条件查找预导入表信息(用于 弘康 业务)
     * @param name 姓名
     * @param phone 电话
     * @param idCard 身份证号
     * @return List
     * @throws Exception
     */
    public List<ErpPreCustomer> findForHK(String name, String phone, String idCard) throws Exception{
        return dao.find(name, phone, idCard);
    }

    /**
     * 根据条件查找预导入表信息(用于 华夏，易安，天津邮政 业务)
     * @param name 姓名
     * @param phone 电话
     * @param idCard 身份证号
     * @param branchCompanyId 支公司ID
     * @return List
     * @throws Exception
     */
    public List<ErpPreCustomer> findForToc(String name, String phone, String idCard, String branchCompanyId) throws Exception{
        return dao.findForToc(name, phone, idCard, branchCompanyId);
    }

    /**
     * 根据外部业务传送的套餐名称查询对应的远盟套餐
     * @param foreignCombo 套餐名称
     * @return 远盟套餐
     * @throws Exception
     */
    public String findYmCombo(String foreignCombo) throws Exception{
        return dao.findYmCombo(foreignCombo);
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

        String foreignCombo = Tools.getValTrim(map,"mealName");
        //根据传送过来的套餐查找对应的远盟套餐
        String ymCombo = this.findYmCombo(foreignCombo);
        if(StringUtils.isNotEmpty(ymCombo)) {
            obj.setSetmealName(ymCombo);
        }
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
                List<ErpCustomer> list = customerService.listCustomerByProps(props);
                log.info("list size: "+list.size());
                if(CollectionUtils.isEmpty(list)) {
                    //保存年龄
                    obj.setAge(preCustomer.getWereAge());
                    obj.setStatusYm(PropertiesUtils.getInt("status","statusYm.cjz"));//样本采集中
                    log.info("保存的Customer: " + obj.toString());
                    //保存年龄
                    customerService.save(obj);
                    log.info("保存成功！！！");
                    log.info("客户表id: "+obj.getId()+", code: "+obj.getCode());
                    preCustomer.setErpCustomerId(obj.getId());
                    preCustomer.setCode(obj.getCode());
                    //更新预备导入表
                    log.info("更新预导入表...");
                    log.info("preCustomer: "+preCustomer.toString());
                    this.update(preCustomer);
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
                    this.update(preCustomer);
                    //erpPreCustomerService.updateByCustomer(preCustomer);
                    log.info("更新成功！！！");
                    msg = "用户信息重复!已更新寄送地址信息！";
                }
            }
        }
        return msg;
    }

}




































