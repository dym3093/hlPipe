package org.hpin.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.XmlUtils;
import org.hpin.webservice.bean.ErpCustomer;
import org.hpin.webservice.bean.ErpReportdetailImgtask;
import org.hpin.webservice.bean.ErpReportdetailPDFContent;
import org.hpin.webservice.dao.ErpCustomerDao;
import org.hpin.webservice.dao.ErpReportdetailImgtaskDao;
import org.hpin.webservice.dao.ErpReportdetailPDFContentDao;
import org.hpin.webservice.util.PropertiesUtils;
import org.hpin.webservice.util.ReturnStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JPG报告查询
 * @author daimian
 * @since 2017-02-14
 */
@Transactional
@Service("org.hpin.webservice.service.ErpReportdetailImgtaskService")
public class ErpReportdetailImgtaskService extends BaseService{

    @Autowired
    private ErpReportdetailImgtaskDao dao;
    
    @Autowired
    private ErpCustomerDao erpCustomerDao; //add by henry.xu 20170411 客户信息Dao
    
    @Autowired
    private ErpReportdetailPDFContentDao erpReportdetailPDFContentDao; //add by henry.xu 20170411 报告信息查询dao
    

    static Logger logger = Logger.getLogger(ErpReportdetailImgtaskService.class);
    
    private static final String WU_CHUANG = "wuchuang";
    
    private static final String WEI_CI = "weici";
    
    private static final String GENE = "gene";

    //预览地址
    private static final String FIND_IMG_VIEW_PREFIX = PropertiesUtils.getString("foreign", "view_prefix");
    //保存文件名
    private static final String FIND_IMG_SAVE_DIR = PropertiesUtils.getString("foreign", "view_saveDir");
    //错误年龄提示
    private static final String MSG_ERR_AGE = PropertiesUtils.getString("foreign", "FIND_IMG_MSG_ERR_AGE");
    //返回的XML标签头
    private static final String XML_FIND_IMG_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response>";
    //返回的XML标签尾
    private static final String XML_FIND_IMG_TAIL = "</response>";
    //微磁查询JPG报告SQL
    private static String SQL_FIND_IMG_WEICI = null;
    //无创生物电查询JPG报告SQL
    private static String SQL_FIND_IMG_BLY= null;
    //查找年龄SQL
    private static String SQL_FIND_AGE = null;
    
    //查询ErpCustomer信息（用于查询通用JPG）
    private static String SQL_FIND_CUSTOMER_INFO = null;
    
    //查询 imgTask
    private static String SQL_FIND_IMGTASK = null;
    
    //查询 imgInfo
    private static String SQL_FIND_IMGINFO = null;
    
    //查询pdfContent的pdf路径
    private static String SQL_PDFCONTENT_INFO = null;
    
    //查询img报告通用返回XML标签头
    private static final String XML_FIND_IMG_REPORT_ALL_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response>";
    
    //查询img报告通用返回XML标签头
    private static final String XML_FIND_IMG_REPORT_ALL_TAIL = "</response>";
    

    static {
        try {
            SQL_FIND_IMG_WEICI = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_IMG_WEICI']");
            SQL_FIND_IMG_BLY = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_IMG_BLY']");
            SQL_FIND_AGE = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_AGE_WC']");
            
            SQL_FIND_CUSTOMER_INFO = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_CUSTOMER']");
            SQL_FIND_IMGTASK = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_IMGTASK']");
            SQL_FIND_IMGINFO = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_IMGINFO']");
            SQL_PDFCONTENT_INFO = XmlUtils.getSingleTxt("sql.xml", "/sql_list/sql[@id='FIND_PDFCONTENT_INFO']");
            
            logger.info("SQL_FIND_IMG_WEICI: " + SQL_FIND_IMG_WEICI);
            logger.info("SQL_FIND_IMG_BLY: " + SQL_FIND_IMG_BLY);
            logger.info("SQL_FIND_AGE: " + SQL_FIND_AGE);
            
            logger.info("SQL_FIND_CUSTOMER_INFO: " + SQL_FIND_CUSTOMER_INFO);
            logger.info("SQL_FIND_IMGTASK: " + SQL_FIND_IMGTASK);
            logger.info("SQL_FIND_IMGINFO: " + SQL_FIND_IMGINFO);
            logger.info("SQL_PDFCONTENT_INFO: " + SQL_PDFCONTENT_INFO);

        } catch (FileNotFoundException e) {
            logger.info(e);
        } catch (DocumentException e) {
            logger.info(e);
        }
    }
    
