package net.sparkmuse.embed;

import net.sparkmuse.common.UrlFetch;
import net.sparkmuse.common.NullTo;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.type.TypeReference;
import play.Logger;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.base.Function;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class Embedly {

  private static String LOOKUP_ENDPOINT = "http://api.embed.ly/1/oembed?";
  private static String SERVICE_ENDPOINT = "http://api.embed.ly/v1/api/services";
  private static List<Pattern> VALID_PATTERNS;

  public static Embed lookup(String url) {
    if (isValidUrlPattern(url)) {
      String response = UrlFetch.asText(buildUrl(url));
      Logger.info("Embedly response for URL [" + url + "]\nEmbed [" + response + "]");
      return decodeResponse(response);
    }
    return null;
  }

  private static boolean isValidUrlPattern(String url) {
    if (null == VALID_PATTERNS) {
      VALID_PATTERNS = Lists.newArrayList();
      for (EmbedService service: retrieveServices()) {
        for (String regex: NullTo.empty(service.getRegexes())) {
          VALID_PATTERNS.add(Pattern.compile(regex.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*")));
        }
      }
    }

    for (Pattern p: VALID_PATTERNS) {
      if (p.matcher(url).matches()) return true;
    }

    return false;
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
      return LOOKUP_ENDPOINT + "format=json&url=" + URLEncoder.encode(url, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<EmbedService> retrieveServices() {
    try {
      return new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(UrlFetch.asText(SERVICE_ENDPOINT), new TypeReference<List<EmbedService>>(){});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
