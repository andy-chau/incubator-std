package com.lakala.encrypt.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;


public class RSA {

	public static String ALGORITHM = "RSA";

	public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算饭

	private static String log = "RSAUtil";

	public static String CHAR_SET = "UTF-8";
	
    /** *//**
	* RSA最大加密明文大小
	*/
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /** *//**
	* RSA最大解密密文大小
	*/
    private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 数据签名
	 * 
	 * @param content
	 *            签名内容
	 * @param privateKey
	 *            私钥
	 * @return 返回签名数据
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(CHAR_SET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签名验证
	 * 
	 * @param content
	 * @param sign
	 * @param lakala_public_key
	 * @return
	 */
	public static boolean verify(String content, String sign,
			String lakala_public_key) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(lakala_public_key);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(CHAR_SET));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 通过公钥解密
	 * 
	 * @param content待解密数据
	 * @param pk公钥
	 * @return 返回 解密后的数据
	 */
	protected static byte[] decryptByPublicKey(String content, PublicKey pk) {

		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.DECRYPT_MODE, pk);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;

				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}

				writer.write(ch.doFinal(block));
			}

			return writer.toByteArray();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过私钥加密
	 * 
	 * @param content
	 * @param pk
	 * @return,加密数据，未进行base64进行加密
	 */
	protected static byte[] encryptByPrivateKey(String content, PrivateKey pk) {

		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.ENCRYPT_MODE, pk);
			return ch.doFinal(content.getBytes(CHAR_SET));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("通过私钥加密出错");
		}
		return null;

	}

	/**
	 * 解密数据，接收端接收到数据直接解密
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String key) {
		System.out.println(log + "：decrypt方法中key=" + key);
		if (null == key || "".equals(key)) {
			System.out.println(log + "：decrypt方法中key=" + key);
			return null;
		}
		String res = null;
		try {
		    PublicKey pk = getPublicKeyByString(key);
		    byte[] data = decryptByPublicKey(content, pk);
			res = new String(data, CHAR_SET);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 对内容进行加密
	 * 
	 * @param content
	 * @param key私钥
	 * @return
	 */
	public static String encrypt(String content, String key) {
		String res = null;
		try {
		    PrivateKey pk = getPrivateKeyByString(key);
		    byte[] data = encryptByPrivateKey(content, pk);
			res = Base64.encode(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}
	
	/** *//**
	* <p>
	* 公钥加密
	* </p>
	*
	* @param data 源数据
	* @param publicKey 公钥(BASE64编码)
	* @return
	* @throws Exception
	*/
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** 
	* <p>
	* 私钥加密
	* </p>
	*
	* @param data 源数据
	* @param privateKey 私钥(BASE64编码)
	* @return
	* @throws Exception
	*/
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	* <P>
	* 私钥解密
	* </p>
	*
	* @param encryptedData 已加密数据
	* @param privateKey 私钥(BASE64编码)
	* @return
	* @throws Exception
	*/
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	public static String decryptByPrivateKey(String encryptedData, String privateKey)
			throws Exception {
		return new String(decryptByPrivateKey(Base64.decode(encryptedData),privateKey));
	}
	
	// 公钥转换
    @SuppressWarnings("restriction")
    public static PublicKey getPublicKeyByString(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    // 私钥转换
    @SuppressWarnings("restriction")
    public static PrivateKey getPrivateKeyByString(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

	public static void main(String[] args) {
		String str = Base64.encode("test".getBytes());
		System.out.println("Base64加密-->" + str);
		String aaa = sign(
				str,
				"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKWmOMF5zQBqc8xA41zq9BKHI8GUdZdJR8qP57mv8y2N7T/TD1YeNEx3UH7cSWpsZpdzV5BWKAhH7sTOWfhIU+EhrXrWbQWAndWcT2+ZDYlI0ctJ5Bo1gM0MEU01+g3mhOf70I9DJrYGvoVYV815m+F46bjq8qVEL06zZZLEKCTPAgMBAAECgYEAjZl3rrvFp/NXpWRadtVJaoUm5ZVYp8g2nEtDVJG5mFlYU1TCKWWMY0kjAC6ie1zKnfA1C+b6NYn36zhR5FE/kTSwUYT1P6INT4rD7JUEiwE8hi4MTvWIDCyqeUmb2H+abHpBo9VZymmh5wmwRTi1PgPQGTDq5uP519lgD00DzEECQQDZzHPSvgy0GztmD6Uip1mHDI9j7syIEVCEPtuIsNzH63GQdR5jLH7hZn0WRXEgrZa+f3gKZnFIew+lj6Ip1lPRAkEAwrQrwe6dcioJHdc0PsW/In5TOBTY+ppVKhrkJ6x9UOzZT/BYuXUFVJL8kiKGIK0wihOzMhHK57HjoN5fT5w2nwJBAJ5D4npmXgbWrxAYGFCZOQZYyy28DmZl5pNitdabZqPj5A8r/Bvm7oBOIGF5rp4nZh4htJIiJPmdax5MxHMQarECQH8yMPvqpJT2fSovcwQnL2ybVkZm6DEfLc/p7W81slBxyq38eBoAJtFPjQzy3Ojv+6vYntJw6Ttf7TMk0uMxTEUCQDw1ZXU5jiKoktSYjHjFL2EfqtfT9F8xTHVyaruKL+R67Z0OvxMNlM0j77ADUS/BAFlwF2bCsSkMT3PLvcNtpPk=");
		System.out.println("加签-->" + aaa);
		System.out
				.println("验签-->"
						+ verify(
								str,
								aaa,
								"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClpjjBec0AanPMQONc6vQShyPBlHWXSUfKj+e5r/Mtje0/0w9WHjRMd1B+3ElqbGaXc1eQVigIR+7Ezln4SFPhIa161m0FgJ3VnE9vmQ2JSNHLSeQaNYDNDBFNNfoN5oTn+9CPQya2Br6FWFfNeZvheOm46vKlRC9Os2WSxCgkzwIDAQAB"));
		System.out.println("原文-->" + new String(Base64.decode(str)));
		
		String key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChHEP4d896wdubHz+m8TjoExgrNYMlKsW87+vMZ3smzwiIqGoIG5EQShZqydsgB2JOXn+9s5jVHPEJxHea2Nbn08VP8BAh4Hb4q6VpsKCXkfdstgsS8+Rh+nJzpTxElW+osUBOA+5i4N7i+ut/NXgsxmCxqlJrrYIJI24LFvQHQkAio0qBjp4K9otm6EAF50EZGteBUTWmZa/VuQxUbcix57PYAAZGQHGdEuxxyNuL/qogfNFt9rfpCgMR4xdRZvrDwRwAX6EVWob/Q/iD+GrU4efHv6tBWSh6Esv8Jt/ZnYL3fSR4P5+m7KUuy9gK+X5c5vZEdy4tNH3Y61rR3VvJAgMBAAECggEAfa8FP3KIA2X0IcFw8JVCJZmvwxWN55LEi65HL0CTDCV6rNFlVknbEvAZKNmr/gKEqEqEMMNIuQhI6avA+qWqkVPdm4zVqPfpF/kfo6HMxjFy6fXiEbj+M4kjfCAtMfu6DcmpNrNOZwiyGDRTPvvBcnyXtkH+5k2HIgXntPMFEBtWPdh3poQwJsHnqWpUifCFiQnR5LvBJQZH4ceAwsvGeWIUSCoWhppkh4NRvAaUDon/k4t0aEy1CPzSpS/uQN4Y/N27m8v2/jk8Z0u8Joe+Q03hpEMr7dfSs3bqkIgQX9ZP4d8AEMtzze8Z423PF1yRW9OlUNOX5CF+BT5TP4h6AQKBgQD1mvsJH/J9hIkTjdXYeRq9trsP4RPS/G/wYtBbektUAxk2K9wPm0byU2HQuqEib8VU4VFdkUuiw0+emQ+2BBNunMy1/+d5cn1huKyiR3kH0uaRzVBL1p3qHG+sqZKWE6e/EhoE9WvqQc3Ry9At9pNCzdD2jQwHUtGbzdMMoS/gaQKBgQCn7dJExXHppqyG9pMXOPjwp4b95tBEr8qPluJPFEvE5ypQt1rW43OeORk+FqGwXz2UHCJKIgXgjYnxvHDHxrM02eZTYyTj+jqeGQO8fics38/JxmAwmBu1144pczF4iAbuclYlB7EBY02iX4eS8L1Qng1zaU0VsIyDLCvH94Q0YQKBgQC0Trf3RfXvAgrkSR9yUc448tq32KSGI39GejS+w7Rjk/bBV0eySWu3YVGRPEIplubG3reuOonNjxd3tqTbGnjtnr2G670S4uN7h2ltpY0MGl/dMF6/nmrGQWQW3VLZTMq8slxZwZcdHnwshjVqWPhZdeHv7zKiecGaYWuMfRU56QKBgQCZEzTE86au8fv62vGiDZD+7fcjoy7eLdBbq5KHu1yGFKKCCWGI2LUf2bSk4ERrXaXoSO0I3pK06tB/xuKXeQ0KdEZ8ZLfQCN0+GFdLj0NuqGXk7Cvqn/1CeUdhiVvjHzwSR682+hfjx/2QsbwHueMYhbqFJcvapaCwQad3FK0ygQKBgQC2gK2KQ2GNzkpOSzQh82d1ofyLwNrFl+fFxYycsHWvxpP2yWj/NDqlzRVBzEQv18DKNvtF6PmtV065b0KOpvcpy6kcKjX1ct9HaxL/LcEMo4dUPD7+PGJcZjeisFw2Fn7fh6g3r3qsS0aFGvgdTirjH6IuuGXOPMBw8at6286xjw==";
        String content = "PsgMreZA64WcuFoFYEq4y0/BmIaNI+tSoLLtxPGuESqEC7GGuvzUWxMPwTdL9oXWbR6A/5bSPsRbzt3OeB4sLlpp98D9ft7H3rY5iroxfcMIaBDC4us2TUbmO8RvRMLwhyRbGqQ8ODip03H85a+UPb84ILSx5HNDxHxDqkCslSPZiZM7gioVWEiLiHltJs9eYfaCDhuu+hbsmsbjQx32KgGKAHvutiqZiOGI6GAavtE2Z5atZ3hd8bqOzpgipHb4SUGnc9aNtcnTx7zIh0DvSYhDx8dIC9kQHQATsIWhQp6SBmMg9JE/b1VENECMtFPTJNNCVQLCzvWVKZL0gaFCPw==";
        try {
            System.out.println(decryptByPrivateKey(content, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
