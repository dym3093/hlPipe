package org.hpin.base.key;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

public class AESKey {

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void test() {
		String content = "test";
		String password = "12345678";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content, password);
		// 解密
		byte[] decryptResult = decrypt(encryptResult, password);
		System.out.println("解密后：" + new String(decryptResult));

	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex); 
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}


	/**  
     * 加密  
     * @param content 需要加密的内容  
     * @param password  加密密码  
     * @return  
     */  
    public static byte[] encrypt2(String content, String password) {   
            try {   
                    SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");   
                    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");   
                    byte[] byteContent = content.getBytes("utf-8");   
                    cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
                    byte[] result = cipher.doFinal(byteContent);   
                    return result; // 加密   
            } catch (NoSuchAlgorithmException e) {   
                    e.printStackTrace();   
            } catch (NoSuchPaddingException e) {   
                    e.printStackTrace();   
            } catch (InvalidKeyException e) {   
                    e.printStackTrace();   
            } catch (UnsupportedEncodingException e) {   
                    e.printStackTrace();   
            } catch (IllegalBlockSizeException e) {   
                    e.printStackTrace();   
            } catch (BadPaddingException e) {   
                    e.printStackTrace();   
            }   
            return null;   
    }  

    // 加密
    public static String encryptAESCBC(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
             System.out.print("Key为空null");
            return null;
         }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
             System.out.print("Key长度不是16位");
            return null;
         }
        byte[] raw = sKey.getBytes();
         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
         IvParameterSpec iv = new IvParameterSpec("1234567812345678".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
         cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(URLEncoder.encode(sSrc,"UTF-8").getBytes());
        return parseByte2HexStr(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
     }

    // 解密 
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                 System.out.print("Key为空null");
                return null;
             }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                 System.out.print("Key长度不是16位");
                return null;
             }
            byte[] raw = sKey.getBytes("ASCII");
             SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
             Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
             IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                     .getBytes());
             cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                 String originalString = new String(original);
                return originalString;
             } catch (Exception e) {
                 System.out.println(e.toString());
                return null;
             }
         } catch (Exception ex) {
             System.out.println(ex.toString());
            return null;
         }
     }

    
    public static void main(String[] str){
    	
    	String content = "test";
		String password = "12345678";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content, password);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		System.out.println("加密后：" + encryptResultStr);
		// 解密
		byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
		byte[] decryptResult = decrypt(decryptFrom, password);
		System.out.println("解密后：" + new String(decryptResult));
    }
}
