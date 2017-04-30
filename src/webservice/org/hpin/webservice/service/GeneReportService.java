package org.hpin.webservice.service;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.common.util.StaticMethod;
import org.hpin.webservice.bean.LogInfo;
import org.hpin.webservice.bean.Response;
import org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImpl;
import org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service(value = "org.hpin.webservice.service.GeneReportService")
@Transactional()
public class GeneReportService extends BaseService{
	@Autowired
	private org.hpin.webservice.dao.GeneReportDao dao = null;
	
	public Response getCheckResponse(String idcard,String name, String tel){
			return dao.getCheckResponse(idcard, name, tel);
	}
	public boolean checkParam(String idcard,String name, String tel){
		boolean flag=true;
		if("".equals(idcard) || "".equals(name)){
			flag=false;
		}
		return flag;
	}	
	public LogInfo getLogInfo(String idcard,String name, String tel,Response response){
		LogInfo loginfo=new LogInfo();
		loginfo.setName(name);
		loginfo.setIdcard(idcard);
		loginfo.setTel(tel);
		//添加判空处理	add by YoumingDeng 2016-12-13
		if(response!=null) {
			loginfo.setCode(response.getCode());
			loginfo.setIdentityStatus(response.getIdentityStatus());
			loginfo.setReportStatus(response.getReportStatus());
			loginfo.setPdfPath(response.getPdfPath());
		}
		loginfo.setCreateTime(new Date());
		return  loginfo;
	}
	public String getRetmsg(String retcode){
		String retmsg="";
		if("1".equals(retcode)){
			retmsg="成功";
		}else if("2".equals(retcode)){
			retmsg="客户重复";
		}else{
			retmsg="失败";
		}			
		return retmsg;
	}
	public void pushGeneNewPdfToWpt(){
		Logger logger = Logger.getLogger("pushGeneNewPdfToWpt");
		logger.info("pushGeneNewPdfToWpt job  begin-----");
		Map map=null;		
		String id;
		String phone;
		String code;
		String pdfPath;
		String pdfDate;
		String retStatus="";
		String sql = "SELECT * FROM (select * from hl_pushwpt_pdfpool  where  status in(0,8)  order by createdate)  WHERE ROWNUM<=2000";
		List list = dao.getJdbcTemplate().queryForList(sql);
		int size=list.size();
		logger.info("list size---"+size);		
		String updatesql="update hl_pushwpt_pdfpool set status=? where id=?";
		
		YMKJWserviceImpl service=null;
		if(size>0){
	     try {
		    service= YMKJWserviceImplServiceLocator.getInstance().getYMKJWserviceImplPort();		   
		  }catch (ServiceException e) {
			// TODO Auto-generated catch block
			    e.printStackTrace();
			    retStatus="7";
		  }
		}
		for(int i=0;i<list.size();i++){
			 map=(Map)list.get(i);
			 id=StaticMethod.nullObject2String(map.get("ID"));					 			
		     phone=StaticMethod.nullObject2String(map.get("PHONE"));
		     code=StaticMethod.nullObject2String(map.get("CODE"));
			 pdfPath=StaticMethod.nullObject2String(map.get("PDFFILEPATH"));
			 pdfDate=StaticMethod.nullObject2String(map.get("CREATEDATE"));		
		  if(service!=null){			 
				 //调用微平台接口
				  long startTime = 0;
				  long endTime = 0;
				 try {					 
					startTime = new Date().getTime();
					//YMKJWserviceImpl service= YMKJWserviceImplServiceLocator.getInstance().getYMKJWserviceImplPort();
					retStatus=service.pushPdfInfo(getGeneWptXml(phone,code,pdfPath,pdfDate));				
				    //0失败，1成功，2 重复
				 }catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						retStatus="8";
				 }
				 endTime = new Date().getTime();
				 logger.info("pdf back code:"+code+",retStatus:"+retStatus+",times:"+(endTime - startTime));
			
			     
		   }else{
			     logger.info("link wrong retStatus:"+retStatus);
			     
		   }
		   dao.getJdbcTemplate().update(updatesql, new Object[]{retStatus,id}); //不能调用则更新状态2 
		}
		logger.info("pushGeneNewPdfToWpt job  end-----");
	}
    public String getGeneWptXml(String phone,String code,String pdfPath,String pdfDate){
		StringBuffer sbf=new StringBuffer();
		sbf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sbf.append("<pdfInfo>");
		sbf.append("<phone>"+phone+"</phone>");
		sbf.append("<code>"+code+"</code>");
		sbf.append("<pdfPath>"+pdfPath+"</pdfPath>");
		sbf.append("<pdfDate>"+pdfDate+"</pdfDate>");
		sbf.append("</pdfInfo>");
		return sbf.toString();
	}
}
