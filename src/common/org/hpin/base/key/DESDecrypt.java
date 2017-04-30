package org.hpin.base.key;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESDecrypt {

	 private byte[] desKey;
	    public DESDecrypt(byte[] desKey) {
	        this.desKey = desKey;
	    }
	    public byte[] doDecrypt(byte[] encryptText) throws Exception {
	        //      DES算法要求有一个可信任的随机数源
	        SecureRandom sr = new SecureRandom();
	        byte rawKeyData[] = desKey; /* 用某种方法获取原始密匙数据 */
	        // 从原始密匙数据创建一个DESKeySpec对象
	        DESKeySpec dks = new DESKeySpec(rawKeyData);
	        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
	        // 一个SecretKey对象
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey key = keyFactory.generateSecret(dks);
	        // Cipher对象实际完成解密操作
	        Cipher cipher = Cipher.getInstance("DES");
	        // 用密匙初始化Cipher对象
	        cipher.init(Cipher.DECRYPT_MODE, key, sr);
	        // 现在，获取数据并解密
	        byte encryptedData[] = encryptText;/* 获得经过加密的数据 */
	        // 正式执行解密操作
	        byte decryptedData[] = cipher.doFinal(encryptedData);
	        return decryptedData;
	    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