    /**
	 * 根据条件查询报告文件信息;
	 * <p>Description: </p>
	 * @author herny.xu
	 * @date 2017年4月11日
	 */
    public String findGeneReportInfoMyMutil(String idcard, String name, String tel) {
    	
    	String identityStatus = ""; //身份核实状态
    	String code = ""; //条形码
    	String reportStatus = ""; //报告状态
    	List<ErpReportdetailPDFContent> pdfContents  = null; //PDF文档路径
    	
    	//>>1. 根据code和name查询客户信息;
    	ErpCustomer cus = this.erpCustomerDao.findByCodeAndName(idcard, name, tel);
    	
    	if(cus == null) {
    		identityStatus = "0"; //未匹配
    		reportStatus   = "0"; //未出报告;
    		return ReturnStringUtils.geneReportInfoResult(identityStatus, code, reportStatus, pdfContents);
    	}
    	
    	//>>2. 身份核实状态;0:身份不匹配, 1:全信息匹配
    	identityStatus = "1"; //全信息匹配;
    	
    	//>>3. 条码
    	code = cus.getCode();
    	
    	//>>4. 1:报告已出，0：报告未出(根据code和那么去pdfcount表中查询) 5. PDF文档路径;
    	pdfContents = this.erpReportdetailPDFContentDao.findByCodeAndUserName(code, name);
    	if(pdfContents == null || pdfContents.size()<=0) {
    		reportStatus   = "0"; //未出报告;
    	} else {
    		reportStatus   = "1"; //已出报告;
    		//处理同一个套餐只展示一本报告,遍历集合去掉相同名称套餐的数据;
    		for(int i=0; i< pdfContents.size(); i++) {
    			for(int j=i+1; j<pdfContents.size(); j++) {
    				//如果套餐存在相等的则移除其中之一
    				if(pdfContents.get(i).getSetmeal_name().equals(pdfContents.get(j).getSetmeal_name())) {
    					pdfContents.remove(j--);
    				}
    			}
    		}
    		
    	}
    	
    	return ReturnStringUtils.geneReportInfoResult(identityStatus, code, reportStatus, pdfContents);
    }

    /**
     * 查找检测人报告的预览地址
     * @param params 查询条件
     * @return String XML字符串
     * @author Damian
     * @since 2017-03-14
     */
    public String findImg(Map<String, String> params) {
        Logger log = null;
        List<Map<String, Object>> imgPathList = null;
        if (!CollectionUtils.isEmpty(params)){
            if (params.containsKey(ErpReportdetailImgtask.F_BIRTHDAY)||params.containsKey(ErpReportdetailImgtask.F_IDNO)){
                int len = params.keySet().size();
                String[] arr = new String[len];
                String[] queryArr = null;
                int i = 0;
                arr[i] = params.get(ErpReportdetailImgtask.F_USERNAME);
                arr[++i] = params.get(ErpReportdetailImgtask.F_PHONENO);
                //SQL
                String sql = null;
                if (params.containsKey(ErpReportdetailImgtask.F_BIRTHDAY)) {
                    //微磁
                    arr[++i] = params.get(ErpReportdetailImgtask.F_BIRTHDAY);
                    log = Logger.getLogger("getGeneReportInfoImg");
                    queryArr = Arrays.copyOf(arr, len-1);
                    sql = SQL_FIND_IMG_WEICI;
                } else if (params.containsKey(ErpReportdetailImgtask.F_IDNO)) {
                    //无创
                    log = Logger.getLogger("getBlyReportInfoImg");
                    arr[++i] = params.get(ErpReportdetailImgtask.F_IDNO);
                    queryArr = arr;
                    sql = SQL_FIND_IMG_BLY;
                }
                log.info("收到的查询参数 arr: "+ Arrays.toString(arr));
                log.info("实际的查询参数 queryArr: "+ Arrays.toString(queryArr));
                log.info("查询语句 sql: "+ sql);
                //查询
                if (StringUtils.isNotEmpty(sql)&&queryArr.length>0) {
                    imgPathList = dao.getJdbcTemplate().queryForList(sql, queryArr);
                }
            } else {
                log = logger;
                log.info("没有查询所需的参数！");
            }
        } else {
            log = logger;
            log.info("空的查询参数！");
        }
        //生成XML
        String respXml = this.generateXml(imgPathList, log);
        log.info("返回给调用者 respXml: "+respXml);
        return respXml;
    }

