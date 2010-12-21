package net.sparkmuse.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author neteller
 * @created: Dec 19, 2010
 */
public class UrlFetch {

  public static String asText(String urlString) {
    try {
      URL url = new URL(urlString);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder response = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      return response.toString();
    } catch (MalformedURLException e) {
      throw newException(e);
    } catch (IOException e) {
      throw newException(e);
    }
  }

  private static RuntimeException newException(Throwable t) {
    return new RuntimeException(t);
  }

}
