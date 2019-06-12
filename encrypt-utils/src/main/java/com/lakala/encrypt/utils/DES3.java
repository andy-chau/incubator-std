package com.lakala.encrypt.utils;



import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import com.alibaba.fastjson.JSONObject;

public class DES3 {

    public static String encryptDES3(String encryptString, String encryptKey, String iV)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(encryptKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(encryptString.getBytes("utf-8"));
        return Base64.encode(encryptData);

    }

    public static String decryptDES3(String encryptText, String seckey, String iV) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(seckey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
        return new String(decryptData, "utf-8");
    }

    // 3DESECB加密
    public static String encrypt3DESECB(String src, String key) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte[] b = cipher.doFinal(src.getBytes());
        return Base64.encode(b).replaceAll("\r", "").replaceAll("\n", "");

    }

    // 3DESECB解密
    public static String decrypt3DESECB(String src, String key) throws Exception {
        byte[] bytesrc = Base64.decode(src);
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        final byte[] retByte = cipher.doFinal(bytesrc);

        return new String(retByte);
    }

    public static void main(String args[]) {
        String IV = "lakala68";
        String KEY = "hankoubank@&&abc$$168168";
        try {
            JSONObject json = new JSONObject();
            json.put("name", "赖洗的");
            json.put("lx", 1);
            String desStr = DES3.encryptDES3(json.toJSONString(), KEY, IV);
            System.out.println(desStr);
            String decryptStr = DES3.decryptDES3(desStr, KEY, IV);
            System.out.println(decryptStr);

            System.out.println(System.currentTimeMillis() / 1000);
            System.out.println(DES3.encryptDES3("aaaa1111", "shoukuanbao&&abc$$168168", "lakala68"));
            System.out.println(DES3.decryptDES3("H1uITsBIT3mp6xf/VvD5Bg==", "shoukuanbao&&abc$$168168", "lakala68"));
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
