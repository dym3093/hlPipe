package org.hpin.webservice.service;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.ty.ErpTYCustomerService;
import org.hpin.webservice.util.Tools;
import org.springframework.stereotype.Service;


/**
 * 天津邮政业务实现类
 * @author machuan
 * @date 2017年1月17日
 */
@Service(value = "org.hpin.webservice.service.GeneServiceImplTY")
@WebService
public class GeneServiceImplTY implements GeneServiceTY{

    private ErpTYCustomerService erpTYCustomerService;


    
    /**
     * 检测码验证
     * @param xml
     * @return
     * @author machuan
     * @date  2017年1月17日
     */
    @Override
    public String getIDAuth(String xml) {
        Logger log = Logger.getLogger("getIDAuth");
        String respXml = null;
        log.info("接收到的xml "+xml);
        if(StringUtils.isNotEmpty(xml)){
            //TODO 测试，先屏蔽正式逻辑
           //respXml = PropertiesUtils.getString("data","getCustomerAuth.resp");
            try {
            	erpTYCustomerService = (ErpTYCustomerService) SpringTool.getBean(ErpTYCustomerService.class);
                log.info("开始查询...");
                respXml = erpTYCustomerService.getIDAuth(xml);
                log.info("查询结束!!!");
            } catch (Exception e) {
                log.info(e);
            }
        }else{
           respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respAuthInfo>\n" +
                "<result>0</result>\n" +
                "<message>您好，未查到您的信息，请核对信息后再提交，谢谢。</message>\n" +
                "<mealName></mealName>\n" +
                "<eventNo></eventNo>\n" +
                "</respAuthInfo>";

        }
        log.info("推送给微服务的xml: "+respXml);
        return respXml;
    }
    
    
    /**
     * 保存客户信息
     * @param xml
     * @return
     * @author machuan
     * @date  2017年1月18日
     */
    @Override
    public String pushCustomerInfo(String xml) {
        Logger log = Logger.getLogger("pushCustomerInfo");
        String respXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<respCustomer>\n" +
                "<result>0</result>\n" +
                "<message>保存失败</message>\n" +
                "</respCustomer>";

        log.info("接收到的xml "+xml);
        String reqXml = Tools.decodeStr(xml);
        log.info("转换成UTF8格式后的xml字符串： "+reqXml);
        if(StringUtils.isNotEmpty(xml)){
            //TODO 测试，先屏蔽正式逻辑
            //respXml = PropertiesUtils.getString("data","pushCustomerInfoHK.resp");
            try {
            	erpTYCustomerService = (ErpTYCustomerService) SpringTool.getBean(ErpTYCustomerService.class);
                log.info("开始保存...");
                //保存从微服务过来的数据
                respXml = erpTYCustomerService.pushCustomerInfo(xml);
                log.info("保存结束!!!");
            } catch (Exception e) {
                 log.info(e);
            }
        }
        log.info("推送给微服务的xml: "+respXml);
        return respXml;
    }


}
