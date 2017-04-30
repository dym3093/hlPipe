/**
 * @author DengYouming
 * @since 2016-10-13 下午6:51:22
 */
package org.hpin.webservice.foreign;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author DengYouming
 * @since 2016-10-13 下午6:51:22
 */
public class MyHostnameVerifier implements HostnameVerifier{

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

}
