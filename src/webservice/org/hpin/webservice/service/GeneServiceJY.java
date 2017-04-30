package org.hpin.webservice.service;

/**
 * 金域接口相关方法
 * Created by Dayton on 2016/11/8.
 */
public interface GeneServiceJY {

    /**
     * 创建癌筛团单号
     * @param jsonStr
     * @return String
     */
    String createGroupOrder(String jsonStr);

    /**
     * 获取报告
     * @param jsonStr
     * @return String
     */
    String gainReport(String jsonStr) throws Exception;

    /**
     * 获取报告单
     * @param jsonStr
     * @return String
     */
    String gainReportDetail(String jsonStr) throws Exception;

    /**
     * 根据团单号查找所有相关团单的会员信息，返回会员信息的JSONArray字符串
     * @param jsonStr JSON字符串
     * @return String
     * @throws Exception
     * @author DengYouming
     * @since 2016-10-21 上午10:21:39
     */
    String findTestessAll(String jsonStr) throws Exception;

    /**
     * 查询客户信息
     * @param jsonStr
     * @return String
     * @throws Exception
     */
    String findTestees(String jsonStr) throws Exception;

    /**
     * 获取报告信息
     * @param jsonStr
     * @return
     * @throws Exception
     */
    String gainReportInfo(String jsonStr) throws Exception;

    /**
     * 获取报告单明细信息
     * @param jsonStr
     * @return
     * @throws Exception
     */
    String gainReportInfoDetail(String jsonStr) throws Exception;

    /**
     * 取消服务
     * @param jsonStr JSON字符串
     * @return String
     */
    String cancelService(String jsonStr);

    /**
     * 删除团单
     * @param jsonStr JSON字符串
     * @return String
     */
    String delOrder(String jsonStr);

    /**
     * @desc  用于测试的接口， 暂时写在金域的接口中
     * @param str 场次号/批次号/团单号等信息
     * @author Damian
     * @since 17-3-17 下午6:16
     */
    String loadEvent(String str);
}
