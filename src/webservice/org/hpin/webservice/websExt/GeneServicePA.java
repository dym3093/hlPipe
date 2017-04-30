package org.hpin.webservice.websExt;



/**
 * 平安健康基因检测接口
 * @author machuan
 * @date 2017年2月9日
 */
public interface GeneServicePA {
    /**
     * 获取基因履约单
     * @param orderId
     * @param boxSn
     * @return
     * @author machuan
     * @date  2017年2月9日
     */
    String getOrder(String orderId,String boxSn);
    
    /**
     * 确认受检
     * @param orderId
     * @return
     * @author machuan
     * @date  2017年2月9日
     */
    String boxReceived(String orderId);
    
    /**
     * 检测失败
     * @param orderId
     * @param reason 检测失败原因 100字符以内
     * @return
     * @author machuan
     * @date  2017年2月9日
     */
    String detectFailed(String orderId,String reason);
    
    /**
     * 上传报告
     * @param orderId 履约单号
     * @param examineOption 检测项名称
     * @param pathFile 报告地址
     * @return
     * @author machuan
     * @date  2017年2月17日
     */
    String uploadReports(String orderId,String examineOption,String pathFile);

}






















