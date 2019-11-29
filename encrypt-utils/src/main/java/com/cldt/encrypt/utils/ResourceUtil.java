package com.cldt.encrypt.utils;

public class ResourceUtil {

  public static void closeQuietly(AutoCloseable... closes) {
    if (closes != null && closes.length > 0) {
      for (AutoCloseable item : closes) {
        try {
          if (item != null) {
             item.close();
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