    /**
     * 生成XML字符串
     * @return String XML格式字符串
     * @author Damian
     * @since 2017-03-14
     */
    private String generateXml(List<Map<String,Object>> list, Logger log) {
        String code = "";
        String identityStatus = "0";
        String reportStatus = "0";
        String message = "";
        StringBuilder imgPaths = new StringBuilder();
        //年龄
        int age;
        //是否返回报告
        boolean reportFlag = false;

        log.info("FIND_IMG_VIEW_PREFIX: "+FIND_IMG_VIEW_PREFIX);
        log.info("FIND_IMG_SAVE_DIR: "+FIND_IMG_SAVE_DIR);
        if (!CollectionUtils.isEmpty(list)){
            log.info("预览地址数量 size: "+list.size());
            code = (String) list.get(0).get("CODE");
            log.info("code: "+code);
            List<Map<String, Object>> checkList = dao.getJdbcTemplate().queryForList(SQL_FIND_AGE, new Object[]{code});
            if (!CollectionUtils.isEmpty(checkList)) {
                Map<String,Object> check = checkList.get(0);
                String ageStr = (String) check.get("AGE");
                log.info("ageStr: "+ ageStr);
                age = StringUtils.isNotEmpty(ageStr)?Integer.valueOf(ageStr):0;
                log.info("ageInt: "+ age);
                if (code.startsWith("C")){
                    reportFlag = (age>=12&&age<=80)?true:false;
                    identityStatus = "2";
                }
                if (code.startsWith("W")){
                    reportFlag = (age>=6&&age<=90)?true:false;
                    identityStatus = "2";
                }
                log.info("reportFlag: "+ reportFlag);
                if (reportFlag) {
                    for (Map<String, Object> map : list) {
                        String orgPath = (String) map.get("IMGPATH");
                        String urlPath = orgPath.replaceAll(FIND_IMG_SAVE_DIR, FIND_IMG_VIEW_PREFIX);
                        log.info("预览路径 urlPath: " + urlPath);
                        imgPaths.append("<path>" + urlPath + "</path>");
                    }
                    log.info("code: " + code);
                    identityStatus = "1";
                    reportStatus = "1";
                } else {
                    message = MSG_ERR_AGE;
                }
            } else {
               log.info("根据code["+code+"]没有在 ERP_CUSTOMER_TEMP_WUCHUANG 表中找到相关客户信息！ ");
            }
        } else {
            log.info("预览地址数量为: 0");
        }
        log.info("message: "+ message);
        String respXml = XML_FIND_IMG_HEAD
                + "<code>"+code+"</code>"
                + "<identityStatus>"+identityStatus+"</identityStatus>"
                + "<imgPaths>"+imgPaths.toString()+"</imgPaths>"
                + "<reportStatus>"+reportStatus+"</reportStatus>"
                + "<message>"+message+"</message>"
                + XML_FIND_IMG_TAIL;
        log.info("respXml: "+respXml);
        return respXml;
    }
    
