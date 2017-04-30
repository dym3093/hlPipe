package org.hpin.base.dict.service;

import org.hpin.common.core.SpringTool;


/**
 * <p>
 * Title:id2name的factory，其中应包括db,xml的id2nameService
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 1.0
 *  
 *  
 */
public class ID2NameServiceFactory {

    /**
     * 获取db的id2nameService
     * 
     * @return db的id2nameService
     */
    public static ID2NameService getId2nameServiceDB() {
        return (ID2NameService) SpringTool.getBean(
        		ID2NameService.class);
    }
}
