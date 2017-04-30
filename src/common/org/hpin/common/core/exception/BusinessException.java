package org.hpin.common.core.exception;

/**
 * 执行业务逻辑时出现的异常，BusinessException是所有这些异常的基类
 * 
 * @author thinkpad
 * @Apr 16, 2009
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	private String code = "000000";// 异常编码

	private String message = null;// 异常信息

	private String dealPage = null;// 处理页面

	private String returnPage = null; // 返回页面

	public BusinessException() {
		super("Error occurred in application.");
	}

	public BusinessException(String code) {
		this.code = code;
	}
	public BusinessException(Throwable cause) {
		super(cause);
	}
	public BusinessException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDealPage() {
		return dealPage;
	}

	public void setDealPage(String dealPage) {
		this.dealPage = dealPage;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}
}