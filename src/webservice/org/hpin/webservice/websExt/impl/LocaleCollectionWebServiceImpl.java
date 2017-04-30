package org.hpin.webservice.websExt.impl;

import javax.jws.WebService;

import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.localeCollection.LocaleCollectionService;
import org.hpin.webservice.websExt.ILocaleCollectionWebService;
import org.springframework.stereotype.Service;

/**
 * 现场采集接口-自动建立场次;
 * @author Henry
 *
 */
@Service("org.hpin.webservice.websExt.impl.LocaleCollectionWebServiceImpl")
@WebService
public class LocaleCollectionWebServiceImpl implements ILocaleCollectionWebService{
	
	@Override
	public String pushBranchInfoAutoEvents(String xml) {
		LocaleCollectionService localeCollectionService = (LocaleCollectionService)SpringTool.getBean(LocaleCollectionService.class);
		return localeCollectionService.pushBranchInfoAutoEvents(xml);
	}
	
}
