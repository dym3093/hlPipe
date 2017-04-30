package org.hpin.base.key;

public class EncodeString {

    /**
     * 将字符串转换为十进制Unicode编码
     */
	public static String toUnicode(String str){
		int len = str.length();
		StringBuffer buffer = new StringBuffer();

        for(int x=0; x<len; x++) {
        	int c = str.codePointAt(x);
        	buffer.append(c);
        	if(x < len - 1 )
        		buffer.append(",");
        }
        return buffer.toString();
	}
	
	/**
	 * 将十进制Unicode编码反转
	 */
	public static String reverseUnicode(String str){
		String[] temp = str.split(",");
		StringBuffer result = new StringBuffer();
		for(String s:temp){
			result.append(s.isEmpty()?"":(char)Integer.parseInt(s));
		}
		return result.toString();
	}
}
