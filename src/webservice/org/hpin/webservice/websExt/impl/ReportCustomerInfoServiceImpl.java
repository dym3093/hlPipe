package org.hpin.webservice.websExt.impl;

import javax.jws.WebService;

import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.ReportCustomerInfoService;
import org.hpin.webservice.websExt.IReportCustomerInfoService;
import org.springframework.stereotype.Service;

@Service("org.hpin.webservice.websExt.impl.ReportCustomerInfoServiceImpl")
@WebService
public class ReportCustomerInfoServiceImpl implements IReportCustomerInfoService {

	/**
	 * 根据申友提供的已出具报告客户信息存入基因系统内，供后续对报告进度进行跟踪
	 * 
	 */
	@Override
	public String customerReportGene(String xml) {
		ReportCustomerInfoService reportCustomerInfoService = (ReportCustomerInfoService)SpringTool.getBean(ReportCustomerInfoService.class);
		return reportCustomerInfoService.saveReportCustomerInfo(xml);
	}

}
