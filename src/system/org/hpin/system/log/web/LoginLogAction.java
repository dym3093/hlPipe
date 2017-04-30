package org.hpin.system.log.web;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hpin.common.core.SpringTool;
import org.hpin.common.core.web.BaseAction;
import org.hpin.common.util.HttpTool;
import org.hpin.common.widget.pagination.Page;
import org.hpin.system.log.service.LoginLogService;

/**
 * 用户登录日志Action
 * 
 * @author thinkpad
 * @data Dec 2, 2009
 */
@Namespace("/system")
@Action("loginLog")
@Results( { @Result(name = "listLoginLog", location = "/WEB-INF/content/system/log/listLoginLog.jsp") })
public class LoginLogAction extends BaseAction {

	private LoginLogService loginLogService = (LoginLogService) SpringTool
			.getBean(LoginLogService.class);

	/**
	 * 现实用户登录日志列表
	 */
	public String listLoginLog() throws Exception {
		Map searchMap = super.buildSearch();
		page = new Page(HttpTool.getPageNum());
		loginLogService.findByPage(page, searchMap);
		return "listLoginLog";
	}
}
