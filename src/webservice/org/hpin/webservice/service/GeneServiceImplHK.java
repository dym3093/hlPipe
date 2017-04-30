package org.hpin.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.hk.ErpPreCustomerService;
import org.hpin.webservice.util.Tools;
import org.hpin.webservice.websclientHK.HkMsgService;
import org.hpin.webservice.websclientHK.HkMsgServiceServiceLocator;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * 弘康业务接口实现类
 * Created by Damian on 16-12-28.
 */
@Service(value = "org.hpin.webservice.service.GeneServiceImplHK")
@WebService
public class GeneServiceImplHK implements GeneServiceHK{


    private ErpPreCustomerService erpPreCustomerService;

    static HkMsgServiceServiceLocator hkLocator = new HkMsgServiceServiceLocator();

    /**
     * 客户身份验证
     * @param xml
     * @return String
     * @auther Damian
     * @since 2016-12-28
     */
    @Override
    public String getCustomerAuth(String xml) {
        Logger log = Logger.getLogger("getCustomerAuth");
        String respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>0</result>\n" +
                "<message>您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。</message>\n" +
                "<mealName></mealName>\n" +
                "<eventNo></eventNo>\n" +
                "<reportReceiveName></reportReceiveName>\n" +
                "<reportReceivePhone></reportReceivePhone>\n" +
                "<reportReceiveAddress></reportReceiveAddress>\n" +
                "</respAuthInfo>";

        log.info( this.getClass().getSimpleName()+ " 接收到的xml "+xml);
        if(StringUtils.isNotEmpty(xml)){
            try {
                erpPreCustomerService = (ErpPreCustomerService) SpringTool.getBean(ErpPreCustomerService.class);
                log.info("开始查询...");
                respXml = erpPreCustomerService.getCustomerAuth(xml);
                log.info("查询结束!!!");
            } catch (Exception e) {
                log.info(e);
            }
        }
        log.info( this.getClass().getSimpleName()+ " 推送给微服务的xml: "+respXml);
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

        log.info(this.getClass().getSimpleName()+" 接收到的xml "+xml);
        String reqXml = Tools.decodeStr(xml);
        log.info(this.getClass().getSimpleName()+" 转换成UTF8格式后的xml字符串： "+reqXml);
        if(StringUtils.isNotEmpty(xml)){
            try {
                erpPreCustomerService = (ErpPreCustomerService) SpringTool.getBean(ErpPreCustomerService.class);
                log.info("开始保存...");
                //保存从为服务过来的数据
                respXml = erpPreCustomerService.pushCustomerInfoHK(xml);
                log.info("保存结束!!!");
            } catch (Exception e) {
                log.info(e);
            }
        }
        log.info(this.getClass().getSimpleName()+" 推送给微服务的xml: "+respXml);
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
        String result = "0";
        String message = "失败";
        String respXml = null;

        log.info(this.getClass().getSimpleName()+" 接收到的xml "+xml);
        String reqXml = Tools.decodeStr(xml);
        log.info("转换成UTF8格式后的xml字符串： "+reqXml);
        if(StringUtils.isNotEmpty(xml)){
            //result = PropertiesUtils.getString("data","pushCustomerStatus.result");
            //message = PropertiesUtils.getString("data","pushCustomerStatus.message");
            try {
                log.info("开始向微服务推送状态...");
                log.info("推送给微服务的xml: "+xml);
                //保存从为服务过来的数据
                HkMsgService hkMsgService = hkLocator.getHkMsgServicePort();
                respXml = hkMsgService.pushCustomerStatus(xml);
                log.info("向微服务推送结束...");
            } catch (Exception e) {
                log.info(e);
            }
        }else {
            respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<respStatus>\n" +
                    "<result>" + result + "</result>\n" +
                    "<message>" + message + "</message>\n" +
                    "</respStatus>";
        }
        log.info(this.getClass().getSimpleName()+"微服务返回的xml: "+respXml);
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
        String respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>0</result>\n" +
                "<message>您好，未查到您的信息，请核对信息后再提交，或联系弘康客服，谢谢。</message>\n" +
                "<mealName></mealName>\n" +
                "<eventNo></eventNo>\n" +
                "<reportReceiveName></reportReceiveName>\n" +
                "<reportReceivePhone></reportReceivePhone>\n" +
                "<reportReceiveAddress></reportReceiveAddress>\n" +
                "</respAuthInfo>";

        log.info(this.getClass().getSimpleName()+"接收到的xml "+xml);
        if(StringUtils.isNotEmpty(xml)){
            try {
                erpPreCustomerService = (ErpPreCustomerService) SpringTool.getBean(ErpPreCustomerService.class);
                log.info("GeneServiceImplHK 开始查询...");
                respXml = erpPreCustomerService.getCustAuth(xml);
                log.info("GeneServiceImplHK 查询结束!!!");
            } catch (Exception e) {
                log.info(e);
            }
        }
        log.info(this.getClass().getSimpleName()+"推送给微服务的xml: "+respXml);
        return respXml;
    }

}
