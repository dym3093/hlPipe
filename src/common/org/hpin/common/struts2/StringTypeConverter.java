package org.hpin.common.struts2;

import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.hpin.common.util.StrUtils;
import org.springframework.web.util.HtmlUtils;

@SuppressWarnings("unchecked")
public class StringTypeConverter extends StrutsTypeConverter {

	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values[0] == null || "-1".equals(values[0])) {
			return null;
		}
		return values[0];
	}

	public String convertToString(Map context, Object o) {
		
		if (o instanceof InputStream) {
			InputStream stream = (InputStream) o;
			return stream.toString() ;
		}else{
			String[] strArray = (String[]) o;
			for (int i = 0; i < strArray.length; i++) {
				if (StrUtils.isNullOrBlank(strArray[i])) {
					strArray[i] = null;
				}
			}
			return HtmlUtils.htmlEscape(strArray[0]);
		}
	}
}
