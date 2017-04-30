package org.hpin.webservice.dao;/**
 * Created by admin on 2016/11/29.
 */

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.jz.ErpReportScheduleJY;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 定时获取报告任务表(金域)DAO
 *
 * @author YoumingDeng
 * @create 2016-11-29 21:06
 */
@Repository
public class ErpReportScheduleJYDao extends BaseDao {

    public List<ErpReportScheduleJY> listScheduleJobByProps(Map<String, String> params, boolean isExact) {
        List<ErpReportScheduleJY> list = null;
        Session session = null;
        Criteria criteria = null;
        if (!CollectionUtils.isEmpty(params)) {
            session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            criteria = session.createCriteria(ErpReportScheduleJY.class);

            for (String key : params.keySet()) {
                String value = params.get(key);
                System.out.println(key + " : " + value);
                if (key.equalsIgnoreCase(ErpReportScheduleJY.F_ID)) {
                    String[] idArr = null;
                    if (value.indexOf(",") != -1) {
                        idArr = value.split(",");
                    } else {
                        idArr = new String[1];
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(ErpReportScheduleJY.F_ID, idArr));
                }else if(ErpReportScheduleJY.F_STATUS.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpReportScheduleJY.F_STATUS, Integer.valueOf(value)));
                }else  if(ErpReportScheduleJY.F_ISDELETED.equalsIgnoreCase(key)){
                    criteria.add(Restrictions.eq(ErpReportScheduleJY.F_ISDELETED, Integer.valueOf(value)));
                }else{
                    if (isExact) {
                        criteria.add(Restrictions.eq(key, value));
                    } else {
                        criteria.add(Restrictions.like(key, value, MatchMode.ANYWHERE));
                    }
                }
            }
            list = criteria.list();
        }
        return list;
    }

    /**
     * 根据条件精确查询
     * @param params 查询条件
     * @return List
     * @throws Exception
     */
    public List<ErpReportScheduleJY> listByProps(Map<String, String> params) throws Exception{
        return this.listScheduleJobByProps(params, true);
    }

    /**
     * @description 根据状态获取定时任务, condtions=="notIn"查询 not in statusArr状态的，
     *              否则默认获取所有状态的未删除数据
     * @param statusArr 状态数组
     * @param conditions 条件， not in ， 其他值则默认为 in 查询
     * @return List
     * @author YoumingDeng
     * @since: 2016/12/8 11:02
     */
    public List<ErpReportScheduleJY> listByStatus(Integer[] statusArr, String conditions)throws Exception{
        Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(ErpReportScheduleJY.class);
        //未删除
        criteria.add(Restrictions.eq(ErpReportScheduleJY.F_ISDELETED, 0));
        //状态
        if(statusArr!=null&&statusArr.length>0) {
            //条件
            if ("notIn".equalsIgnoreCase(conditions)) {
                criteria.add(Restrictions.not(Restrictions.in(ErpReportScheduleJY.F_STATUS, statusArr)));
            } else {
                criteria.add(Restrictions.in(ErpReportScheduleJY.F_STATUS, statusArr));
            }
        }
        return criteria.list();
    }

    /**
     * 逻辑删除定时任务中的数据
     * @param code 条码
     * @param name 姓名
     * @return boolean
     */
    public boolean cleanOld(String code, String name){
        boolean flag = false;
        if(StringUtils.isNotEmpty(code)&&StringUtils.isNotEmpty(name)){
            String sql = " update ERP_REPORT_SCHEDULE_JY t set t.is_deleted = 1 where t.code = ? and t.name = ? and t.is_deleted <> 1 ";
            this.getJdbcTemplate().update(sql, new String[]{code, name});
            flag = true;
        }
        return flag;
    }
}