    /**
     * 生成 查询用户jpg通用 XML
     * @param params
     * @return
     * @author LeslieTong
     * @date 2017-4-25下午5:02:37
     */
    public String assembleReportInfo(Map<String, String> params){
    	Logger log = Logger.getLogger("assembleReportInfo");
    	//未匹配客户信息
    	String notHaveReport = XML_FIND_IMG_REPORT_ALL_HEAD + "<identityStatus>0</identityStatus>"
      			 +"<reportInfos></reportInfos>"
      			 +XML_FIND_IMG_REPORT_ALL_TAIL;
    	
    	StringBuilder findCustomerSql = new StringBuilder(SQL_FIND_CUSTOMER_INFO);
    	
    	StringBuilder xmlBuilder = new StringBuilder(XML_FIND_IMG_REPORT_ALL_HEAD);	//用于存放ErpCustomer匹配成功后拼接XML信息
    	xmlBuilder.append("<identityStatus>1</identityStatus><reportInfos>");
    	
    	List<Map<String, Object>> customerList = null;
    	String respXml = "";
    	String name = " AND C.NAME = ";
    	String phone = " AND C.PHONE = ";
    	String idno = " AND C.IDNO = ";
    	
    	if (!CollectionUtils.isEmpty(params)){
    		
    		 findCustomerSql.append(name +"'"+ params.get(ErpCustomer.F_NAME)+"' ");
    		
    		 if (params.containsKey(ErpCustomer.F_PHONE)||params.containsKey(ErpCustomer.F_IDNO)){
                 //SQL
                 if (params.containsKey(ErpCustomer.F_PHONE)&&StringUtils.isNotEmpty(params.get(ErpCustomer.F_PHONE))) {
                	 
                	 findCustomerSql.append(phone +"'"+ params.get(ErpCustomer.F_PHONE) + "' ");
                	 
                 } 
                 if (params.containsKey(ErpCustomer.F_IDNO)&&StringUtils.isNotEmpty(params.get(ErpCustomer.F_IDNO))) {
                	 
                	 findCustomerSql.append(idno +"'"+ params.get(ErpCustomer.F_IDNO) + "' ");
                	 
                 }
                 
                 //获取ErpCustomer
                 customerList = erpCustomerDao.getJdbcTemplate().queryForList(findCustomerSql.toString());
                 
                 if(customerList!=null&&!customerList.isEmpty()){		//判断是否能在Erp_customer找到
                	 
                	for (Map<String, Object> map : customerList) {
                		 this.assembleXML(xmlBuilder, map);
					}
                	
                	xmlBuilder.append("</reportInfos>");
                	xmlBuilder.append(XML_FIND_IMG_REPORT_ALL_TAIL);
                	log.info("assembleReportInfo complete xml -- "+xmlBuilder.toString());
                	respXml = xmlBuilder.toString();
                 }else{
                	 //未找到信息返回XML
                	 respXml = notHaveReport;
                 }
                 
    		 } else {
    			//未找到信息返回XML
            	 respXml = notHaveReport;
            	 
                 log = logger;
                 log.info("没有查询所需的参数！");
             }
    		
    	} else {
    		//未找到信息返回XML
       	 	respXml = notHaveReport;
    		
            log = logger;
            log.info("空的查询参数！");
        }
    	
    	return respXml;
    }
    
