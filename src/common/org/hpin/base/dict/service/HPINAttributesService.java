package org.hpin.base.dict.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 * 
 */
@Service(value = "org.hpin.base.dict.service.HPINAttributesService")
@Transactional()
public class HPINAttributesService {
	/**
	 * 事例
	 */
	private String sample;

	/**
	 * 登陆方式，可为sso，acegi
	 */
	private String loginType;

	/**
	 * 默认每页显示条数
	 */
	private Integer pageSize;

	/**
	 * cas server 地址
	 */
	private String casUrl;

	/**
	 * hpin地址
	 */
	private String hpinUrl;

	/**
	 * 数据库类型
	 */
	private String dbType;

	/**
	 * 版本是否发布
	 */
	private String release;

	/**
	 * 队列是否启用
	 */
	private String sequenceOpen;

	/**
	 * 自定义图片资源文件地址
	 */
	private String propertiesFilePath;

	/**
	 * wap工单url
	 */
	private String wapSheetUrl;

	/**
	 * 版本日志打印路径
	 */
	private String versionPath;

	/**
	 * 多菜单ip是否启用 on为启用 off为禁用
	 */
	private String menu_ipOpen;

	public String getMenu_ipOpen() {
		return menu_ipOpen;
	}

	public void setMenu_ipOpen(String menu_ipOpen) {
		this.menu_ipOpen = menu_ipOpen;
	}
	public String getSequenceOpen() {
		return sequenceOpen;
	}

	public void setSequenceOpen(String sequenceOpen) {
		this.sequenceOpen = sequenceOpen;
	}

	/**
	 * @return the release
	 */
	public String getRelease() {
		return release;
	}

	/**
	 * @param release
	 *            the release to set
	 */
	public void setRelease(String release) {
		this.release = release;
	}

	/**
	 * @return the sample
	 */
	public String getSample() {
		return sample;
	}

	/**
	 * @param sample
	 *            the sample to set
	 */
	public void setSample(String sample) {
		this.sample = sample;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getCasUrl() {
		return casUrl;
	}

	public void setCasUrl(String casUrl) {
		this.casUrl = casUrl;
	}

	public String getHpinUrl() {
		return hpinUrl;
	}

	public void setHpinUrl(String hpinUrl) {
		this.hpinUrl = hpinUrl;
	}

	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType
	 *            the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}

	public void setPropertiesFilePath(String propertiesFilePath) {
		this.propertiesFilePath = propertiesFilePath;
	}

	public String getWapSheetUrl() {
		return wapSheetUrl;
	}

	public void setWapSheetUrl(String wapSheetUrl) {
		this.wapSheetUrl = wapSheetUrl;
	}

	/**
	 * @return the versionPath
	 */
	public String getVersionPath() {
		return versionPath;
	}

	/**
	 * @param versionPath
	 *            the versionPath to set
	 */
	public void setVersionPath(String versionPath) {
		this.versionPath = versionPath;
	}

	/**
	 * 注册的IP映射属性
	 */
	private Map register;

	/**
	 * 注册的接口调用过滤
	 */
	private List interfaceList;

	public List getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List interfaceList) {
		this.interfaceList = interfaceList;
	}

	public Map getRegister() {
		return register;
	}

	public void setRegister(Map register) {
		this.register = register;
	}

}
