package org.hpin.webservice.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.hk.ErpPreCustomer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 弘康客户信息预存表的DAO
 * Created by Damian on 16-12-28.
 */
@Repository
public class ErpPreCustomerDao extends BaseDao{

    /**
     * 根据条件查询
     * @param params 传入的Map键值对
     * @param isExact 是否精确查找（只对字符串类型有效，ID为in查找，不用like）
     * @return List ErpPreCustomer对象集
     * @author DengYouming
     * @since 2016-8-18 上午11:57:59
     */
    public List<ErpPreCustomer> listByProps(Map<String,String> params, boolean isExact){
        List<ErpPreCustomer> list = null;
        Session session;
        Criteria criteria;
        if(!CollectionUtils.isEmpty(params)){
            session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            criteria = session.createCriteria(ErpPreCustomer.class);

            for (String key : params.keySet()) {
                String value = params.get(key);
                if(key.equalsIgnoreCase(ErpPreCustomer.F_ID)){
                    String[] idArr;
                    if(value.indexOf(",")!=-1){
                        idArr = value.split(",");
                    }else{
                        idArr = new String[1] ;
                        idArr[0] = value;
                    }
                    criteria.add(Restrictions.in(ErpPreCustomer.F_ID, idArr));
                }
                else{
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
     * @param params 传入的参数
     * @return List
     * @author YoumingDeng
     * @since: 2016/12/6 14:51
     */
    public List<ErpPreCustomer> listByProps(Map<String,String> params) throws Exception{
        List<ErpPreCustomer> list = null;
        if(!CollectionUtils.isEmpty(params)){
            list = this.listByProps(params, true);
        }
        return list;
    }

    /**
     * 根据姓名，电话，身份证号码查找预导入表
     * @param name 姓名
     * @param phone 电话
     * @param idCard 身份证号
     * @return List
     * @throws Exception
     */
    public List<ErpPreCustomer> find(String name, String phone, String idCard) throws Exception{
//        String sql = "  select t.* from erp_pre_customer t " +
//                "where t.IS_DELETED = 0 and t.were_name=? and t.WERE_PHONE = ? and t.WERE_IDCARD = ? ";
//        return this.getJdbcTemplate().query(sql, new String[]{name, phone, idCard},
//                new BeanPropertyRowMapper<ErpPreCustomer>(ErpPreCustomer.class));
        Map<String,String> params = new HashMap<String, String>();
        params.put(ErpPreCustomer.F_WERENAME, name);
        params.put(ErpPreCustomer.F_WEREPHONE, phone);
        params.put(ErpPreCustomer.F_WEREIDCARD, idCard);
        return this.listByProps(params);
    }

    /**
     * 根据条件查找预导入表信息(用于 华夏，易安，天津邮政 业务)
     * @param name 姓名
     * @param phone 电话
     * @param idCard 身份证号
     * @param branchCompanyId 支公司ID
     * @return List
     * @throws Exception
     */
    public List<ErpPreCustomer> findForToc(String name, String phone, String idCard, String branchCompanyId) throws Exception{
//        String sql = "  select t.* from erp_pre_customer t " +
//                " where t.IS_DELETED = 0 and t.were_name=? and t.WERE_PHONE = ? and t.WERE_IDCARD = ? and t.COMPANY_ID = ? ";
//        return this.getJdbcTemplate().query(sql, new String[]{name, phone, idCard, branchCompanyId},
//                new BeanPropertyRowMapper<ErpPreCustomer>(ErpPreCustomer.class));
        Map<String,String> params = new HashMap<String, String>();
        params.put(ErpPreCustomer.F_WERENAME, name);
        params.put(ErpPreCustomer.F_WEREPHONE, phone);
        params.put(ErpPreCustomer.F_WEREIDCARD, idCard);
        params.put("companyId",branchCompanyId);
        return this.listByProps(params);
    }

    /**
     * 根据外部业务传送的套餐名称查询对应的远盟套餐
     * @param foreignCombo 套餐名称
     * @return 远盟套餐
     * @throws Exception
     */
    public String findYmCombo(String foreignCombo) throws Exception{
        String ymCombo = null;
        if (StringUtils.isNotEmpty(foreignCombo)) {
            String sql = " select YM_COMBONAME from ERP_YMCOMBONAME_TESTCOMBONAME where TEST_COMBONAME = ? ";
            SqlRowSet rowSet = this.getJdbcTemplate().queryForRowSet(sql, new String[]{foreignCombo});
            while (rowSet.next()){
                ymCombo = rowSet.getString(1);
            }
        }
        return ymCombo;
    }

    /**
     * 更新
     * @param entity
     * @return int
     * @throws Exception
     */
    public int updateEntity(ErpPreCustomer entity) throws Exception{
        int num = 0;
        if (entity!=null) {
            Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
            session.update(entity);
            num = 1;
        }
        return num;
    }
}
