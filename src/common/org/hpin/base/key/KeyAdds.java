package org.hpin.base.key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyAdds {

	private static String Algorithm = "DES";
	// 定义 加密算法,可用 DES,DESede,Blowfish
	static boolean debug = false;

	/**
	 * 构造子注解.
	 */
	public KeyAdds() {

	}

	/**
	 * 生成密钥
	 * 
	 * @return byte[] 返回生成的密钥
	 * @throws exception
	 *             扔出异常.
	 */
	public static byte[] getSecretKey() throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
		if (debug)
			System.out.println("生成密钥:" + byte2hex(deskey.getEncoded()));
		return deskey.getEncoded();
	}

	/**
	 * 将指定的数据根据提供的密钥进行加密
	 * 
	 * @param input
	 *            需要加密的数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encryptData(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug) {
			System.out.println("加密前的二进串:" + byte2hex(input));
			System.out.println("加密前的字符串:" + new String(input));

		}
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(input);
		if (debug)
			System.out.println("加密后的二进串:" + byte2hex(cipherByte));
		return cipherByte;

	}

	/**
	 * 将给定的已加密的数据通过指定的密钥进行解密
	 * 
	 * @param input
	 *            待解密的数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密后的数据
	 * @throws Exception
	 */
	public static byte[] decryptData(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug)
			System.out.println("解密前的信息:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		if (debug) {
			System.out.println("解密后的二进串:" + byte2hex(clearByte));
			System.out.println("解密后的字符串:" + (new String(clearByte)));

		}
		return clearByte;

	}

	/**
	 * 字节码转换成16进制字符串
	 * 
	 * @param byte[]
	 *            b 输入要转换的字节码
	 * @return String 返回转换后的16进制字符串
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";

		}
		return hs.toUpperCase();

	}
	/***
	 * 视频加密使用
	 * @param str
	 * @return
	 */
	public static String Gbk2Unicode(String str){
		String result = null;
		for (int i = 0; i < str.length(); i++){
			int chr1 = (char) str.charAt(i);
			if (result==null)
				result=Integer.toString(chr1);
			else
				result += ","+Integer.toString(chr1);
		}
		return result;
	}
	
	
	public static byte[] toKey(String str){
		 KeyAdds  etg  = new  KeyAdds();
		 debug  = false ;
		 byte[] strs =null;
		try {
			 byte[] key = etg.getSecretKey ();
			
	         
	         byte [] en  = etg.encryptData (str.getBytes() ,key );
	          strs=en;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return strs;
	}
	
	public static String rKey(byte[] str){
		KeyAdds  etg  = new  KeyAdds();
		System.out.println ("source ="+str);
		String strs="";
		try {
			 byte[] de = etg.decryptData (	str,etg.getSecretKey() );
				System.out.println ("source2 ="+de);
			  strs = new  String (de );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return strs;
		
	}
	public static void main(String[] args) {
		 try 
	        {
			 KeyAdds  etg  = new  KeyAdds();
			 debug  = false ;
             byte [] key  = etg.getSecretKey ();
             String str="sdfsdf";
             byte[] strs = etg.toKey(str);
             System.out.println ("encryptData = "+strs);
             
             
             
             String dstr = etg.rKey(strs);
             System.out.println ("decryptData = "+dstr);
             
             
	        }catch (Exception  e )
	        {
	             e.printStackTrace ();
	            
	        }
	}
}
