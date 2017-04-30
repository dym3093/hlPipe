package org.hpin.base.dict.exceptions;

/**
 * <p>
 * Title:创建document时抛出的异常
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
public class DocumentCreateException extends DictException {

    /**
     *  
     */
    public DocumentCreateException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorMessage
     */
    public DocumentCreateException(String errorMessage) {
        super(errorMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public DocumentCreateException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
