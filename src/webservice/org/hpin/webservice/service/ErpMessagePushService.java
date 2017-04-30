package org.hpin.webservice.service;

import org.hpin.common.core.orm.BaseService;
import org.hpin.webservice.bean.ErpMessagePush;
import org.hpin.webservice.dao.ErpMessagePushDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 消息推送表Service
 * Created by Damian on 17-1-1.
 */
@Service("org.hpin.webservice.service.ErpMessageService ")
@Transactional
public class ErpMessagePushService extends BaseService{

    @Autowired
    private ErpMessagePushDao dao;

    /**
     * 根据条件是否严格查找
     * @param params 条件集合
     * @param isExact true:equal查找，false:like查找
     * @return List
     */
    public List<ErpMessagePush> listByProps(Map<String,String> params, boolean isExact) throws Exception {
        return dao.listByProps(params,isExact);
    }


    /**
     * 根据条件严格查找
     * @param params 条件集合
     * @return List
     */
    public List<ErpMessagePush> listByProps(Map<String,String> params) throws Exception{
        return dao.listByProps(params);
    }
}
