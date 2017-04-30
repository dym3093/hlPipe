package org.hpin.webservice.websExt;

/**
 * 
 * <p>Description: 根据申友提供的已出具报告客户信息存入基因系统内，供后续对报告进度进行跟踪</p>
 * @author henry.xu
 * @date 2017年2月22日
 */
public interface IReportCustomerInfoService {
	/**
	 * 
	 * <p>Description: 根据申友提供的已出具报告客户信息存入基因系统内，供后续对报告进度进行跟踪</p>
	 * @author herny.xu
	 * @date 2017年2月22日
	 */
	public String customerReportGene(String xml);
}
