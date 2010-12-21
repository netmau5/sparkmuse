package net.sparkmuse.common;

import java.io.*;

/**
 * @author neteller
 * @created: Dec 17, 2010
 */
public class InputStreams {

  public static String convertToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the
     * Reader.read(char[] buffer) method. We iterate until the
     * Reader return -1 which means there's no more data to
     * read. We use the StringWriter class to produce the string.
     */
    if (is != null) {
      Writer writer = new StringWriter();

      char[] buffer = new char[1024];
      try {
        try {
          Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
          int n;
          while ((n = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, n);
          }
        } finally {
          is.close();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return writer.toString();
    } else {
      return "";
    }
  }

}
