package com.cldt.encrypt.utils;


import org.apache.commons.lang3.StringUtils;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

public class SignUtil {

  public static final String RSA = "RSA";
  public static final String DSA = "DSA";
  public static final String MD5 = "MD5";
  private static final String DEFAULT_SIGN_ALGORITHM_PREFIX = "SHA1With";
  private static final List<String> SIGN_TYPES = Arrays.asList(RSA, DSA, MD5);

  public static boolean isValidSignType(String signType) {
    return SIGN_TYPES.contains(signType);
  }

  private static void checkSignTypeIsSupport(String signType) {
    if (StringUtils.isBlank(signType)) {
      throw new IllegalArgumentException("SignUtil -> signType required not blank");
    }
    if (!isValidSignType(signType)) {
      throw new UnsupportedOperationException(String.format("SignUtil -> checkSignTypeIsSupport -> signType :{%s} is not support", signType));
    }
  }

  private static Signature getSignature(String signType) {
    if (StringUtils.isBlank(signType)) {
      throw new IllegalArgumentException("SignUtil -> signType required not blank");
    }
    checkSignTypeIsSupport(signType);
    Signature signature = null;
    try {
      signature = Signature.getInstance(DEFAULT_SIGN_ALGORITHM_PREFIX + signType);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(String.format("SignUtil-> getSignature -> Signature.getInstance(algorithm:{%s}) error", DEFAULT_SIGN_ALGORITHM_PREFIX + signType), e);
    }
    return signature;
  }

  private static void initKey(Signature signature, Key key) {
    Objects.requireNonNull(signature);
    Objects.requireNonNull(key);
    try {
      if (key instanceof PrivateKey) {
        signature.initSign((PrivateKey) key);
      } else if (key instanceof PublicKey) {
        signature.initVerify((PublicKey) key);
      } else {
        throw new IllegalArgumentException(String.format("SignUtil -> initKey -> parameter key : {%s} illegal", key));
      }
    } catch (InvalidKeyException e) {
      throw new RuntimeException("SignUtil-> initSign -> Signature exception.", e);
    }
  }

  private static void updateSignature(Signature signature, byte[] data) {
    Objects.requireNonNull(signature);
    if (data == null || data.length <= 0) {
      throw new IllegalArgumentException("SignUtil ->  sign update data required not blank");
    }
    try {
      signature.update(data);
    } catch (SignatureException e) {
      throw new RuntimeException(String.format("SignUtil -> updateSign -> Signature.update(data:{})", new String(data)), e);
    }
  }

  private static Signature newSignatureAndInitAndUpdate(String signType, Key key, byte[] data) {
    Signature signature = getSignature(signType);
    initKey(signature, key);
    updateSignature(signature, data);
    return signature;
  }

  /**
   *
   * @param signType
   * @param privateKey
   * @param data
   * @return
   */
  public static String genSign(final String signType, final PrivateKey privateKey, final byte[] data) {
    Signature signature = newSignatureAndInitAndUpdate(signType, privateKey, data);
    try {
      byte[] sign = signature.sign();
      return DatatypeConverter.printBase64Binary(sign);
    } catch (SignatureException e) {
      throw new RuntimeException(String.format("SignUtil -> genSign -> Signature.sign error,signType:{%s},PrivateKey:{%s},data:{%s}", signType, privateKey, new String(data)), e);
    }
  }

  /**
   *
   * @param signType
   * @param privateKeyString
   * @param content
   * @return
   */
  public static String genSign(final String signType, String privateKeyString, String content) {

    PrivateKey privateKey = getPrivateKeyFromString(signType,privateKeyString);

    try {
      Signature signature = newSignatureAndInitAndUpdate(signType, privateKey, content.getBytes("UTF-8"));
      byte[] sign = signature.sign();
      return DatatypeConverter.printBase64Binary(sign);
    } catch (SignatureException e) {
      throw new RuntimeException(String.format("SignUtil -> genSign -> Signature.sign error,signType:{%s},PrivateKey:{%s},data:{%s}", signType, privateKey, content), e);
    }catch (UnsupportedEncodingException e) {
      throw new RuntimeException(String.format("SignUtil -> genSign -> Signature.sign error,signType:{%s},PrivateKey:{%s},data:{%s}", signType, privateKey, content), e);

    }
  }

