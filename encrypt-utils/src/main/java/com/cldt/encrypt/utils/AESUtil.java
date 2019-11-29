package com.cldt.encrypt.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES加密工具类
 */

public class AESUtil {

  private static final String CHARSET = "UTF-8";  //密钥加密算法
  private static final String KEY_ALGORITHM = "AES";  //密钥加密算法
  private static final String AESTYPE = "AES/ECB/PKCS5Padding";

//  /**
//   * 使用默认密钥进行加密，字符集默认UTF-8
//   *
//   * @param originData 明文数据
//   * @return 返回加密后密文
//   */
//  public static String encrypt(String originData) {
//    return encrypt(KEY_STR_DEFAULT, originData,CHARSET);
//  }
  /**
   * 使用默认密钥进行加密,字符集默认UTF-8
   * @param keyStr     密钥
   * @param originData 明文数据
   * @return 返回加密后密文
   */
  public static String encrypt(String keyStr,String originData) throws Exception {
    return encrypt(keyStr, originData,CHARSET);
  }
  /**
   * 加密操作
   *
   * @param keyStr     密钥
   * @param originData 明文数据
   * @param charset    字符集，如UTF-8, GBK, GB2312
   * @return 返回加密后密文
   */
  public static String encrypt(String keyStr, String originData,String charset) throws Exception {
    try {
      if (StringUtils.isBlank(keyStr)) {
        throw new IllegalArgumentException("AESUtil -> encrypt -> param keyStr required not blank");
      }
      if (StringUtils.isBlank(originData)) {
        throw new IllegalArgumentException("AESUtil -> encrypt -> param originData required not blank");
      }
      Cipher cipher = newCipherAndInit(AESTYPE, Cipher.ENCRYPT_MODE, keyStr);
      byte[] data = StringUtils.isEmpty(charset) ? originData.getBytes()
              : originData.getBytes(charset);
      byte[] encrypt = doFinal(cipher, data);
      return new String(Base64.encodeBase64(encrypt));
    }catch (Exception e) {
        throw new Exception("EncodeContent = " + originData + ",charset = " + charset, e);
    }
  }

//  /**
//   * 使用默认密钥进行解密操作,字符集默认UTF-8
//   *
//   * @param encryptData 密文数据
//   * @return 返回解密后明文
//   */
//  public static String decrypt(String encryptData) {
//    return decrypt(KEY_STR_DEFAULT, encryptData,CHARSET);
//  }
  /**
   * 使用默认密钥进行解密操作,字符集默认UTF-8
   * @param keyStr     密钥
   * @param encryptData 密文数据
   * @return 返回解密后明文
   */
  public static String decrypt(String keyStr,String encryptData) throws Exception {
    return decrypt(keyStr, encryptData,CHARSET);
  }

  /**
   * 解密操作
   *
   * @param keyStr      密钥
   * @param encryptData 密文数据
   * @return 返回解密后明文
   */
  public static String decrypt(String keyStr, String encryptData,String charset) throws Exception {

    try {
    if (StringUtils.isBlank(keyStr)) {
      throw new IllegalArgumentException("AESUtil -> decrypt -> param keyStr required not blank");
    }
    if (StringUtils.isBlank(encryptData)) {
      throw new IllegalArgumentException("AESUtil -> decrypt -> param encryptData required not blank");
    }
    Cipher cipher = newCipherAndInit(AESTYPE, Cipher.DECRYPT_MODE, keyStr);
    byte[] encryptedData = StringUtils.isEmpty(charset)
            ? Base64.decodeBase64(encryptData.getBytes())
            : Base64.decodeBase64(encryptData.getBytes(charset));
    byte[] decrypt = doFinal(cipher, encryptedData);
    return new String(decrypt).trim();
    }catch (Exception e) {
      throw new Exception("EncodeContent = " + encryptData + ",charset = " + charset, e);
    }
  }

  private static Cipher newCipherAndInit(String cipherType, int initType, String keyStr)
          throws Exception {
    if (StringUtils.isBlank(keyStr)) {
      throw new IllegalArgumentException("AESUtil -> newCipherAndInit -> param keyStr required not blank");
    }
    if (StringUtils.isBlank(cipherType)) {
      cipherType = AESTYPE;
    }
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(cipherType);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new Exception(String.format("AESUtil -> newCipherAndInit -> Cipher.getInstance({%s}) error", cipherType), e);
    }

    try {
      cipher.init(initType, generateKey(keyStr));
    } catch (InvalidKeyException e) {
      throw new Exception(String.format("AESUtil -> newCipherAndInit -> cipher.init(cipherType:{%d}, key:{%s}) error", initType, keyStr), e);
    }
    return cipher;
  }

  private static byte[] doFinal(Cipher cipher, byte[] data) throws Exception {
    Objects.requireNonNull(cipher, "AESUtil -> doFinal -> cipher required not null");
    if (data == null || data.length <= 0) {
      throw new IllegalArgumentException("AESUtil -> doFinal -> byte array required not blank");
    }
    try {
      return cipher.doFinal(data);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      throw new Exception(String.format("AESUtil -> doFinal -> cipher.doFinal(param: cipher:{%s},data:{%s}) error", cipher, new String(data)), e);
    }
  }

  /**
   * 生成密钥操作
   *
   * @param keyStr 密钥字符串
   */
  private static Key generateKey(String keyStr) {
    if (StringUtils.isBlank(keyStr)) {
      throw new IllegalArgumentException("AESUtil -> generateKey -> key required not blank");
    }
    Key keySpec = new SecretKeySpec(keyStr.getBytes(), KEY_ALGORITHM);
    return keySpec;
  }

}
