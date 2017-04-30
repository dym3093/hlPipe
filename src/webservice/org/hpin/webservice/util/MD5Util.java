/**
 * @author DengYouming
 * @since 2016-7-29 下午3:10:54
 */
package org.hpin.webservice.util;

/**
 * @author DengYouming
 * @since 2016-7-29 下午3:10:54
 */
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
public class MD5Util {  
	 //十六进制下数字到字符的映射数组  
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",  
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};  
  
    
    public static void main(String[] args) {
		System.out.println(encodeByMD5("address={\"provinceId\":120000,\"districtName\":\"河西区\",\"districtId\":120103,\"cityId\":120000,\"address\":\"23432\",\"cityName\":\"天津市\",\"provinceName\":\"天津\"}&appId=gjk001299&bookTime=2016-08-01 14:54:23 +0800&contact={\"phone\":\"\",\"name\":\"gs201600499\"}&name=HL1608011404002&nonceStr=C7177C971EE04B68AA5FB38363D0788A&serviceItems=[{\"itemCodes\":\"1348,1347,1161\",\"name\":\"TM3\"}]&key=66acda0861df193ab32b4fb45dad9092fd2271855cb51961735cf7a8cebfa786"));
	}
    
    /** * 把inputString加密     */  
    public static String generatePassword(String inputString){  
        return encodeByMD5(inputString);  
    }  
      
      /** 
       * 验证输入的密码是否正确 
     * @param password    加密后的密码 
     * @param inputString    输入的字符串 
     * @return    验证结果，TRUE:正确 FALSE:错误 
     */  
    public static boolean validatePassword(String password, String inputString){  
        if(password.equals(encodeByMD5(inputString))){  
            return true;  
        } else{  
            return false;  
        }  
    }  
    /**  对字符串进行MD5加密     */  
    public static String encodeByMD5(String originString){  
        if (originString != null){  
            try{  
                //创建具有指定算法名称的信息摘要  
                MessageDigest md = MessageDigest.getInstance("MD5");  
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算  
                byte[] results = md.digest(originString.getBytes());  
                //将得到的字节数组变成字符串返回  
                String resultString = byteArrayToHexString(results);  
                return resultString.toUpperCase();  
            } catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
        return null;  
    }  
    
    /**
     * Apache的MD5加密
     * @param orgStr
     * @return
     * @author DengYouming
     * @since 2016-8-10 下午8:49:01
     */
    public static String md5Str(String orgStr){
    	if(orgStr!=null){
    		return DigestUtils.md5Hex(orgStr).toUpperCase();
    	}
    	return null;
    }
      
    /**  
     * 转换字节数组为十六进制字符串 
     * @param     字节数组 
     * @return    十六进制字符串 
     */  
    private static String byteArrayToHexString(byte[] b){  
        StringBuffer resultSb = new StringBuffer();  
        for (int i = 0; i < b.length; i++){  
            resultSb.append(byteToHexString(b[i]));  
        }  
        return resultSb.toString();  
    }  
      
    /** 将一个字节转化成十六进制形式的字符串     */  
    private static String byteToHexString(byte b){  
        int n = b;  
        if (n < 0)  
            n = 256 + n;  
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }  
}  
