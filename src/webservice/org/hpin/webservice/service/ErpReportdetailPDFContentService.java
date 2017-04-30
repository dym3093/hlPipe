/**
 * @author DengYouming
 * @since 2016-8-16 下午6:30:02
 */
package org.hpin.webservice.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.webservice.bean.*;
import org.hpin.webservice.dao.ErpReportdetailPDFContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author DengYouming
 * @since 2016-8-16 下午6:30:02
 */
@Service(value = "org.hpin.webservice.service.ErpReportdetailPDFContentService")
@Transactional()
public class ErpReportdetailPDFContentService {

	Logger dealLog = Logger.getLogger("dealReport");

	@Autowired
	private ErpReportdetailPDFContentDao dao;

	@Autowired
	private ErpPrintTaskContentService contentService;
	
	/**
	 * 
	 * @param pdfContentList
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-30 下午8:47:36
	 */
	public void save(List<ErpReportdetailPDFContent> pdfContentList) throws Exception{
		dao.save(pdfContentList);
	}
	
	/**
	 * 
	 * @param obj
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-10-10 下午12:06:17
	 */
	public void saveEntity(ErpReportdetailPDFContent obj) throws Exception{
		dao.saveEntity(obj);
	}
	
	/**
	 * 
	 * @param obj
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-30 下午8:47:40
	 */
	public void update(ErpReportdetailPDFContent obj) throws Exception{
		dao.update(obj);
	}
	
	/**
	 * 
	 * @param pdfName
	 * @param code
	 * @return
	 * @throws Exception
	 * @author DengYouming
	 * @since 2016-8-30 下午8:47:43
	 */
	public List<ErpReportdetailPDFContent> findByProps(String code, String pdfName) throws Exception{
		List<ErpReportdetailPDFContent> list = null;
		if(StringUtils.isNotEmpty(code)&&StringUtils.isNotEmpty(pdfName)){
			list = dao.findByProps( code,pdfName);
		}
		return list;
	}

	/**
	 * 把要打印的文件添加到打印任务
	 * @param filePath 文件的物理路径
	 * @param entity 客户信息
	 * @param events 场次信息
	 * @return boolean
	 * @author DengYouming
	 * @since 2016-10-26 下午2:14:56
	 */
	public boolean deal4PrintTask(String filePath, Integer fileSize, ErpCustomer entity, ErpEvents events){
		//返回标志
		boolean flag = false;
		ErpReportdetailPDFContent pdfContentObj = null;

		String name = entity.getName();
		String barcode = entity.getCode();
		String age = entity.getAge();
		String gender = entity.getSex();
		String combo = entity.getSetmealName();
		Date date = Calendar.getInstance().getTime();
		//查找PDF匹配报告
		List<ErpReportdetailPDFContent> pdfList = null;

		String pdfName = filePath.substring(filePath.lastIndexOf(File.separator)+1);

		try {
			pdfList = this.findByProps(barcode, pdfName);

			if(StringUtils.isNotEmpty(filePath)&&filePath.contains(".")){

				String type = "3";
				if(StringUtils.containsIgnoreCase(filePath, "pdf")){
					type = "2";
				}

				if(!CollectionUtils.isEmpty(pdfList)){
					pdfContentObj = pdfList.get(0);
					if(pdfContentObj!=null){
						//无文件路径或者文件路径不正常，则更新，否则不更新
						pdfContentObj.setUsername(name); //修复bug用
						pdfContentObj.setFilepath(filePath);
						pdfContentObj.setFilesize(""+fileSize); // add by me 2016-12-19
						pdfContentObj.setUpdateTime(date);
						this.update(pdfContentObj);

						/**@since 2016年10月8日15:18:37 @author Carly*/
						List<ErpPrintTaskContent> contentList = contentService.getContentByPdfId(pdfContentObj.getId());
						if (!CollectionUtils.isEmpty(contentList)) {
							ErpPrintTaskContent content2 = contentList.get(0);
							content2.setFilePath(filePath);
							content2.setUserName(name);
							content2.setUpdateTime(date);
							content2.setType(type);
							contentService.update(content2);
						}
						flag = true;
						dealLog.info("姓名："+name+",条码："+barcode+",ERP_REPORTDETAIL_PDFCONTENT表中信息已更新！");
					}

				}else{

					pdfContentObj = new ErpReportdetailPDFContent();

					pdfContentObj.setPdfname(pdfName);
					pdfContentObj.setUsername(name);
					pdfContentObj.setAge(""+age);
					pdfContentObj.setCode(barcode);
					pdfContentObj.setSex(gender);
					pdfContentObj.setSetmeal_name(combo);

					pdfContentObj.setFilesize(""+fileSize);
//					pdfContentObj.setMd5(md5);
					pdfContentObj.setBatchno(events.getBatchNo());
					pdfContentObj.setFilepath(filePath);
					pdfContentObj.setIsrecord(2);
					pdfContentObj.setIsrepeat(0);

					pdfContentObj.setMatchstate(2); //匹配状态 add 2016-12-30

					pdfContentObj.setCreatedate(Calendar.getInstance().getTime());
					pdfContentObj.setProvice(entity.getProvice());
					pdfContentObj.setCity(entity.getCity());
					pdfContentObj.setBranch_company(events.getBranchCompanyId());
					pdfContentObj.setEvents_no(events.getEventsNo());

					pdfContentObj.setPs("0");
					pdfContentObj.setSettlement_status("0");

					this.saveEntity(pdfContentObj);

					//add by chenqi @since 2016年10月11日12:01:34 添加到需要打印的表（ErpPrintTaskContent）中
					List<ErpCustomer> customerList = contentService.getCustomerInfoByCode(pdfContentObj.getCode());
					CustomerRelationShipPro shipPro = contentService.getProjectCodeByEvent(customerList.get(0).getEventsNo());
					ErpPrintTaskContent contents = new ErpPrintTaskContent();
					contents.setAge(pdfContentObj.getAge());
					contents.setBatchNo(pdfContentObj.getBatchno());
					contents.setBranchCompanyId(pdfContentObj.getBranch_company());
					contents.setProvince(pdfContentObj.getProvice());
					contents.setCity(pdfContentObj.getCity());
					contents.setCode(pdfContentObj.getCode());
					contents.setUserName(pdfContentObj.getUsername());
					contents.setCombo(pdfContentObj.getSetmeal_name());
					contents.setGender(pdfContentObj.getSex());
					contents.setBatchNo(pdfContentObj.getBatchno());
					contents.setFilePath(pdfContentObj.getFilepath());
					contents.setSaleman(pdfContentObj.getSales_man());
					contents.setPdfContentId(pdfContentObj.getId());

					contents.setCustomerId(customerList.get(0).getId());
					contents.setDept(customerList.get(0).getDepartment());
					contents.setOwnedCompanyId(customerList.get(0).getOwnedCompanyId());

					if (shipPro!=null) {
						contents.setProjectCode(shipPro.getProjectCode());
					}
					contents.setPs("0");
					contents.setType(type);
					contents.setIsManuallyAdd("2");
					contents.setCreateTime(date);
					contents.setCreateUser("金域");
					contents.setReportType(1); // modify 2016-12-12

					contentService.save(contents);

					flag = true;
					}
				}
		} catch (Exception e) {
			dealLog.info(e);
		}

		return flag;
	}

}
