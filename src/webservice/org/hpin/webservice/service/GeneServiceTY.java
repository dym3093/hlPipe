package org.hpin.webservice.service;


/**
 * 天津邮政业务接口
 * @author machuan
 * @date 2017年1月17日
 */
public interface GeneServiceTY {
    /**
     * 检测码验证
     * @param xml
     * @return
     * @author machuan
     * @date  2017年1月17日
     */
    String getIDAuth(String xml);
    
    /**
     * 保存客户信息
     * @param xml
     * @return
     * @author machuan
     * @date  2017年1月18日
     */
    String pushCustomerInfo(String xml);

}






















