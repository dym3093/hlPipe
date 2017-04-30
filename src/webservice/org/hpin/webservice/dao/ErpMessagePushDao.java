package org.hpin.webservice.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpMessagePush;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 消息推送表DAO
 * Created by Damian on 17-1-1.
 */
@Repository
public class ErpMessagePushDao extends BaseDao{

    /**
     * 根据条件是否严格查找
     * @param params 条件集合
     * @param isExact true:equal查找，false:like查找
     * @return List
     */
    public List<ErpMessagePush> listByProps(Map<String,String> params, boolean isExact) throws Exception{
        List<ErpMessagePush> list = null;
        Session session;
        Criteria criteria;
        if(!CollectionUtils.isEmpty(params)){
            session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            criteria = session.createCriteria(ErpMessagePush.class);

            for (String key : params.keySet()) {
                String value = params.get(key);
                System.out.println(key+" : "+value);
                if(key.equalsIgnoreCase(ErpMessagePush.F_ID)){
                    String[] idArr;
                    if(value.indexOf(",")!=-1){
                        idArr = value.split(",");
                    }else{
                        idArr = new String[1] ;
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(ErpMessagePush.F_ID, idArr));
                }else if (ErpMessagePush.F_STATUS.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpMessagePush.F_STATUS, Integer.valueOf(value)));
                } else {
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
     * 根据条件严格查找
     * @param params 条件集合
     * @return List
     */
    public List<ErpMessagePush> listByProps(Map<String,String> params) throws Exception{
        return this.listByProps(params,true);
    }



}
