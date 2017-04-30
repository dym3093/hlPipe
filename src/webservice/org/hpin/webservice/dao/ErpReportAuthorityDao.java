package org.hpin.webservice.dao;/**
 * Created by admin on 2016/12/6.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpReportAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 报告权限表DAO
 *
 * @author YoumingDeng
 * @since 2016-12-06 15:57
 */
@Repository
public class ErpReportAuthorityDao extends BaseDao{


    /**
     * @description 根据条件精确查询信息
     * @param params 条件
     * @return List
     * @author YoumingDeng
     * @since: 2016/12/6 16:05
     */
    public List<ErpReportAuthority> listByProps(Map<String,String> params) throws Exception{
        List<ErpReportAuthority> list = null;
        if(!CollectionUtils.isEmpty(params)){
            list = this.listByProps(params, true);
        }
        return  list;
    }

    /**
     * @description 根据条件是否精确查询信息
     * @param params 条件
     * @param isExact 是否精确查询
     * @return List
     * @author YoumingDeng
     * @since: 2016/12/6 16:02
     */
    public List<ErpReportAuthority> listByProps(Map<String,String> params, boolean isExact) throws Exception{
        List<ErpReportAuthority> list = null;
        Session session = null;
        Criteria criteria = null;
        if(!CollectionUtils.isEmpty(params)){
            session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            criteria = session.createCriteria(ErpReportAuthority.class);
            for (String key : params.keySet()) {
                String value = params.get(key);
                if(key.equalsIgnoreCase(ErpReportAuthority.F_ID)){
                    String[] idArr = null ;
                    if(value.indexOf(",")!=-1){
                        int n = value.split(",").length;
                        idArr = new String[n] ;
                        idArr = value.split(",");
                    }else{
                        idArr = new String[1] ;
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(ErpReportAuthority.F_ID, idArr));
                }else if(key.equalsIgnoreCase(ErpReportAuthority.F_ISDELETED)){
                    criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
                }else if(ErpReportAuthority.F_STATUS.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpReportAuthority.F_STATUS, Integer.valueOf(value)));
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
}
