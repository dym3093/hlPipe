package org.hpin.webservice.service;/**
 * Created by admin on 2016/11/30.
 */

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.jz.ErpReportUrlJY;
import org.hpin.webservice.dao.ErpReportUrlJYDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报告URL地址信息表(ERP_REPORT_SCHEDULE_JY的从表)Service
 *
 * @author YoumingDeng
 * @create 2016-11-30 14:43
 */
@Service("org.hpin.webservice.service.ErpReportUrlJYService ")
public class ErpReportUrlJYService extends BaseService{

    @Autowired
    private ErpReportUrlJYDao dao;

    public List<ErpReportUrlJY> listByProps(Map<String,String> params) throws Exception{
        List<ErpReportUrlJY> list = null;
        if (params!=null&&params.size()>0){
            list = dao.listByProps(params);
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
        return dao.cleanOldUrl(code, name);
    }
}
