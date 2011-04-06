package net.sparkmuse.embed;

import net.sparkmuse.common.UrlFetch;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class Embedly {

  private static String ENDPOINT = "http://api.embed.ly/1/oembed?";

  public static Embed lookup(String url) {
    String response = UrlFetch.asText(buildUrl(url));
    return decodeResponse(response);
  }

  private static Embed decodeResponse(String response) {
    try {
      return new ObjectMapper().readValue(response, Embed.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String buildUrl(String url) {
    try {
      return ENDPOINT + "format=json&url=" + URLEncoder.encode(url, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}
