package org.hpin.base.dict.exceptions;

import org.hpin.common.core.exception.BusinessException;


/**
 * <p>
 * Title:字典exception基类
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
 */
public class DictException extends BusinessException {

    /**
     *  
     */
    public DictException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorMessage
     */
    public DictException(String errorMessage) {
        super(errorMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public DictException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
