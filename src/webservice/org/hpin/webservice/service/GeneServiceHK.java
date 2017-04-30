package org.hpin.webservice.service;

/**
 * 弘康业务接口
 * Created by Damian on 16-12-28.
 */
public interface GeneServiceHK {

    /**
     * 客户身份验证
     * @param xml
     * @return String
     * @auther Damian
     * @since 2016-12-28
     */
    String getCustomerAuth(String xml);

    /**
    * 保存客户信息
    * @param xml
    * @return String
    * @auther Damian
    * @since 2016-12-28
    */
    String pushCustomerInfoHK(String xml);

    /**
     * 客户状态推送
     * @param xml
     * @return String
     * @auther Damian
     * @since 2016-12-28
     */
    String pushCustomerStatus(String xml);


    /**
     * 华夏，易安，北京邮政项目：查询预导入表进行客户信息验证
     * @param xml XML格式的字符串
     * @return String
     * @auther Damian
     * @since 2017-01-17
     */
    String getCustAuth(String xml);

}






















