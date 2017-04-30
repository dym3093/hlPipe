package org.hpin.webservice.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpReportdetailImgtask;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 17-2-13.
 */
@Repository
public class ErpReportdetailImgtaskDao extends BaseDao{

    /**
     * 根据参数查找
     * @param name
     * @param tel
     * @param birthday
     * @return
     */
    public List<ErpReportdetailImgtask> find(String name, String tel, String birthday) throws Exception{
        List<ErpReportdetailImgtask> list = null;
        if(StringUtils.isNotEmpty(name)&&StringUtils.isNotEmpty(tel)&&StringUtils.isNotEmpty(birthday)){
            String hql = " from ErpReportdetailImgtask where isDeleted = 0 and state = 1 and userName = ? and phoneNo = ? and birthday = ? ";
            list = this.getHibernateTemplate().find(hql, new Object[]{name, tel, birthday});
        }
        return list;
    }

    /**
     * 根据传入的条件查询
     * @param params
     * @return List
     * @throws Exception
     */
    public List<ErpReportdetailImgtask> listByProps(Map<String,String> params) throws Exception{
        List<ErpReportdetailImgtask> list = null;
        if (!CollectionUtils.isEmpty(params)){
            Session session;
            Criteria criteria;
            if(!CollectionUtils.isEmpty(params)){
                session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
                criteria = session.createCriteria(ErpReportdetailImgtask.class);

                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if(key.equalsIgnoreCase(ErpReportdetailImgtask.F_ID)){
                        String[] idArr;
                        if(value.indexOf(",")!=-1){
                            idArr = value.split(",");
                        }else{
                            idArr = new String[1] ;
                            idArr[0] = value;
                        }
                        criteria.add(Restrictions.in(ErpReportdetailImgtask.F_ID, idArr));
                    } else if (key.equalsIgnoreCase(ErpReportdetailImgtask.F_STATE)
                            ||key.equalsIgnoreCase(ErpReportdetailImgtask.F_ISDELETED)){
                        criteria.add(Restrictions.eq(key, Integer.valueOf(value)));
                    } else{
                        criteria.add(Restrictions.eq(key, value));
                    }
                }
                list = criteria.list();
            }
        }
        return list;
    }
    
    /**
     * 用于判断是否重复 save ErpReportdetailImgtask(未转换的信息)
     * @param code
     * @param name
     * @return
     * @author LeslieTong
     * @date 2017-4-24下午5:22:21
     */
    public ErpReportdetailImgtask getImgTaskByCodeAndName(String code,String name){
    	ErpReportdetailImgtask erpReportdetailImgtask = null;
    	String hql = "from ErpReportdetailImgtask where isDeleted = 0 and state <> 1 and code = ? and userName = ? ";
    	List<ErpReportdetailImgtask> imgtasks = this.getHibernateTemplate().find(hql, new Object[]{code,name});
    	if(imgtasks!=null&&imgtasks.size()>0){
    		erpReportdetailImgtask = imgtasks.get(0);
    	}
    	return erpReportdetailImgtask;
    }
}
