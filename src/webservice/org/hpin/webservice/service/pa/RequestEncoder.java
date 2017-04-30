package org.hpin.webservice.service.pa;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.pajk.suez.common.cipher.HMacUtil;
import com.pajk.suez.common.cipher.TripleDESUtil;
import com.pajk.suez.common.codec.CodecUtil;

/**
 * Created by fangliang on 16/12/2.
 */
public class RequestEncoder {

    public static String encode(String key, HashMap<String, String> m, String partnerId, String action) {

        String qs = RequestEncoder.toString(m);
        String salt = CodecUtil.nextRandomFloat();
        if (qs == null) {
            return null;
        } else {
            qs = TripleDESUtil.encryptHex(qs, key);
        }
        String hmac = null;
        try {
            hmac = HMacUtil.sha1HString(qs, key, salt);
        } catch (Exception e) {
            return null;
        }

        // partner salt hamc action fileId
        return String.format("p=%s&s=%s&h=%s&m=%s&q=%s", partnerId, salt, hmac, action, qs);
    }

    public static String toString(Map<String, String> map) {
        if(map == null) {
            return null;
        } else {
            TreeMap m = new TreeMap(map);

            try {
                StringBuilder e = new StringBuilder();
                Iterator i$ = m.entrySet().iterator();

                while(i$.hasNext()) {
                    Map.Entry entry = (Map.Entry)i$.next();
                    if(entry.getValue() != null) {
                        String key = ((String)entry.getKey()).trim();
                        String value = URLEncoder.encode(((String) entry.getValue()).trim(), "utf-8");
                        e.append(String.format("%s=%s&", new Object[]{key, value}));
                    }
                }

                return e.toString();
            } catch (UnsupportedEncodingException var7) {
                return null;
            }
        }
    }

}
