package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.*;
import com.google.appengine.api.datastore.Text;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author neteller
 * @created: Dec 20, 2010
 */
public class Feedback {

  @Id private String key;
  private String title;
  @Type(Text.class) private String content;  //content as entered by user
  @Type(Text.class) private String displayContent; //what we will show
  private List<String> imageKeys;
  private boolean isPrivate;

  private static final String REGEX_IMG_ID = "\\$\\{([\\w\\-;#&]*)\\}";
  static final String IMG_TOKEN = "${image-key}";
  static final String IMG = "<div class=\"lightbox-wrapper\"><img src=\"http://a.sparkmuse.com/serve/" + IMG_TOKEN + "\" alt=\"Landing Page\" class=\"lightbox\" /></div>";

  public boolean isPrivate() {
    return isPrivate;
  }

  public String getKey() {
    return key;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getDisplayContent() {
    return displayContent;
  }

  public List<String> getImageKeys() {
    return imageKeys;
  }

  public static Feedback newInstance(String key, String title, String content, String displayContent, boolean isPrivate, List<String> imageKeys) {
    final Feedback f = new Feedback();
    f.key = key;
    f.title = title;
    f.content = content;
    f.displayContent = addImages(displayContent);
    f.imageKeys = imageKeys;
    f.isPrivate = isPrivate;
    return f;
  }

  static String addImages(final String content) {
    final Pattern p = Pattern.compile(REGEX_IMG_ID);
    final Matcher m = p.matcher(content);
    String toReturn = content;
    while (m.find()) {
      toReturn = toReturn.replace(m.group(0), IMG.replace(IMG_TOKEN, m.group(1)));
    }
    return toReturn;
  }

}
