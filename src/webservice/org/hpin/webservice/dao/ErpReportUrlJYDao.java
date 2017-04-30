package org.hpin.webservice.dao;/**
 * Created by admin on 2016/11/29.
 */

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.jz.ErpReportUrlJY;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 报告URL地址信息表(ERP_REPORT_SCHEDULE_JY的从表)Dao
 *
 * @author YoumingDeng
 * @since  2016-11-29 21:07
 */
@Repository
public class ErpReportUrlJYDao extends BaseDao{


	/**
	 * 根据条件精确查找
	 * @param params Map类型的条件
	 * @return List
	 * @throws Exception
     */
	public List<ErpReportUrlJY> listByProps(Map<String,String> params) throws Exception {
		return this.listByProps(params,true);
	}

	/**
	 * 根据传入的条件判定是否需要精确查询
	 * @param params 条件
	 * @param isExact true:精确查询， false:模糊查询
     * @return List
     */
    public List<ErpReportUrlJY> listByProps(Map<String,String> params, boolean isExact) throws Exception{
        List<ErpReportUrlJY> list = null;
		Session session;
		Criteria criteria;
		if(!CollectionUtils.isEmpty(params)){
			session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
			criteria = session.createCriteria(ErpReportUrlJY.class);
			for (String key : params.keySet()) {
				String value = params.get(key);
				System.out.println(key+" : "+value);
				if(key.equalsIgnoreCase(ErpReportUrlJY.F_ID)){
					String[] idArr;
					if(value.indexOf(",")!=-1){
						idArr = value.split(",");
					}else{
						idArr = new String[1] ;
						idArr[0] = value;
					}
					criteria.add(Restrictions.in(ErpReportUrlJY.F_ID, idArr));
				}else if(key.equalsIgnoreCase(ErpReportUrlJY.F_ISDELETED)){
					criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
				}else if(ErpReportUrlJY.F_STATUS.equalsIgnoreCase(key)){
					criteria.add(Restrictions.eq(ErpReportUrlJY.F_STATUS, Integer.valueOf(value)));
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
	 * 清除旧的URL数据
	 * @param code 条码
	 * @param name 姓名
	 * @return boolean
	 */
    public boolean cleanOldUrl(String code, String name){
    	boolean flag = false;
    	if(StringUtils.isNotEmpty(code)&&StringUtils.isNotEmpty(name)){
    		String sql = " update ERP_REPORT_URL_JY t set t.IS_DELETED = 1 where t.code= ? and t.name=? and t.is_deleted = 0 ";
    		this.getJdbcTemplate().update(sql, new String[]{code, name});
    		flag = true;
		}
    	return flag;
	}
}
