package org.hpin.webservice.websExt;

public interface ILocaleCollectionWebService {
	
	/**
	 * 根据微服务提供的支公司信息，基因系统自动生成场次信息。
	 * @param xml
	 * @return
	 */
	String pushBranchInfoAutoEvents(String xml);
	
	
}
