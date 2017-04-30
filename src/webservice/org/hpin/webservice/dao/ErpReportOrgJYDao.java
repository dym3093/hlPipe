package org.hpin.webservice.dao;/**
 * Created by admin on 2016/11/28.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.jz.ErpReportOrgJY;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 金域传送的报告信息DAO
 *
 * @author YoumingDeng
 * @create 2016-11-28 14:07
 */
@Repository
public class ErpReportOrgJYDao extends BaseDao{

    /**
     * 保持原始报告信息
     * @param entity ErpReportOrgJY
     */
    public void saveReportOrgJy(ErpReportOrgJY entity){
        if(entity!=null) {
            Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            session.save(entity);
        }
    }

    public List<ErpReportOrgJY> listByProps(Map<String,String> params, boolean isExact){
        List<ErpReportOrgJY> list = null;
        Session session;
        Criteria criteria;
        if(!CollectionUtils.isEmpty(params)){
            session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            criteria = session.createCriteria(ErpReportOrgJY.class);

            for (String key : params.keySet()) {
                String value = params.get(key);
                System.out.println(key+" : "+value);
                if(key.equalsIgnoreCase(ErpReportOrgJY.F_ID)){
                    String[] idArr;
                    if(value.indexOf(",")!=-1){
                        idArr = value.split(",");
                    }else{
                        idArr = new String[1] ;
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(ErpReportOrgJY.F_ID, idArr));
                }else if (ErpReportOrgJY.F_STATUS.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpReportOrgJY.F_STATUS, Integer.valueOf(value)));
                }else if(ErpReportOrgJY.F_ISDELETED.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpReportOrgJY.F_ISDELETED, Integer.valueOf(value)));
                }else {
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

    public List<ErpReportOrgJY> listByProps(Map<String,String> params){
        return this.listByProps(params,true);
    }

}