  /**
   *
   * @param signType
   * @param publicKeyString
   * @param content
   * @param sign
   * @return
   */
  public static boolean verifySign(final String signType, String publicKeyString, String content, final String sign) {

    PublicKey  publicKey = getPublicKeyFromString(signType,publicKeyString);
    try {
      Signature signature = newSignatureAndInitAndUpdate(signType, publicKey, content.getBytes("UTF-8"));
      byte[] bytes = DatatypeConverter.parseBase64Binary(sign);
      return signature.verify(bytes);
    } catch (SignatureException e) {
      throw new RuntimeException(String.format("SignUtil -> verifySign -> Signature.verify(data:{%s}) error ", new String(content)), e);
    }catch (UnsupportedEncodingException e) {
      throw new RuntimeException(String.format("SignUtil -> verifySign -> Signature.verify(data:{%s}) error ", new String(content)), e);
    }
  }

  /**
   *
   * @param signType
   * @param publicKey
   * @param data
   * @param signStr
   * @return
   */
  public static boolean verifySign(final String signType, PublicKey publicKey, final byte[] data, final String signStr) {
    Signature signature = newSignatureAndInitAndUpdate(signType, publicKey, data);
    byte[] bytes = DatatypeConverter.parseBase64Binary(signStr);
    try {
      return signature.verify(bytes);
    } catch (SignatureException e) {
      throw new RuntimeException(String.format("SignUtil -> verifySign -> Signature.verify(data:{%s}) error ", new String(signStr)), e);
    }
  }


  public static PrivateKey getPrivateKeyFromString(final String signType, final String keyStr) {
    if (StringUtils.isBlank(signType)) {
      throw new IllegalArgumentException("SignUtil -> getPrivateKeyFromString -> signType required not blank");
    }
    if (StringUtils.isBlank(keyStr)) {
      throw new IllegalArgumentException("SignUtil -> getPrivateKeyFromFile -> keyStr required not blank");
    }
    byte[] keyBytes = DatatypeConverter.parseBase64Binary(keyStr);
    KeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(signType);
      return keyFactory.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(String.format("SignUtil -> getPrivateKeyFromString -> error,signType:{%s},keyStr:{%s}", signType, keyStr), e);
    }
  }


  public static PublicKey getPublicKeyFromString(final String signType, final String keyStr) {
    if (StringUtils.isBlank(signType)) {
      throw new IllegalArgumentException("SignUtil -> getPublicKeyFromString -> signType required not blank");
    }
    if (StringUtils.isBlank(keyStr)) {
      throw new IllegalArgumentException("SignUtil -> getPublicKeyFromString -> keyStr required not blank");
    }
    byte[] keyBytes = DatatypeConverter.parseBase64Binary(keyStr);
    KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(signType);
      return keyFactory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(String.format("SignUtil -> getPublicKeyFromString -> error,signType:{%s},keyStr:{%s}", signType, keyStr), e);
    }
  }
  public static PrivateKey getPrivateKeyFromFile(final String signType, final String signFile) {
    String keyStr = loadKeyFile(signFile);
    return getPrivateKeyFromString(signType, keyStr);
  }
  private static String loadKeyFile(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      throw new IllegalArgumentException("SignUtil ->  fileName required not blank");
    }
    InputStream inputStream = SignUtil.class.getClassLoader().getResourceAsStream(fileName);
    if (inputStream == null) {
      throw new RuntimeException("SignUtil -> key file not found");
    }
    String keyStr;
    try {
      byte[] bytes = new byte[inputStream.available()];
      inputStream.read(bytes);
      keyStr = new String(bytes);
      keyStr = keyStr.replaceAll("---.*---", "");
      keyStr.trim();
    } catch (IOException ioe) {
      throw new RuntimeException(String.format("SignUtil -> loadKeyFile -> error,fileName:{%s}", fileName), ioe);
    } finally {
      ResourceUtil.closeQuietly(inputStream);
    }
    return keyStr;
  }
  public static PublicKey getPublicKeyFromFile(final String signType, final String signFile) {
    String keyStr = loadKeyFile(signFile);
    return getPublicKeyFromString(signType, keyStr);
  }


}
