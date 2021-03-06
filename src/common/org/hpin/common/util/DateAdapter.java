package org.hpin.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
	 private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	    public Date unmarshal(String val) throws Exception {
	        if (val == null) {
	            return null;
	        }
	        return df.parse(val);
	    }
	 
	    public String marshal(Date val) throws Exception {
	        if (val == null) {
	            return null;
	        }
	        return df.format(val);
	    }
}
