package com.cldt.encrypt.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * RSA常用(qunaer)
 *
 * @author andang.ye
 */
public class RSAUtil {

    /**
     * 加解密算法
     */
    private static final String ENCRYPT_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHM = "MD5withRSA";

    /**
     * PKCS8文件初始化为PrivateKey.
     *
     * @param keyPath
     * @return
     * @throws Exception
     */
    public static PrivateKey initPrivateKey(String keyPath) throws Exception {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(keyPath));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0 || line.charAt(0) == '-') {
                    continue;
                }
                stringBuilder.append(line).append("\r");
            }

            return initPrivateKeyByContent(stringBuilder.toString());
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
    }

    public static PrivateKey initPrivateKeyByContent(String keyContent) throws Exception {
        Preconditions.checkNotNull(keyContent);

        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] keyByte = base64Decoder.decodeBuffer(keyContent);

        KeyFactory kf = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyByte);
        return kf.generatePrivate(keySpec);
    }

    /**
     * 公钥初始化
     *
     * @param keyPath
     * @return
     */
    public static PublicKey initPublicKey(String keyPath) throws Exception {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(keyPath));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0 || line.charAt(0) == '-') {
                    continue;
                }
                stringBuilder.append(line).append("\r");
            }

            return initPublicKeyByContent(stringBuilder.toString());
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
    }

    /**
     * 公钥初始化
     * 
     * @param keyContent 证书内容
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey initPublicKeyByContent(String keyContent) throws Exception {
        Preconditions.checkNotNull(keyContent, "检查安全存储配置或是否已初始化");

        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] keyByte = base64Decoder.decodeBuffer(keyContent);

        KeyFactory kf = KeyFactory.getInstance(ENCRYPT_ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyByte);
        return kf.generatePublic(keySpec);
    }

    /**
     * 签名算法: MD5withRSA
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception 签名异常直接抛出
     */
    public static String signWithMD5(String content, String charset, PrivateKey privateKey)
            throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(content.getBytes(charset));

        byte[] signByte = signature.sign();

        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encodeBuffer(signByte);
    }

    /**
     * 验签:MD5whtiRSA ,异常吞掉,返回验签失败
     *
     * @param content
     * @param sign
     * @param charset
     * @param publicKey
     * @return
     */
    public static boolean verifyWithMD5(String content, String sign, String charset,
            PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(charset));

            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] keyByte = base64Decoder.decodeBuffer(sign);

            return signature.verify(keyByte);
        } catch (Exception e) {
            // logger.error("验签出现异常", e);
            return false;
        }
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

    public static String getFilePath(String file) {
        String classPath = RSAUtil.class.getClassLoader().getResource("").getPath();
        String filePath = classPath + file;
        return filePath;
    }

    // 公钥转换
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    // 私钥转换
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    
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

    public static void main(String[] args) throws Exception {
    	KeyPair keyPair = genKeyPair("956091");// 测试时,生成的key
        // KeyPair keyPair = genKeyPair("");//生产时,通过随时生成种子，产生公钥及私钥
    	
//        //qunaer 公钥
//        String qunaerPubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxBx3Yp8PDX5SyE5pxb5yG7bR92fpYcCfgb8GKIpnLzXdPL2LubRED7HWs1a0uN40yehYgIIXYeDgBvJzZYl0e8pJk1/2Xtrb+JtxeLzjTnyfvh+ruXzxayBgidwfIaH+R/ubVIaM0CozKc/LxXf/4i1YkQYVG2FdC3QDyd5DlCdVbQtUCt1C6IXvsddMPx0IsRNkBs3uTM3Ual8nep9S4/CcW7WAYBdQxDPv08zu3qTnfFr6IamVkXmJHdWxJAXMFkdImeCIXbBXfFkOGzTizxKLUZtLoZ5xdnyT+XVEWmjmBKKrClt+CurnlcLAIzfFRNHFP16HeSrSauS6MrEniQIDAQAB";
//        //拉卡拉 私钥
//        String lklPrikey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDGprdvxLnV6+0ahHIVFb14SsO0uCam+RyyYeKDpFysFPYygQUtnQyekB2SPaq6GdgD0h0MO29rWURGpaX0KWl5b+JHUYZJ89cEpAGq5EdZDoxx4WJNrepKN0WfqwD1nxsNBx3wGE8J9+Dzn8yfWHciaxk/RYM3ZVQ4Gm7VLfmbJ0JCRZjbi0KEUbLO1TeZMjAoSesS4I2aVT2jl8qgGKWWzCgP9eBq2htCaALXl7r4IdPn5xhv0Eir1LHpwrYLcU18LG6/QPSoAMHv33qOvdPUHauQkXSgdFCpht3cfIs0PiI5ie/ILz5Yxs4xFnCyGySp7SF6JHL8/IycT9W3nahLAgMBAAECggEAHBuQzv698LfeJ6df4P3ffjrN8C8yQOP0nITfIRNLjYm1wDWjPu3+0BzCImQZWRW5C+lZ9NYgEMdjsr9TePW3F9it4rt1TddzsBNorP553e8CDkb+utEF2pRxhIMUsqxdjn1YThWScW2iZbZRpKZGzRbsTpiOVXM/9GRBFGbZ74rhbseFrrHg264DN2LNymVPZuzYRcKP2BLofSH6Mp7Q9DL4nlAMRp7gqosZ7hOext6btB5GuPFmZmmU0n2yjHsL/eDK8W1WbroVhNfNNv9UasjeuYUQRKVhwbplGFNWvd3uYtjbve011hH+dOVlvUKn6Eo+OlSp8+f+Y7ABLYNPwQKBgQD3F1L0sTfOehd19rq3vIkJpoxBlyTtRsaxut19K8by8peioOFjA2PJys6WNgKigEsCIJvV1VixoWZHmPFBc3wRKzFA7f5gcp3fLFozwSptUAFZKlSzc4Q9zkJX+rKTEVn60YvWwjQZbuHg/bAxcsZgxv7YPlzvFJ62pTHnrTeHKwKBgQDN0EmeR2+Y4UMhSAWYl6PwMoB6ulmo7BeuxpCiaY1QKq5bKOJzHaoS0LGRQsPBaN2RPaVYp8U2JS7bgKWWNsrYhNxYy6XptKsEJIXaz1Pdf8vHMabasUSYY4ER8K9MjYvVAV7YGPnabBDq6ZUNETdqDf5d3iOo4qdDKO/NXgHTYQKBgH5TfGQBnsh/fzMeO4Pobh9y+M4n2gc9YiRHpylxkjtvb7sDiXe3qfLnLwt4/YvbD3488a83bisSm1oLhXyy8EFO1dWeV6kzsCcDY/KF1Y0hlC7kB33fl0KKCz0zvMyX2Gr1awsXKs+98tm2mbgOOAIOewhPie/pmONEPel2k6cVAoGAVMwuqNIR/tFBEWNVOuuvlfWYzfgdnOW5VD9ntCL9IDyVneWYd4mPWzU1E/6IHRYRxBe8Gmz1ajujtLiqgNuOc+s5xjQR5dqUY56Qr/igOzC+SxIABzbmZksJENUzxLzFEjwAUdTojL/dsIwQ51Nim4WsegxecOUBuUASnXDQG8ECgYBEhQ+2JkTwdYwcZ3o1un1g9+6pilXcC6BxfEG8dia61QzjXWNumvimlHgaVGHRO+GYIyXWx2ZBHxHD3P0zLbTEc76vWr4j0gpbjEXNfzsEmz+ArT8nmICZe0qYuteOa2jns2bQ3oVKgIHVYHDL6NseYgXalR1LYjOFG9rvy5/fWA==";
        
        String lklPrikey = Base64.encode(keyPair.getPrivate().getEncoded());
        String qunaerPubkey = Base64.encode(keyPair.getPublic().getEncoded());
        
        String content = "{\"partner_id\":\"1060319791\",\"change_time\":\"20190718104707\",\"term\":3,\"open_id\":\"T6aqp1mnuh\",\"invest_repay_plan_detail\":{},\"service\":\"LOAN_DETAIL_NOTIFY\",\"repay_plan_detail\":[{\"paid_penalty_amount\":\"0.00\",\"repay_status\":\"FINISH\",\"overdue_date\":\"20190723235959\",\"penalty_amount\":\"0.00\",\"principal\":\"6400.00\",\"interest\":\"99.84\",\"is_overdue\":\"0\",\"paid_principal\":\"6400.00\",\"paid_interest\":\"99.84\",\"term_no\":\"1\"},{\"paid_penalty_amount\":\"0.00\",\"repay_status\":\"FINISH\",\"overdue_date\":\"20190823235959\",\"penalty_amount\":\"0.00\",\"principal\":\"0.00\",\"interest\":\"0.00\",\"is_overdue\":\"0\",\"paid_principal\":\"0.00\",\"paid_interest\":\"0.00\",\"term_no\":\"2\"},{\"paid_penalty_amount\":\"0.00\",\"repay_status\":\"FINISH\",\"overdue_date\":\"20190923235959\",\"penalty_amount\":\"0.00\",\"principal\":\"0.00\",\"interest\":\"0.00\",\"is_overdue\":\"0\",\"paid_principal\":\"0.00\",\"paid_interest\":\"0.00\",\"term_no\":\"3\"}],\"partner\":\"LKLCASH\",\"product_no\":\"cash\",\"loan_no\":\"CAQ20190624233509934cash033099325\",\"service_version\":\"1.0\"}";
        //加密
        PublicKey thirdPubKey = RSAUtil.initPublicKeyByContent(qunaerPubkey);
        // 2.1 请求内容加密,第三方用自己私钥解密
        String reqContent = RSAUtil.encrypt(content, "UTF-8", thirdPubKey);

        PrivateKey lakalaPriKey = RSAUtil.initPrivateKeyByContent(lklPrikey);
        // 2.2 请求内容签名
        String sign = RSAUtil.signWithMD5(content, "UTF-8", lakalaPriKey);
        
        System.out.println("reqContent=" + reqContent);
        System.out.println("sign=" + sign);
        
        
        //解密
        // 1、初始化秘钥
        PrivateKey thirdPriKey = RSAUtil.initPrivateKeyByContent(lklPrikey);
        PublicKey qunarPublicKey = RSAUtil.initPublicKeyByContent(qunaerPubkey);
        // 2.1 解密
        String contentJson = RSAUtil.decrypt(reqContent, "UTF-8", thirdPriKey);
        System.out.println("解密后的数据..." + contentJson);
        // 2.2 验签
        boolean f = RSAUtil.verifyWithMD5(contentJson, sign, "UTF-8", qunarPublicKey);
        System.out.println("验签结果..." + f);
        
        System.out.println("URLEncoder reqContent=" + URLEncoder.encode(reqContent, "UTF-8"));
        System.out.println("URLEncoder sign=" + URLEncoder.encode(sign, "UTF-8"));
    }
    
    private static String doUrlDecodeIfNecessary(String content) {
        try {

            if (StringUtils.isNoneEmpty(content) && content.indexOf("%") > 0) {
                return URLDecoder.decode(content, "UTF-8");

            }
        } catch (Throwable e) {
//            logger.error("urlDecode异常", e);
            System.out.println("urlDecode异常" + e.getMessage());
        }
        return content;
    }
}
