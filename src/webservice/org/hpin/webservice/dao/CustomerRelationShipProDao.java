package org.hpin.webservice.dao;
/**
 * Created by admin on 2016/12/6.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.CustomerRelationShipPro;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 支公司与项目关系表 DAO
 *
 * @author YoumingDeng
 * @since 2016-12-06 14:01
 */
@Repository
public class CustomerRelationShipProDao extends BaseDao{
	
	/**
	 * 通过项目的id来查询对应的套餐信息;
	 * @param shipId
	 * @return
	 */
	public List<Map<String, Object>> findComboListByShipId(String shipId) {
		String querySql = "select "+
		"jyCombo.id comboId,  "+
		"jyCombo.COMBO_NAME comboName,  "+
		"proCombo.COMBO_SHOW_NAME comboDisName, "+
		"jyCombo.PROJECT_TYPES comboType "+
		"from erp_relationshippro_combo proCombo "+
		"left join HL_CUSTOMER_RELATIONSHIP_PRO pro on pro.id = proCombo.CUSTOMER_RELATIONSHIP_PRO_ID "+
		"left join HL_JY_COMBO jyCombo on jyCombo.id = proCombo.COMBO_ID "+
		"where CUSTOMER_RELATIONSHIP_PRO_ID = '" + shipId + "' and IS_USED='1' ";
		
		return this.getJdbcTemplate().queryForList(querySql);
	}
	
	/**
	 * 根据支公司id和项目类型获取对应的唯一的项目Id;
	 * create by henry.xu 20170215
	 * @param branchCompanyId
	 * @param projectType
	 * @return
	 */
	public CustomerRelationShipPro findByCompanyIdAndProjectType(String branchCompanyId, String projectType) {
		String querySql = "select pro.ID id, " +
		"pro.CUSTOMER_RELATIONSHIP_ID customerRelationShipId, " +
		"pro.PROJECT_CODE projectCode, " +
		"pro.PROJECT_NAME projectName, " +
		"pro.PROJECT_OWNER projectOwner, " +
		"pro.PROJECT_TYPE projectType, " +
		"pro.LINK_NAME linkName, " +
		"pro.LINK_TEL linkTel, " +
		"pro.REMARK remark, " +
		"pro.MAIL_ADDRESS mailAddress, " +
		"pro.RECEPTION reception, " +
		"pro.RECEPTION_TEL receptionTel " +
		" from HL_CUSTOMER_RELATIONSHIP_PRO pro " +
		"left join T_PROJECT_TYPE pt on pt.id = pro.Project_type " +
		"where " + 
		"pro.CUSTOMER_RELATIONSHIP_ID = '" + branchCompanyId + "' " +
		"and pro.IS_SEAL = '0' " +
		"and pro.IS_DELETED = '0' " +
		"and pt.project_type = '" + projectType + "'";
		BeanPropertyRowMapper<CustomerRelationShipPro> rowMapper = new BeanPropertyRowMapper<CustomerRelationShipPro>(CustomerRelationShipPro.class);
		List<CustomerRelationShipPro> result = this.getJdbcTemplate().query(querySql, rowMapper);
		
		return result != null && result.size() > 0 ? result.get(0) : null;
	}

    public List<CustomerRelationShipPro> listByProps(Map<String,String> params, boolean isExact) throws Exception{
        List<CustomerRelationShipPro> list = null;
        if(params!=null&&!params.isEmpty()){
            Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            Criteria criteria = session.createCriteria(CustomerRelationShipPro.class);
            Iterator<String> iter = params.keySet().iterator();
            while (iter.hasNext()){
                String key = iter.next();
                String value = params.get(key);
                if(key.equalsIgnoreCase(CustomerRelationShipPro.F_ID)){
                    String[] idArr = null ;
                    if(value.indexOf(",")!=-1){
                        int n = value.split(",").length;
                        idArr = new String[n] ;
                        idArr = value.split(",");
                    }else{
                        idArr = new String[1] ;
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(CustomerRelationShipPro.F_ID, idArr));
                }else{
                    if(isExact){
                        criteria.add(Restrictions.eq(key, value));
                    }else{
                        criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                    }
                }
            }
            list = criteria.list();
        }
        return list;
    }
    /**
     * @description
     * @author YoumingDeng
     * @since: 2016/12/6 20:08
     */
    public List<CustomerRelationShipPro> listByProps(Map<String,String> params) throws Exception{
        return this.listByProps(params, true);
    }
    
    
}
