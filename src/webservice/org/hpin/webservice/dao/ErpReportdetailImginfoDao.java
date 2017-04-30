package org.hpin.webservice.dao;

import org.apache.commons.lang.StringUtils;
import org.hpin.common.core.orm.BaseDao;
import org.hpin.webservice.bean.ErpReportdetailImginfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 17-2-6.
 */
@Repository
public class ErpReportdetailImginfoDao extends BaseDao {


    /**
     * 根据指定信息查询客户信息报告
     *
     * @param name     姓名
     * @param tel      电话
     * @param birthday 生日
     * @return String XML字符串
     * @auther Damian
     * @since 2017-02-06
     */
    public List<ErpReportdetailImginfo> findImginfo(String name, String tel, String birthday){
        List<ErpReportdetailImginfo> list = null;
        if(StringUtils.isNotEmpty(name)&& StringUtils.isNotEmpty(tel)&& StringUtils.isNotEmpty(birthday)){
            String sql = "SELECT r.ID    AS id,\n" +
                    "  r.TASKID     AS taskId,\n" +
                    "  r.IMGPATH    AS imgPath,\n" +
                    "  r.CREATETIME AS createTime,\n" +
                    "  r.ISDELETED  AS isDeleted\n" +
                    "FROM erp_reportdetail_imginfo r\n" +
                    "WHERE 1         =1\n" +
                    "AND r.ISDELETED = 0\n" +
                    "AND r.TASKID   IN\n" +
                    "  (SELECT t.ID\n" +
                    "  FROM erp_reportdetail_imgtask t\n" +
                    "  WHERE 1         =1\n" +
                    "  AND t.ISDELETED = 0\n" +
                    "  AND t.STATE     = 1\n" +
                    "  AND t.USERNAME  = ?\n" +
                    "  AND t.PHONENO   = ?\n" +
                    "  AND t.BIRTHDAY  = ?\n" +
                    "  )\n" +
                    "ORDER BY length(r.IMGPATH), r.IMGPATH ASC ";
            list = this.getJdbcTemplate().query(sql, new Object[]{name, tel, birthday},
                    new BeanPropertyRowMapper<ErpReportdetailImginfo>(ErpReportdetailImginfo.class));
        }
        return list;
    }

    /**
     * 根据指定信息查询客户信息报告
     * @param name     姓名
     * @param tel      电话
     * @param idNo     身份证号
     * @return List
     * @auther Damian
     * @since 2017-02-10
     */
    public List<ErpReportdetailImginfo> getByReportInfoImg(String name, String tel, String idNo){
        List<ErpReportdetailImginfo> list = null;
        if(StringUtils.isNotEmpty(name)&& StringUtils.isNotEmpty(tel)&& StringUtils.isNotEmpty(idNo)){
            String sql = "SELECT r.ID    AS id,\n" +
                    "  r.TASKID     AS taskId,\n" +
                    "  r.IMGPATH    AS imgPath,\n" +
                    "  r.CREATETIME AS createTime,\n" +
                    "  r.ISDELETED  AS isDeleted\n" +
                    "FROM erp_reportdetail_imginfo r\n" +
                    "WHERE 1         =1\n" +
                    "AND r.ISDELETED = 0\n" +
                    "AND r.TASKID   IN\n" +
                    "  (SELECT t.ID\n" +
                    "  FROM erp_reportdetail_imgtask t\n" +
                    "  WHERE 1         =1\n" +
                    "  AND t.ISDELETED = 0\n" +
                    "  AND t.STATE     = 1\n" +
                    "  AND t.USERNAME  = ?\n" +
                    "  AND t.PHONENO   = ?\n" +
                    "  AND t.IDNO      = ?\n" +
                    "  )\n" +
                    "ORDER BY length(r.IMGPATH), r.IMGPATH ASC ";
            list = this.getJdbcTemplate().query(sql, new Object[]{name, tel, idNo},
                    new BeanPropertyRowMapper<ErpReportdetailImginfo>(ErpReportdetailImginfo.class));
        }
        return list;
    }

}
