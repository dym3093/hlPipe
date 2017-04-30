
package org.hpin.webservice.service.yg;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.yg.ErpBxCompanyPreSet;
import org.hpin.webservice.dao.yg.ErpBxcompanyPreSetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author machuan
 * @date 2017年2月4日
 */
@Service(value = "org.hpin.webservice.service.yg.ErpBxCompanyPreSetService")
@Transactional
public class ErpBxCompanyPreSetService extends BaseService{

	@Autowired
	private ErpBxcompanyPreSetDao dao;

	/**
	 * @param filterMap
	 * @return
	 * @author machuan
	 * @date  2017年2月4日
	 */
	public List<ErpBxCompanyPreSet> getComboInfo(Map<String, String> filterMap) {
		return dao.getComboInfo(filterMap);
	}

	/**
	 * 根据套餐名  申请单号  支公司ID 查询套餐显示名
	 * @param comboName
	 * @param applicationNo
	 * @param customerRelationshipId
	 * @return
	 * @author machuan
	 * @date  2017年2月8日
	 */
	public String getComboShowNameByParam(String comboName, String applicationNo, String customerRelationshipId) {
		Logger log = Logger.getLogger("getComboInfo");
		log.info("comboName:"+comboName+",applicationNo:"+applicationNo+",customerRelationshipId"+customerRelationshipId);
		String comboShowName = "";
		try{
			String sql = "select c.combo_show_name from erp_relationshippro_combo c "
					+"left join hl_customer_relationship_pro p on c.customer_relationship_pro_id=p.id and p.is_seal='0' "
					+"left join erp_application a on a.banny_company_id=p.customer_relationship_id and a.project_code = p.project_code "
					+"left join hl_jy_combo j on j.id=c.combo_id and j.is_delete='0'  "
					+"where (c.is_used='1' or c.is_used is null) and j.combo_name=?  "
					+"and a.application_no=? and a.banny_company_id=?";
			List<Map<String, Object>> list = dao.getJdbcTemplate().queryForList(sql, comboName,applicationNo,customerRelationshipId);
			if(list!=null&&list.size()>0){
				comboShowName = (String) list.get(0).get("combo_show_name");
			}
		}catch(Exception e){
			log.error("getComboShowNameByParam--error: ",e);
		}
		return comboShowName;
	}
	
}
