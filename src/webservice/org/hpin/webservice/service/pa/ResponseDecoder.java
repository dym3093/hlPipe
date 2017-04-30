package org.hpin.webservice.service.pa;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.pajk.suez.common.codec.BaseResponseCodec;
import com.pajk.suez.common.codec.CodecUtil;

/**
 * Created by fangliang on 17/2/10.
 */
public class ResponseDecoder extends BaseResponseCodec {

    public ResponseDecoder() {
    }

    public ResponseDecoder(String key) {
        super.key = key;
    }

    private Map<String, String> map;

    public boolean decode(String data) {
        try {
            if (data == null || data.trim().length() == 0) {
                map = Collections.emptyMap();
                return false;
            } else {
                data = data.trim();
            }
            String[] pairs = data.split("&");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (String pair : pairs) {
                paramMap.put(pair.split("=")[0], pair.split("=")[1]);
            }
            String cipher = paramMap.get("d");
            String salt = paramMap.get("s");
            String hmac = paramMap.get("h");
            map = CodecUtil.decode(cipher, key, salt, hmac);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getData() {
        if (map != null && !map.isEmpty()) {
            return map.get(RESERVED_NAME_OBJECT);
        } else {
            throw new IllegalStateException("data has NOT been decoded, map is NULL or EMPTY");
        }
    }
}
