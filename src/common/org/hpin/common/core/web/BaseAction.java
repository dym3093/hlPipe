package org.hpin.common.core.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hpin.common.util.ServletUtils;
import org.hpin.common.widget.pagination.Page;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	//protected Long id = null;
	protected String id = null;
	protected String ids = null;
	protected String mids = null;
	protected Page page = null;

	protected String alert = "操作成功！";

	protected String jumpUrl = null;

	protected String jsonString = null;
	protected String navTabId;

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getIds() {
		return ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getMids() {
		return mids;
	}

	public void setMids(String mids) {
		this.mids = mids;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	public String getJsonString() {
		return jsonString;
	}
	
	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	/**
	 * 获取查询参数(查询参数以filter开始)
	 * 
	 * @return 查询参数
	 */
	@SuppressWarnings("unchecked")
	public Map buildSearch() {
		Map<String, String> filterParamMap = ServletUtils.getParametersStartWith(ServletActionContext.getRequest(),"filter", false);
		filterParamMap = ServletUtils.getEncodParametersStartWith(ServletActionContext.getRequest(),"Efilter", false,filterParamMap);
		Map<String, String> resultMap  = new HashMap<String, String>();
		for(String strValue : filterParamMap.keySet()){
			if(StringUtils.isBlank(strValue)) {
				continue ;
			}
			String valueSearch = filterParamMap.get(strValue) ;
			if(StringUtils.isBlank(valueSearch)) {
				continue ;
			}
			resultMap.put(strValue , valueSearch.trim() ) ;
		}
		Map<String , String> orderMap = ServletUtils.getParametersStartWith(ServletActionContext.getRequest(), "order", false) ;
		
		for(String str : orderMap.keySet()){
			if(StringUtils.isBlank(str)) {
				continue ;
			}
			String value = orderMap.get(str) ;
			if(StringUtils.isBlank(value)) {
				continue ;
			}
			resultMap.put(str , value.trim() ) ;
		}
		return resultMap;
	}

	/**
	 * 将其他参数转换为查询参数
	 * 
	 * @param searchMap
	 * @param paramName
	 * @param propertyName
	 */
	public void transferBuildSearch(Map searchMap, String paramName,
			String propertyName) {
		String value = ServletActionContext.getRequest()
				.getParameter(paramName);
		buildSearch(searchMap, propertyName, value);
	}

	/**
	 * 添加查询参数
	 * 
	 * @param searchMap
	 * @param propertyName
	 * @param propertyValue
	 */
	public void buildSearch(Map searchMap, String propertyName,
			Object propertyValue) {
		searchMap.put(propertyName, propertyValue.toString());
	}

	/**
	 * json处理函数
	 * 
	 * @param jsonList
	 * @return
	 */
	public String json(List jsonList) {
		JSONArray array = JSONArray.fromObject(jsonList);
		this.jsonString = array.toString();
		return "json";
	}

	/**
	 * 设置跳转页面
	 * 
	 * @param jumpUrl
	 * @return
	 */
	public String jump(String jumpUrl) {
		this.jumpUrl = jumpUrl;
		return "jump";
	}

	/**
	 * 设置跳转页面
	 * 
	 * @param jumpUrl
	 * @return
	 */
	public String jump(String jumpUrl, String alert) {
		this.alert = alert;
		this.jumpUrl = jumpUrl;
		return "jump";
	}

	/**
	 * 向页面写JSON数据
	 * 
	 * @param json
	 */
	protected void renderJson(JSONObject json) {
		renderJson(json, "text/plain");
	}

	/**
	 * 向页面写JSON数据
	 * 
	 * @param json
	 * @param responseType
	 *            返回类型
	 */
	protected void renderJson(JSONObject json, String responseType) {
		if (StringUtils.isBlank(responseType)) {
			responseType = "text/plain";
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			json.write(response.getWriter());
		} catch (Exception e) {
			// logger.error("向页面输出数据时出错!", e);
		}
	}
	
	protected void printObject(String str) {
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
}
