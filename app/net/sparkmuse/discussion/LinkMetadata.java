package net.sparkmuse.discussion;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author neteller
 * @created: Dec 8, 2010
 */
public class LinkMetadata {

  private static final LinkMetadata NO_METADATA_FOUND = new LinkMetadata();
  private static final Pattern HTML_TITLE = Pattern.compile(".*<title>(.*)</title>.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
  private static final Pattern HTML_META_DESCRIPTION = Pattern.compile(".*(<meta.*name=[\"']description[\"'].*/?>).*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
  private static final Pattern HTML_META_CONTENT = Pattern.compile("<meta.*content=[\"'](.*)[\"'].*/?>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
  private static final Pattern HTML_HEADER_CLOSE = Pattern.compile("</head>", Pattern.CASE_INSENSITIVE);

  private final String title;
  private final String description;

  private LinkMetadata() {
    this.title = null;
    this.description = null;
  }

  public LinkMetadata(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public static LinkMetadata lookup(String urlString) {
    try {
      URL url = new URL(urlString);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      String line, content = "";
      int i = 0;

      while ((line = reader.readLine()) != null && i++ < 500) {
        content += line;
        if (HTML_HEADER_CLOSE.matcher(line).matches()) break;
      }
      reader.close();

      return newMetadata(content);
    } catch (MalformedURLException e) {
      return NO_METADATA_FOUND;
    } catch (IOException e) {
      return NO_METADATA_FOUND;
    }
  }

  static LinkMetadata newMetadata(String content) {
    String title = null, description = null;

    final Matcher titleMatcher = HTML_TITLE.matcher(content);
    if (titleMatcher.matches()) {
      title = titleMatcher.group(1);
      if (null != title) {
        title = title.replaceAll("\\s+", " ").trim();
      }
    }
    final Matcher metaMatcher = HTML_META_DESCRIPTION.matcher(content);
    if (metaMatcher.matches()) {
      final Matcher contentMatcher = HTML_META_CONTENT.matcher(metaMatcher.group(1));
      if (contentMatcher.matches()) {
        description = contentMatcher.group(1);
        if (null != description) {
          description = description.replaceAll("\\s", " ").trim();
        }
      }
    }

    if (null == title && null == description) return NO_METADATA_FOUND;
    return new LinkMetadata(title, description);
  }

}
