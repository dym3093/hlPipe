package org.hpin.webservice.service;


 interface YmGeneReportService {
	/**
	 * 保险公司营销员验证
	 * create by henry.xu 2017年2月8日
	 * @param xml
	 * @return
	 */
	public String verifySalesMan(String xml);

	/**
	 * 获取CRM中相关项目负责人以及项目名称信息，解析后返填到当前申请界面中
	 * create by henry.xu 20160905
	 * @param  projectNum 项目编码
	 * @return
	 */
	String getCrmBaseInfoByProCode(String projectNum);
	
	String getGeneReportInfo(String idcard, String name, String tel);
	String pushGeneReportInfo(String xml);
	//儿童基因套餐
	String pushGeneComboInfo(String xml);
	String pushGeneOrderInfo(String xml);
	String updateGeneOrderInfo(String xml);
	
	/**
	 * 团购订单处理
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-23 下午3:24:09
	 */
	String pushGeneGroupOrderInfo(String xml);
	/**
	 * 根据电话号码验证是否为会员
	 * @param phone
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-23 下午3:23:23
	 */
	String verifyMemberPhone(String phone);
	
	/**
	 * 根据用户信息验证是否为会员
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-23 下午3:25:01
	 */
	String verifyMemberInfo(String xml);
	
	/**
	 * PDF报告信息定时增量推送给微平台，微平台可以通过微信告知对应会员报告情况
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-6-23 下午3:25:38
	 */
	String pushPdfInfo(String xml);
	
	/**
	 * 创建团单
	 * @param jsonStr 发送的JSON格式字符串
	 * @return String JSON字符串
	 * @author DengYouming
	 * @since 2016-7-29 下午6:55:53
	 */
	String createGroupOrder(String jsonStr);
	
	/**
	 * 根据传入的参数获取对应的会员的报告并保存，返回保存的物理路径
	 * @param jsonStr json字符串
	 * @return String 物理路径
	 * @author DengYouming
	 * @since 2016-7-30 下午4:49:11
	 */
	 String gainReport(String jsonStr) throws Exception;
	
	/**
	 * 根据报告单ID获取报告单详情
	 * @param jsonStr
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-19 上午11:16:58
	 */
	 String gainReportDetail(String jsonStr) throws Exception;
	
	/**
	 * 根据传入的数据获取报告信息
	 * @param jsonStr
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-19 下午6:51:44
	 */
	 String gainReportInfo(String jsonStr) throws Exception;
	
	/**
	 * 根据传入的数据获取报告明细
	 * @param jsonStr
	 * @return String
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-19 下午6:52:19
	 */
	 String gainReportInfoDetail(String jsonStr) throws Exception;
	/**
	 * 根据JSON参数查找团单检测人列表
	 * @param jsonStr JSON字符串
	 * @return  String
	 * @author DengYouming
	 * @since 2016-7-30 下午4:16:28
	 */
	 String findTestees(String jsonStr);
	
	/**
	 * 客户已收采样盒
	 * @param xml
	 * @return String
	 * @author ChenQi
	 * @since 2016-6-23 下午3:24:09
	 */
	 String pushSampleInfo(String xml);
	
	/**
	 * 根据信息生成二维码
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-8-18 下午6:12:44
	 */
	 String pushEventQRCodeInfo(String xml);
	
	/**
	 * 获取场次相关二维码信息
	 * @param xml 
	 * @return String
	 * @author DengYouming
	 * @since 2016-8-18 上午11:13:44
	 */
	 String getEventQRCodeInfo(String xml);
	
	/**
	 * 设置二维码失效
	 * @param xml
	 * @return String
	 * @author DengYouming
	 * @since 2016-8-18 下午6:15:24
	 */
	 String setEventQRCodeInvalid(String xml);
	
	/**
	 * 保存场次相关的客户信息
	 * @param xml 客户信息
	 * @return String 1 or 0
	 * @author DengYouming
	 * @since 2016-8-18 上午11:23:42
	 */
	 String pushEventCustomerInfo(String xml);
	 
	 /**
	  * 获取套餐接口
	  * @param xml
	  * @return
	  * @author machuan
	  * @date  2017年2月4日
	  */
	String getComboInfo (String xml);
	

	/**
	 * 传过来的订单信息
	 * @param xml xml格式的文本
	 * @return String 返回信息
	 * @author DengYouming
	 * @since 2016-10-9 下午2:21:26
	 */
	 String pushErpOrder(String xml);

	/**
	 * 修改会员信息时调用CRM会员备案接口进行备份
	 * @param xml xml格式的报文字符串
	 * @return String 0或者1， 0代表成功，1代表失败
     */
	 String productMemberImmediate(String xml);
	/**
	 * 保存相关场次客户信息;
	 * 1、 系统自动生成条形码，生成规则为：查询远盟基因系统条码生成子表（erp_bar_code_detail）中，最大的条码号+1。
	 * （条码生成表中的条码默认规则为7位，以”W”开头，后面为数字：000001、000002……..）
	 * 2、 解析XML报文中的客户信息，将解析出来的客户信息以及步骤一中自动生成的条码信息存储erp_customer表中。
	 * 3、 接口返回步骤一中生成的条形码
	 * create by henry.xu 20161125
	 * @param xml 接口返回xml数据;
	 * @return String 条形码;
 	 */
	 String pushCustomerGenerCode(String xml);
	
	/**
	 * create by henry.xu 20161125
	 * 
	 * @param xml
	 * @return
	 */
	String validateQRCode(String xml);
	
	/**
	 * 客户扫描支公司二维码，填写信息，微服务调用基因系统
	 * create by henry.xu 2016年12月1日
	 * @return
	 */
	String pushCustomerGCByCompany(String xml);
	/**
	 * 客户扫描支公司二维码，填写信息，微服务调用基因系统
	 * create by henry.xu 2016年12月1日
	 * @return
	 */
	String pushCustomerGCByCompanyTaiPing(String xml);
	
	/**
	 * 知康每天定时23:59:59 将当天检测的客户信息打包推送给远盟，远盟提供接收接口，进行处理
	 * create by henry.xu 2016年12月2日
	 * @param json
	 * @return
	 */
	String receiveExamined(String json);
	/**
	 * 支公司二维码生成
	 * @param xml
	 * @return
	 * @author machuan
	 * @date  2016年12月5日
	 */
	 String pushCompanyQRCodeInfoWuChuang(String xml);

	 /**
	  * 客户身份验证
	  * @param xml
	  * @return String
	  * @auther Damian
	  * @since 2016-12-28
	  */
	 String getCustomerAuth(String xml);
	 
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
	  * @return String
	  * @auther Damian
	  * @since 2016-12-28
	  */
	 String pushCustomerInfoHK(String xml);
	
	 /**
	  * 保存客户信息 TY
	  * @param xml
	  * @return
	  * @author machuan
	  * @date  2017年1月18日
	  */
	String pushCustomerInfo(String xml);

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
	 
	 /**
	  * 获取基因报告文件信息; 由于针对星宁单独提供基因报告查询;
	  * 与之前不同在于返回中一个条码可能对应多个报告;
	  * <p>Description: </p>
	  * @author herny.xu
	  * @date 2017年4月11日
	  */
	 String getGeneReportInfoMyMutil(String idcard, String name, String tel);

	 /**
	  * 客户报告JPG查询
	  * @param name 姓名
	  * @param tel 电话
	  * @param birthday 生日
	  * @return String XML字符串
	  * @auther Damian
	  * @since 2017-02-06
	  */
	 String getGeneReportInfoImg (String name, String tel, String birthday);

	 /**
	  * 无创-生物电客户报告JPG查询接口
	  * @param name 姓名
	  * @param tel 电话
	  * @param idno 身份证号
	  * @return String XML字符串
	  * @auther Damian
	  * @since 2017-02-10
	  */
	 String getBlyReportInfoImg (String name, String tel, String idno);

	 /**
	  * 客户状态推送(太平微磁TPWC）
	  * @param xml
	  * @return String
	  * @auther Damian
	  * @since 2017-02-17
	  */
	 String pushCustomerStatusTPWC(String xml);

	 /**
	  * 客户状态推送(星宁基因）
	  * @param xml
	  * @return String
	  * @auther Damian
	  * @since 2017-04-17
	  */
	 String pushCustomerStatusXN(String xml);
	 
	 /**
	  * 
	  * 基因通用报告JPG查询接口
	  * 
	  * @param idcard 	身份证号
	  * @param name		姓名
	  * @param tel		电话
	  * @return String
	  * @author LeslieTong
	  * @date 2017-4-20下午3:00:26
	  */
	 String getGeneReportInfoAll(String idcard,String name, String tel);

}
