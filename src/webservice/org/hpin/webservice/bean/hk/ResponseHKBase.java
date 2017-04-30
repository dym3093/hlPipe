package org.hpin.webservice.bean.hk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by root on 16-12-28.
 */

@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseHKBase {

    /** 场次号 */
    private String eventsNo;
    /** 接收人名字 */
    private String reportReceiveName;
    /** 接收人电话 */
    private String reportReceivePhone;
    /** 接收人地址 */
    private String reportReceiveAddress;

}
