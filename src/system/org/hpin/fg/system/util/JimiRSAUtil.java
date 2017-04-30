package org.hpin.fg.system.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class JimiRSAUtil {

		
	public static void main(String[] args) throws Exception {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		// 获取keys
		KeyPair keys = getKeyPair(Constant.PEM_KEY_PAIR_PROP);
		System.out.println(keys.getPrivate());
		System.out.println(keys.getPublic());

		// 签名
		byte[] message = "{\"name\":\"eeeeee\",\"mobile\":\"137610313344\"}11389940071".getBytes();
		byte[] sigBytes = sig(message, keys.getPrivate());
		String base64Sig = DatatypeConverter.printBase64Binary(sigBytes);
		System.out.println("Signature:" + base64Sig);

		// 验证签名
		boolean isValid = verify(message, keys.getPublic(), base64Sig);
		System.out.println("Verify signature:" + isValid);
	}

	public static String getSig(byte[] data) throws Exception {
		KeyPair keys = getKeyPair(Constant.PEM_KEY_PAIR_PROP);
		byte[] sigBytes = sig(data, keys.getPrivate());
		return DatatypeConverter.printBase64Binary(sigBytes);
	}

	/**
	 * 获取key对象
	 * @param pemString
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static KeyPair getKeyPair(String pemString) throws IOException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		StringReader br = new StringReader(pemString);
		BufferedReader read = new BufferedReader(br);
		PEMParser pa = new PEMParser(read);
		Object keyObj = pa.readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		return converter.getKeyPair((PEMKeyPair) keyObj);
	}
	/**
	 * 获取key对象
	 * @param pemString
	 * @return
	 * @throws IOException
	 */
	public static KeyPair getKeyPair() throws IOException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		StringReader br = new StringReader(Constant.PEM_KEY_PAIR_PROP);
		Object keyObj = new PEMParser(new BufferedReader(br)).readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
		return converter.getKeyPair((PEMKeyPair) keyObj);
	}

	public static byte[] sig(byte[] data, PrivateKey key) throws Exception {
		Signature signer = Signature.getInstance("SHA1withRSA", "BC");
		signer.initSign(key);
		signer.update(data);
		return signer.sign();
	}

	public static boolean verify(byte[] data, PublicKey pubKey, String base64Sig) throws Exception {
		Signature signer = Signature.getInstance("SHA1withRSA", "BC");
		signer.initVerify(pubKey);
		signer.update(data);
		return signer.verify(DatatypeConverter.parseBase64Binary(new String(base64Sig)));
	}
	
	public static boolean verify(byte[] data, String base64Sig) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Signature signer = Signature.getInstance("SHA1withRSA", "BC");
		KeyPair keys = getKeyPair(Constant.PEM_KEY_PAIR_PROP);
		signer.initVerify(keys.getPublic());
		signer.update(data);
		return signer.verify(DatatypeConverter.parseBase64Binary(new String(base64Sig)));
	}

}
