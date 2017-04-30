package org.hpin.webservice.dao;

import java.util.List;
import java.util.Map;

import org.hpin.common.core.orm.BaseDao;
import org.hpin.common.util.StaticMehtod;
import org.hpin.webservice.bean.Response;
import org.springframework.stereotype.Repository;
@Repository()
public class GeneReportDao extends BaseDao {
 /*
  * 信息完全匹配，提供条形码和报告文件路径
  * 姓名身份证号匹配，但是手机号不一致，提供条形码和报告文件路径
  * 姓名身份证号匹配，但是基因业务系统中没有手机号，提供条形码和报告文件路径
  * 姓名身份证号不匹配。什么都没有。
  */
	public Response getCheckResponse(String idcard,String name, String tel){
		String identityStatus="";
		String reportStatus="";
		String pdfPath="";
		Response response=new Response();
		String queryString1="select t.code,t.name,t.idno,t.phone,t.PDFFILEPATH from ERP_CUSTOMER t where t.idno=? and t.name=? and t.is_deleted=0";
		//String queryString2="select t.FILEPATH from erp_reportdetail t where t.code=? ";
		
		List list = this.getJdbcTemplate().queryForList(queryString1, new Object[]{idcard,name});	
		if(list!=null && list.size()>0){
			Map map=(Map)list.get(0);
			String code=StaticMehtod.nullObject2String(map.get("CODE"));
			String phone=StaticMehtod.nullObject2String(map.get("PHONE"));
			 pdfPath=StaticMehtod.nullObject2String(map.get("PDFFILEPATH"));
			response.setCode(code); //code	
			//0:身份不匹配, 1:全信息匹配，2:身份匹配手机不匹配, 3:身份匹配手机号为空
			if("".equals(tel)){
				identityStatus="1";
			}else{
			 if("".equals(phone)){
				identityStatus="3";
			 }else if(tel.equals(phone)){
				identityStatus="1";
			 }else{
				identityStatus="2";
			 }
			}
			response.setIdentityStatus(identityStatus);//identityStatus
			 if(!"".equals(pdfPath)){
		       reportStatus="1";	
		     }else{
			   reportStatus="0";
		     }
			//取pdf报告信息			
//			List list1 = this.getJdbcTemplate().queryForList(queryString2, new Object[]{code});
//			if(list1!=null && list1.size()>0){
//				Map map1=(Map)list1.get(0);
//				pdfPath=StaticMehtod.nullObject2String(map1.get("FILEPATH"));
//				if(!"".equals(pdfPath)){
//				    reportStatus="1";	
//				}else{
//					reportStatus="0";
//				}
//			}else{
//				reportStatus="0";
//			}
			response.setReportStatus(reportStatus); //reportStatus
			response.setPdfPath(pdfPath);  //pdfPath
		}else{
			response.setCode("");
			response.setIdentityStatus("0");
			response.setPdfPath("");
			response.setReportStatus("0");
		}
		return response;
	}
}
