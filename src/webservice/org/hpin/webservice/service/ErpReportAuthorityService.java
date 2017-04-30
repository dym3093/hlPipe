package org.hpin.webservice.service;
/**
 * Created by admin on 2016/12/6.
 */

import org.apache.commons.lang.StringUtils;
import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.ErpReportAuthority;
import org.hpin.webservice.dao.ErpReportAuthorityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 报告权限表Service
 *
 * @author YoumingDeng
 * @since 2016-12-06 15:58
 */
@Service("org.hpin.webservice.service.ErpReportAuthorityService")
public class ErpReportAuthorityService extends BaseService{

    @Autowired
    private ErpReportAuthorityDao dao;

    @Autowired
    private GeneCustomerService geneCustomerService;
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
            list = dao.listByProps(params, true);
        }
        return  list;
    }

    /**
     * @description 根据条件判定是否精确查询信息
     * @param params 条件
     * @param isExact 是否精确查询
     * @return List
     * @author YoumingDeng
     * @since: 2016/12/6 16:05
     */
    public List<ErpReportAuthority> listByProps(Map<String,String> params, boolean isExact) throws Exception{
        List<ErpReportAuthority> list = null;
        if(!CollectionUtils.isEmpty(params)){
            list = dao.listByProps(params, isExact);
        }
        return  list;
    }

    /**
     * @description 根据会员信息查询其是否有查看报告的权限
     * @param params 会员信息
     * @return boolean false:否， true:是
     * @author YoumingDeng
     * @since: 2016/12/6 16:41
     */
    public boolean findAuthorityByProps(Map<String,String> params) throws Exception{
        boolean flag = false;
        if(!CollectionUtils.isEmpty(params)){
		// 1. 根据客户信息获取项目编码
		String projectNo = null;
		try {
			//根据客户信息查询项目号
			projectNo = geneCustomerService.findProjectNoByProps(params);
            if(StringUtils.isNotEmpty(projectNo)) {
                // 2. 根据项目编码获取 过滤逻辑表信息
                params.clear();
                params.put(ErpReportAuthority.F_PROJECTNO, projectNo);
                params.put(ErpReportAuthority.F_ISDELETED, "0");
                List<ErpReportAuthority> reportAuthorityList = this.listByProps(params);
                //有权限关系，则判定
                if (!CollectionUtils.isEmpty(reportAuthorityList)) {
                    String filterNo = reportAuthorityList.get(0).getFilterNo();
                    if ("0".equalsIgnoreCase(filterNo)) {
                        //推送
                        flag = true;
                    } else if ("1".equalsIgnoreCase(filterNo)) {
                        //不推送
                        flag = false;
                    }
                } else {
                    //没有配置权限关系，则默认通过
                    flag = true;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        }
        return flag;
    }

}
