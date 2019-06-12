package com.lakala.encrypt.utils;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 通用的RSA工具
 * 
 * @author lakala
 *
 */
public class RSAUtils {


    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * 测试时的，种子
     */
    private static final String TEST_RAND = "956091";

    /**
     * 加解密算法
     */
    private static final String ENCRYPT_ALGORITHM = "RSA";

    /**
     * 随机生成密钥对
     */
    public static KeyPair genKeyPair(String rand) {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(rand)) {
            // 当传了密钥时，
            keyPairGen.initialize(2048, new SecureRandom(rand.getBytes()));// 使用传过来的种子
        } else {
            keyPairGen.initialize(2048);// 随机生成种子
        }
        // keyPairGen.initialize(2048, new SecureRandom("956091".getBytes()));//自定义种子956091

        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取公钥
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return getPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 获取公钥
     * 
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 获取私钥
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            return getPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 获取私钥
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 公钥加密
     * 
     * @param publicKey
     * @param plainStr
     * @return
     */
    public static String encryptString(RSAPublicKey publicKey, String plainStr) {
        try {
            return new String(encrypt(publicKey, plainStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 公钥加密
     * 
     * @param publicKey
     * @param plainTextData
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 私钥解密
     * 
     * @param privateKey
     * @param cipherData
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    /*
     * 私钥签名
     */
    public static String sign(String content, String privateKey, String input_charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature =
                    java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 公钥验签
     * 
     * @param content
     * @param sign
     * @param publicKey
     * @param input_charset
     * @return
     */
    public static boolean verify(String content, String sign, String publicKey, String input_charset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature =
                    java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(input_charset));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 私钥加密
     * 
     * @param privateKey
     * @param plainTextData
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }


    /**
     * 私钥加密
     * 
     * @param privateKey
     * @param plainStr
     * @return
     */
    public static String encryptString(RSAPrivateKey privateKey, String plainStr) {
        try {
            return Base64.encode(encrypt(privateKey, plainStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 私钥加密
     * 
     * @param privateKey
     * @param plainStr
     * @return
     */
    public static String encryptString(String privateKey, String plainStr) {
        try {
            return Base64.encode(encrypt(getPrivateKey(privateKey), plainStr.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 公钥解密
     * 
     * @param publickKey
     * @param plainStr
     * @return
     */
    public static String decryptString(String publickKey, String plainStr) {
        try {
            return new String(decryptString(getPublicKey(publickKey), plainStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 公钥解密
     * 
     * @param rsaPublicKey
     * @param cipherData
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(RSAPublicKey rsaPublicKey, byte[] cipherData) throws Exception {
        if (rsaPublicKey == null) {
            throw new Exception("公钥密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 公钥解密
     * 
     * @param rsaPublicKey
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptString(RSAPublicKey rsaPublicKey, String data) throws Exception {
        return decrypt(rsaPublicKey, Base64.decode(data));
    }

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param charset 字符集
     * @param key 密钥
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static String encrypt(String content, String charset, Key key) throws Exception {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(content), "待加密内容不能为空");
        Preconditions.checkArgument(key instanceof RSAKey, "key类型必须为RSAKey");
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] data = content.getBytes(charset);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        int maxEncryptBlock = calcMaxEncryptBlock((RSAKey) key);
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(data, offSet, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncryptBlock;
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encodeBuffer(out.toByteArray());
    }

    /**
     * 解密
     *
     * @param encryptedContent 待解密内容
     * @param charset 字符集
     * @param key 密钥
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static String decrypt(String encryptedContent, String charset, Key key) throws Exception {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(encryptedContent), "待解密内容不能为空");
        Preconditions.checkArgument(key instanceof RSAKey, "key类型必须为RSAKey");
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] encryptedData = base64Decoder.decodeBuffer(encryptedContent);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        int maxDecryptBlock = calcMaxDecryptBlock((RSAKey) key);
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDecryptBlock) {
                cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDecryptBlock;
        }

        return new String(out.toByteArray(), charset);
    }

    // 计算加密分块最大长度
    private static int calcMaxEncryptBlock(RSAKey key) {
        return key.getModulus().bitLength() / 8 - 11;
    }

    // 计算解密分块最大长度
    private static int calcMaxDecryptBlock(RSAKey key) {
        return key.getModulus().bitLength() / 8;
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

    public static void test() {
        String pubKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoRxD+HfPesHbmx8/pvE46BMYKzWDJSrFvO/rzGd7Js8IiKhqCBuREEoWasnbIAdiTl5/vbOY1RzxCcR3mtjW59PFT/AQIeB2+KulabCgl5H3bLYLEvPkYfpyc6U8RJVvqLFATgPuYuDe4vrrfzV4LMZgsapSa62CCSNuCxb0B0JAIqNKgY6eCvaLZuhABedBGRrXgVE1pmWv1bkMVG3Iseez2AAGRkBxnRLsccjbi/6qIHzRbfa36QoDEeMXUWb6w8EcAF+hFVqG/0P4g/hq1OHnx7+rQVkoehLL/Cbf2Z2C930keD+fpuylLsvYCvl+XOb2RHcuLTR92Ota0d1byQIDAQAB";
        String priKey =
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChHEP4d896wdubHz+m8TjoExgrNYMlKsW87+vMZ3smzwiIqGoIG5EQShZqydsgB2JOXn+9s5jVHPEJxHea2Nbn08VP8BAh4Hb4q6VpsKCXkfdstgsS8+Rh+nJzpTxElW+osUBOA+5i4N7i+ut/NXgsxmCxqlJrrYIJI24LFvQHQkAio0qBjp4K9otm6EAF50EZGteBUTWmZa/VuQxUbcix57PYAAZGQHGdEuxxyNuL/qogfNFt9rfpCgMR4xdRZvrDwRwAX6EVWob/Q/iD+GrU4efHv6tBWSh6Esv8Jt/ZnYL3fSR4P5+m7KUuy9gK+X5c5vZEdy4tNH3Y61rR3VvJAgMBAAECggEAfa8FP3KIA2X0IcFw8JVCJZmvwxWN55LEi65HL0CTDCV6rNFlVknbEvAZKNmr/gKEqEqEMMNIuQhI6avA+qWqkVPdm4zVqPfpF/kfo6HMxjFy6fXiEbj+M4kjfCAtMfu6DcmpNrNOZwiyGDRTPvvBcnyXtkH+5k2HIgXntPMFEBtWPdh3poQwJsHnqWpUifCFiQnR5LvBJQZH4ceAwsvGeWIUSCoWhppkh4NRvAaUDon/k4t0aEy1CPzSpS/uQN4Y/N27m8v2/jk8Z0u8Joe+Q03hpEMr7dfSs3bqkIgQX9ZP4d8AEMtzze8Z423PF1yRW9OlUNOX5CF+BT5TP4h6AQKBgQD1mvsJH/J9hIkTjdXYeRq9trsP4RPS/G/wYtBbektUAxk2K9wPm0byU2HQuqEib8VU4VFdkUuiw0+emQ+2BBNunMy1/+d5cn1huKyiR3kH0uaRzVBL1p3qHG+sqZKWE6e/EhoE9WvqQc3Ry9At9pNCzdD2jQwHUtGbzdMMoS/gaQKBgQCn7dJExXHppqyG9pMXOPjwp4b95tBEr8qPluJPFEvE5ypQt1rW43OeORk+FqGwXz2UHCJKIgXgjYnxvHDHxrM02eZTYyTj+jqeGQO8fics38/JxmAwmBu1144pczF4iAbuclYlB7EBY02iX4eS8L1Qng1zaU0VsIyDLCvH94Q0YQKBgQC0Trf3RfXvAgrkSR9yUc448tq32KSGI39GejS+w7Rjk/bBV0eySWu3YVGRPEIplubG3reuOonNjxd3tqTbGnjtnr2G670S4uN7h2ltpY0MGl/dMF6/nmrGQWQW3VLZTMq8slxZwZcdHnwshjVqWPhZdeHv7zKiecGaYWuMfRU56QKBgQCZEzTE86au8fv62vGiDZD+7fcjoy7eLdBbq5KHu1yGFKKCCWGI2LUf2bSk4ERrXaXoSO0I3pK06tB/xuKXeQ0KdEZ8ZLfQCN0+GFdLj0NuqGXk7Cvqn/1CeUdhiVvjHzwSR682+hfjx/2QsbwHueMYhbqFJcvapaCwQad3FK0ygQKBgQC2gK2KQ2GNzkpOSzQh82d1ofyLwNrFl+fFxYycsHWvxpP2yWj/NDqlzRVBzEQv18DKNvtF6PmtV065b0KOpvcpy6kcKjX1ct9HaxL/LcEMo4dUPD7+PGJcZjeisFw2Fn7fh6g3r3qsS0aFGvgdTirjH6IuuGXOPMBw8at6286xjw==";
        JSONObject data = new JSONObject();
        data.put("mobile", "18718565884");
        String mmData = RSAUtils.encryptString(priKey, data.toJSONString());
        String sign = RSAUtils.sign(data.toJSONString(), priKey, "UTF-8");
        System.out.println(mmData);
        System.out.println(sign);

    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = genKeyPair(TEST_RAND);// 测试时,生成的key
        // KeyPair keyPair = genKeyPair("");//生产时,通过随时生成种子，产生公钥及私钥

        String prikey = Base64.encode(keyPair.getPrivate().getEncoded());
        String pubKey = Base64.encode(keyPair.getPublic().getEncoded());

        System.out.println("prikey=" + prikey);
        System.out.println("pubkey=" + pubKey);
        // prikey =
        // "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChHEP4d896wdubHz+m8TjoExgrNYMlKsW87+vMZ3smzwiIqGoIG5EQShZqydsgB2JOXn+9s5jVHPEJxHea2Nbn08VP8BAh4Hb4q6VpsKCXkfdstgsS8+Rh+nJzpTxElW+osUBOA+5i4N7i+ut/NXgsxmCxqlJrrYIJI24LFvQHQkAio0qBjp4K9otm6EAF50EZGteBUTWmZa/VuQxUbcix57PYAAZGQHGdEuxxyNuL/qogfNFt9rfpCgMR4xdRZvrDwRwAX6EVWob/Q/iD+GrU4efHv6tBWSh6Esv8Jt/ZnYL3fSR4P5+m7KUuy9gK+X5c5vZEdy4tNH3Y61rR3VvJAgMBAAECggEAfa8FP3KIA2X0IcFw8JVCJZmvwxWN55LEi65HL0CTDCV6rNFlVknbEvAZKNmr/gKEqEqEMMNIuQhI6avA+qWqkVPdm4zVqPfpF/kfo6HMxjFy6fXiEbj+M4kjfCAtMfu6DcmpNrNOZwiyGDRTPvvBcnyXtkH+5k2HIgXntPMFEBtWPdh3poQwJsHnqWpUifCFiQnR5LvBJQZH4ceAwsvGeWIUSCoWhppkh4NRvAaUDon/k4t0aEy1CPzSpS/uQN4Y/N27m8v2/jk8Z0u8Joe+Q03hpEMr7dfSs3bqkIgQX9ZP4d8AEMtzze8Z423PF1yRW9OlUNOX5CF+BT5TP4h6AQKBgQD1mvsJH/J9hIkTjdXYeRq9trsP4RPS/G/wYtBbektUAxk2K9wPm0byU2HQuqEib8VU4VFdkUuiw0+emQ+2BBNunMy1/+d5cn1huKyiR3kH0uaRzVBL1p3qHG+sqZKWE6e/EhoE9WvqQc3Ry9At9pNCzdD2jQwHUtGbzdMMoS/gaQKBgQCn7dJExXHppqyG9pMXOPjwp4b95tBEr8qPluJPFEvE5ypQt1rW43OeORk+FqGwXz2UHCJKIgXgjYnxvHDHxrM02eZTYyTj+jqeGQO8fics38/JxmAwmBu1144pczF4iAbuclYlB7EBY02iX4eS8L1Qng1zaU0VsIyDLCvH94Q0YQKBgQC0Trf3RfXvAgrkSR9yUc448tq32KSGI39GejS+w7Rjk/bBV0eySWu3YVGRPEIplubG3reuOonNjxd3tqTbGnjtnr2G670S4uN7h2ltpY0MGl/dMF6/nmrGQWQW3VLZTMq8slxZwZcdHnwshjVqWPhZdeHv7zKiecGaYWuMfRU56QKBgQCZEzTE86au8fv62vGiDZD+7fcjoy7eLdBbq5KHu1yGFKKCCWGI2LUf2bSk4ERrXaXoSO0I3pK06tB/xuKXeQ0KdEZ8ZLfQCN0+GFdLj0NuqGXk7Cvqn/1CeUdhiVvjHzwSR682+hfjx/2QsbwHueMYhbqFJcvapaCwQad3FK0ygQKBgQC2gK2KQ2GNzkpOSzQh82d1ofyLwNrFl+fFxYycsHWvxpP2yWj/NDqlzRVBzEQv18DKNvtF6PmtV065b0KOpvcpy6kcKjX1ct9HaxL/LcEMo4dUPD7+PGJcZjeisFw2Fn7fh6g3r3qsS0aFGvgdTirjH6IuuGXOPMBw8at6286xjw==";
        // pubKey =
        // "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoRxD+HfPesHbmx8/pvE46BMYKzWDJSrFvO/rzGd7Js8IiKhqCBuREEoWasnbIAdiTl5/vbOY1RzxCcR3mtjW59PFT/AQIeB2+KulabCgl5H3bLYLEvPkYfpyc6U8RJVvqLFATgPuYuDe4vrrfzV4LMZgsapSa62CCSNuCxb0B0JAIqNKgY6eCvaLZuhABedBGRrXgVE1pmWv1bkMVG3Iseez2AAGRkBxnRLsccjbi/6qIHzRbfa36QoDEeMXUWb6w8EcAF+hFVqG/0P4g/hq1OHnx7+rQVkoehLL/Cbf2Z2C930keD+fpuylLsvYCvl+XOb2RHcuLTR92Ota0d1byQIDAQAB";

        JSONObject json = new JSONObject();
        json.put("isRedirect", "0"); // 是否直接跳转网页（0为否，1为是）。下面代码访问/access接口生成URL时，此值须为0
        json.put("userId", "4400889930");
        json.put("mobile", "13581113366");
        String merAccCode = "";

        // 37接口
        /*
         * json.put("mobile", "13581765500"); json.put("realname", "李俊敬");
         * json.put("idCard","110106198510201829"); json.put("isFilter","Y");
         */

        // 私钥给商户 分段加密
        String encryptStr =
                RSAUtils.encrypt(json.toJSONString(), "UTF-8",
                        RSAUtils.getPrivateKeyByString(prikey));
        System.out.println("encryptStr：→");
        System.out.println(URLEncoder.encode(encryptStr, "UTF-8"));

        String sign = RSAUtils.sign(json.toJSONString(), prikey, "UTF-8");
        System.out.println("sign：→");
        System.out.println(sign);

        // 分段解密
        String decryptStr =
                RSAUtils.decrypt(encryptStr, "UTF-8", RSAUtils.getPublicKeyByString(pubKey));
        System.out.println("decryptStr=" + decryptStr);
//        decryptStr =
//                RSAUtils.decrypt("PsgMreZA64WcuFoFYEq4y0/BmIaNI+tSoLLtxPGuESqEC7GGuvzUWxMPwTdL9oXWbR6A/5bSPsRbzt3OeB4sLlpp98D9ft7H3rY5iroxfcMIaBDC4us2TUbmO8RvRMLwhyRbGqQ8ODip03H85a+UPb84ILSx5HNDxHxDqkCslSPZiZM7gioVWEiLiHltJs9eYfaCDhuu+hbsmsbjQx32KgGKAHvutiqZiOGI6GAavtE2Z5atZ3hd8bqOzpgipHb4SUGnc9aNtcnTx7zIh0DvSYhDx8dIC9kQHQATsIWhQp6SBmMg9JE/b1VENECMtFPTJNNCVQLCzvWVKZL0gaFCPw==", "UTF-8", RSAUtils.getPublicKeyByString(pubKey));
//        System.out.println("decryptStr=" + decryptStr);
        // 公钥给拉卡拉
//        boolean b = RSAUtils.verify(decryptStr, sign, pubKey, "UTF-8");
//        System.out.println("b=" + b);

    }
}