    /**
     * 组装reportInfos部分的XML
     * @param unfinishedXML
     * @param map
     * @param log
     * @author LeslieTong
     * @date 2017-4-25下午4:58:59
     */
    public void assembleXML(StringBuilder unfinishedXML,Map<String, Object> map){
    	Logger log = Logger.getLogger("assembleReportInfo");
    	boolean flag = false;
    	List<Map<String, Object>> imgTaskList = null;		//存放imgTsak
    	List<Map<String, Object>> imgInfoList = null;		//存放imgInfo（转换后的jpg文件集合）
    	Map<String, Object> contentMap = null;
    	StringBuilder pdfListXMLBuilder = new StringBuilder("<pdfList>");	//存放pdf转jpg文件路径集合
    	String pdfListXMLEnd = "</pdfList>";
    	StringBuilder xmlTemp = new StringBuilder("<reportInfo>");
    	
		//0：未出报告，未转jpg；
    	//1：已出报告，已转jpg；
    	//2：已出报告，未转jpg
    	String reportState = "";
    	
    	//wuchuang	无创
    	//weici		微磁
    	//gene		基因
    	String testCompany = "";							//存放 检测公司
    	
    	String cus_id = (String) map.get(ErpCustomer.F_ID);
    	String cus_code = (String) map.get(ErpCustomer.F_CODE);
   	 	String cus_name = (String) map.get(ErpCustomer.F_NAME);
   	 	Date cus_simplingDate = (Date) map.get(ErpCustomer.F_SAMPLINGDATE);
   	 	String cus_setmealName = (String) map.get(ErpCustomer.F_SETMEALNAME);
   	 	String cus_pdfFilePath = (String) map.get(ErpCustomer.F_PDFFILEPATH);	//用于判断是否出报告
   	 	String cus_idno= (String) map.get(ErpCustomer.F_IDNO);
   	 	String cus_phone = (String) map.get(ErpCustomer.F_PHONE);
   	 	
   		xmlTemp.append("<code>"+cus_code+"</code>");
   		xmlTemp.append("<name>"+cus_name+"</name>");
   		xmlTemp.append("<setmeal_name>"+cus_setmealName+"</setmeal_name>");
   		xmlTemp.append("<sampling_date>"+cus_simplingDate+"</sampling_date>");
   	 	
   		
		//判断条形码是哪个检测公司
		if (cus_code.startsWith("W") ){		//无创微磁
			testCompany = WU_CHUANG;
		}else if (cus_code.startsWith("C")) {
			testCompany = WEI_CI;
		}else{
			testCompany = GENE;
		}
		
		//判断是否有报告
		flag = this.judgeReport(cus_pdfFilePath);
		
		log.info(cus_name+"，"+cus_code+"是否有报告: "+flag);
		
		if(flag){
			
			//获取imgTask的ID
			imgTaskList = dao.getJdbcTemplate().queryForList(SQL_FIND_IMGTASK,new Object[]{cus_code,cus_name});
			
			String imgTaskId = this.judgeImgTask(imgTaskList);
			
			if(StringUtils.isNotEmpty(imgTaskId)){
				log.info(cus_name+"，"+cus_code+"有报告，imgTask的Id："+imgTaskId);
				
				imgInfoList = dao.getJdbcTemplate().queryForList(SQL_FIND_IMGINFO,new Object[]{imgTaskId});
				
				if(!CollectionUtils.isEmpty(imgInfoList)&&imgInfoList.size()>0){		//判断是否有转换完成的jpg文件
					log.info("被替换和替换的路径 -- FIND_IMG_SAVE_DIR:"+FIND_IMG_SAVE_DIR+",FIND_IMG_VIEW_PREFIX:"+FIND_IMG_VIEW_PREFIX);
					
					String tempJpgTemp = "";
					for (Map<String, Object> imgInfoMap : imgInfoList) {
						String jpgPath = (String) imgInfoMap.get("imgPath");
						log.info("jpg替换前路径 jpgPath: " + jpgPath);
						String urlPath = jpgPath.replaceAll(FIND_IMG_SAVE_DIR, FIND_IMG_VIEW_PREFIX);
						log.info("jpg替换后的预览路径 urlPath: " + urlPath);
						tempJpgTemp = tempJpgTemp + "<pdfPath>" + urlPath + "</pdfPath>";
					}
					
					log.info("【无创微磁基因】 imgInfo List xml string -- "+tempJpgTemp);
					pdfListXMLBuilder.append(tempJpgTemp);
					
					reportState = "1";
				}else{
					//修改imgTask 的状态为 "未转换" 状态（有可能出现状态为"已转换"，实际没转换）
					reportState = "2";
					
					ErpReportdetailImgtask imgtask = dao.getHibernateTemplate().get(ErpReportdetailImgtask.class, imgTaskId);
					imgtask.setState(0);
					dao.update(imgtask);
				}
				
			}else{
				
				log.info(cus_name+"，"+cus_code+"没有imgTask_id。公司为： " + testCompany);
				
				/*
				 * 获取的imgTaskList state条件为 --> "已转换"
				 * 【无创微磁】没有imgTask不需要入库
				 * 【基因】没有imgTask需要入库,要判断是否存在state为 --> "未转换"的信息
				 */
				if(testCompany.equals(GENE)){
					
					//判断是否重复
					ErpReportdetailImgtask imgtask = dao.getImgTaskByCodeAndName(cus_code,cus_name);
					if(imgtask==null){
						log.info("【基因】需要入库imgTask表： "+cus_code+","+cus_name);
						//pdfContent的文件路径
						List<Map<String, Object>> pdfContentList = dao.getJdbcTemplate().queryForList(SQL_PDFCONTENT_INFO, new Object[]{cus_id});
						if(pdfContentList!=null&&pdfContentList.size()>0){									//有报告
							contentMap = pdfContentList.get(0);
				   	 		String pdfName = (String) contentMap.get("PDFNAME");
				   	 		String filePath = (String) contentMap.get("FILEPATH");
				   	 		String batchNo = (String) contentMap.get("PRINTBTHNO");								//不为空,有报告
				   	 		
				   	 		//此处需要保存基因的信息到imgTask表中，把报告转为jpg
							ErpReportdetailImgtask saveImgtask = new ErpReportdetailImgtask();
							saveImgtask.setCustomerId(cus_id);
							saveImgtask.setUserName(cus_name);
							saveImgtask.setCode(cus_code);
							saveImgtask.setIdNo(cus_idno);
							saveImgtask.setPhoneNo(cus_phone);
							saveImgtask.setPdfName(pdfName);
							saveImgtask.setFilePath(filePath);
							saveImgtask.setBatchNo(batchNo);
							saveImgtask.setCreateTime(new Date());
							saveImgtask.setState(0);
							saveImgtask.setIsDeleted(0);
							
							dao.save(saveImgtask);
						}else{
							log.info(cus_code+","+cus_name+" save失败，未在pdfContent表找到pdf报告信息！");
						}
					}else{
						log.info(cus_code+","+cus_name+",已经存在ErpReportdetailImgtask！");
					}
					
				}
				
				reportState = "2";
				
			}
			
		}else{
			//没有报告，没有img
			reportState = "0";
		}
		
		pdfListXMLBuilder.append(pdfListXMLEnd);
		
		xmlTemp.append("<reportStatus>"+reportState+"</reportStatus>");
		xmlTemp.append(pdfListXMLBuilder.toString());
    	xmlTemp.append("</reportInfo>");
    	
    	log.info("[assembleXML()]当次reportInfo："+xmlTemp.toString());
    	
    	unfinishedXML.append(xmlTemp);
    	log.info("[assembleXML()]完成判断后的XML："+unfinishedXML.toString());
    }
    
    /**
     * 判断是否有报告
     * @param pdfContentList
     * @return String
     * @author LeslieTong
     * @date 2017-4-21下午5:15:13
     */
    private boolean judgeReport(String filePath){
    	boolean flag = false;
    	if(StringUtils.isNotEmpty(filePath)){
    		flag = true;
   	 	}else{
   	 		flag = false;
   	 	}
    	return flag;
    }
    
    /**
     * 判断imgTask的id
     * @param imgTaskList
     * @return
     * @author LeslieTong
     * @date 2017-4-24下午5:05:52
     */
    private String judgeImgTask(List<Map<String, Object>> imgTaskList){
    	String imgTaskId = "";
    	if(!CollectionUtils.isEmpty(imgTaskList)&&imgTaskList.size()>0){
    		imgTaskId = (String) imgTaskList.get(0).get("id");
    	}
    	return imgTaskId;
    }

}
