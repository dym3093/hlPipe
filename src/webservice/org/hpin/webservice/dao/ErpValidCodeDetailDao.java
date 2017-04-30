package org.hpin.webservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.springframework.stereotype.Repository;


/**
 * 天津邮政业务dao层
 * @author machuan
 * @date 2017年1月17日
 */
@Repository
public class ErpValidCodeDetailDao extends BaseDao{

   
    /**
     * 根据检测码查询ERP_VALID_CODE_DETAIL中是否有未使用的
     * @param idNum
     * @return
     * @throws Exception
     * @author machuan
     * @date  2017年1月17日
     */
    public List<Object> find(String idNum) throws Exception{
    	Logger log = Logger.getLogger("getIDAuth");
    	List<Object> list = new ArrayList<Object>();
    	String msg = "";
    	//判断是否有匹配成功的检测码  true为匹配成功
    	boolean flag = false;
    	//判断检测码是否已被使用 true为已被使用
    	boolean isUsed = true;
    	String branchId = "";
    	String projectId = "";
    	log.info("ErpValidCodeDetailDao -- find : idNum=="+idNum);
    	if(StringUtils.isNotBlank(idNum)){
    		String sql = "select d.valid_code,d.is_used,d.branch_id,d.project_id from erp_valid_code_detail d where d.valid_code ='"+idNum+"' and d.is_deleted=0";
    		List<Map<String, Object>> result = this.getJdbcTemplate().queryForList(sql);
    		//查询的结果不为空进行循环判断
    		if(result!=null&&result.size()>0){
    			log.info("ErpValidCodeDetailDao -- find :result.size =="+result.size());
    			for(Map<String, Object> map : result){
    				if(StringUtils.isBlank((String) map.get("is_used"))||"0".equals(map.get("is_used"))){
    					flag = true;
    					msg="绑定成功。";
    					isUsed = false;
    					branchId = (String) map.get("branch_id");
    					projectId = (String) map.get("project_id");
    				}
    			}
    			if(isUsed){
    	    		msg = "您好，您已成功绑定，请勿重复绑定。";
    	    	}
    		}else{
    			msg = "您好，未查到您的信息，请核对信息后再提交，谢谢。";
    		}
    	}else{
    		msg = "您好，未查到您的信息，请核对信息后再提交，谢谢。";
    	}
    	list.add(flag);
    	list.add(msg);
    	list.add(branchId);
    	list.add(projectId);
    	return list;
    }

	/**
	 * 根据项目ID 找到所有的套餐名
	 * @param projectId
	 * @return
	 * @author machuan
	 * @date  2017年1月18日
	 */
	public String getMealNameByProId(String projectId,String project) {
		String comboName = "";
		//20170328修改需求  从erp_relationshippro_combo查询套餐
		String sql = "select jc.combo_name from erp_relationshippro_combo rc "
					+"left join hl_jy_combo jc on rc.combo_id=jc.id "
					+"where rc.customer_relationship_pro_id='"
					+projectId
					+ "' and jc.is_delete=0";
//		String sql = "select t.test_comboname from erp_ymcomboname_testcomboname t where t.project='"
//					+project
//					+"' and t.ym_comboname in (select c.combo_name from hl_jy_combo c where c.id in"
//					+"(select p.combo_id from erp_relationshippro_combo p where p.customer_relationship_pro_id='"
//					+projectId
//					+ "'" 
//					+"and (p.is_used is null or p.is_used='1')) and c.is_delete='0')";
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
		if(list!=null&&list.size()>0){
			for(Map<String, Object> map :list){
				comboName+="+"+map.get("combo_name");
			}
		}
		if(StringUtils.isNotBlank(comboName)){
			comboName = comboName.substring(1);
		}
		return comboName;
	}

	/**
	 * 根据项目id或者项目
	 * @param projectId
	 * @return
	 * @author machuan
	 * @date  2017年1月19日
	 */
	public CustomerRelationShipPro findShipProById(String projectId) {
		String sql = "from CustomerRelationShipPro r where id=?";
		return (CustomerRelationShipPro) (this.getHibernateTemplate().find(sql, projectId).get(0));
	}

	/**
	 * 将该检测码的状态置为1
	 * @param idNum
	 * @author machuan
	 * @date  2017年1月19日
	 */
	public void editIsUsedStatus(String idNum) {
		String sql = "update ERP_VALID_CODE_DETAIL set is_used='1' where valid_code='"+idNum+"' and (is_used='0' or is_used is null)";
		this.getJdbcTemplate().update(sql);
	}

}
