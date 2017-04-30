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
import org.hpin.system.log.service.OperationLogService;

/**
 * 系统操作日志
 * 
 * @author thinkpad
 * @data Mar 22, 2010
 */
@Namespace("/system")
@Action("operationLog")
@Results( {
		@Result(name = "listOperationLog", location = "/WEB-INF/content/system/log/listOperationLog.jsp"),
		@Result(name = "listOperationLogForLoginLog", location = "/WEB-INF/content/system/log/listOperationLogForLoginLog.jsp") })
public class OperationLogAction extends BaseAction {
	private OperationLogService operationLogService = (OperationLogService) SpringTool
			.getBean(OperationLogService.class);

	/**
	 * 分页查询系统登录日志
	 */
	public String listOperationLog() throws Exception {
		Map searchMap = super.buildSearch();
		page = new Page(HttpTool.getPageNum());
		operationLogService.findByPage(page, searchMap);
		return "listOperationLog";
	}

	/**
	 * 按登录日志获取所属的操作日志
	 */
	public String listOperationLogForLoginLog() throws Exception {
		Map searchMap = super.buildSearch();
		page = new Page(HttpTool.getPageNum());
		searchMap.put("filter_and_loginLogId_EQ_S", "pass_loginLogId");
		operationLogService.findByPage(page, searchMap);
		return "listOperationLogForLoginLog";
	}
}
