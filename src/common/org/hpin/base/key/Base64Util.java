
package org.hpin.base.key;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Base64Util {
	
	/**
	 * 这个二进制编码，不用管
	 */
	public static String CHAR_SET = "UTF-8";
	
	/**
	 * 加密方法
	 * @param str 需要加密的字符串
	 * @return 加密后的字符串，不会乱码
	 */
	public static String encode( String str ) {
		if( isEmpty( str ) ) {
			return str;
		}
		BASE64Encoder en = new BASE64Encoder();
		String encode = null;
		try {
			encode = en.encode( str.getBytes( CHAR_SET ) );
		} catch (UnsupportedEncodingException e) {			// 不需要处理
		}
		return encode;
	}
	
	/**
	 * 解密方法
	 * @param encoded 需要解密的字符串
	 * @return 解密后的字符串
	 */
	public static String decode( String encoded ) {
		if( isEmpty( encoded ) ) {
			return encoded;
		}
		BASE64Decoder de = new BASE64Decoder();
		byte[] code;
		String str = "";
		try {
			code = de.decodeBuffer( encoded );
			str = new String( code, CHAR_SET );
		} catch (IOException e) {
//			如果是这个类加密的东西，并没有被修改过的加密字符串，这里是不会出异常的。
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 判断空，空的难道还需要处理吗？
	 * @param str 需要判断的字符串
	 * @return 判断结果
	 */
	private static boolean isEmpty( String str ) {
		return str==null&&str.length()==0;
	}
	
	/**
	 * 测试方法
	 * @param args
	 */
	public static void main( String[] args ) {
		//testAllChar();
		
		test( "骺t瘴Z踳雵鬼蕉k~烍饿護v" );
	}
	
	/**
	 * 测试所有的字符，看出问题不.
	 */
	public static void testAllChar() {
		for( int c=Character.MIN_SUPPLEMENTARY_CODE_POINT; c<Character.MAX_CODE_POINT; c++ ) {
			test( (char)c +"" );
		}
	}
	
	public static void test( String str ) {
		System.out.println( "===========\nstr=" + str );
		String encode = encode( str );
		System.out.println( "encode=" + encode );
		String decode = decode( encode );
		System.out.println( "decode=" + decode );
		System.out.println( "equals=" + str.equals( decode ) );
		System.out.println( "===========" );
	 }
	
}
