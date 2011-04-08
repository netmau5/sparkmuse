package net.sparkmuse.embed;

import org.codehaus.jackson.annotate.JsonProperty;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

/**
 * OEmbed data provided by Embed.ly
 *
 * @author neteller
 * @created: Apr 5, 2011
 */
public class Embed {

  //required fields
  @JsonProperty("contentType")
  private ContentType contentType;

  @JsonProperty("version")
  private String version;

  //optional fields

  @JsonProperty("title")
  private String title;

  @JsonProperty("author_name")
  private String authorName;

  @JsonProperty("author_url")
  private String authorUrl;

  @JsonProperty("provider_name")
  private String providerName;

  @JsonProperty("provider_url")
  private String providerUrl;

  @JsonProperty("thumbnail_url")
  private String thumbnailUrl;

  @JsonProperty("thumbnail_width")
  private Integer thumbnailWidth;

  @JsonProperty("thumbnail_height")
  private Integer thumbnailHeight;

  @JsonProperty("description")
  @Type(Text.class)
  private String description;

  @JsonProperty("url")
  private String url;

  //photo type, all fields required

  @JsonProperty("url")
  private String imageUrl;

  @JsonProperty("width")
  private Integer width;

  @JsonProperty("height")
  private Integer height;

  //video or rich type, all fields required, includes with/height from above

  @Type(Text.class)
  @JsonProperty("html")
  private String html;

  //these are lowercase to make for easier deserialization according to response api
  public enum ContentType {
    photo,
    video,
    link,
    rich
  }

  public ContentType getType() {
    return contentType;
  }

  public void setType(ContentType contentType) {
    this.contentType = contentType;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public String getAuthorUrl() {
    return authorUrl;
  }

  public void setAuthorUrl(String authorUrl) {
    this.authorUrl = authorUrl;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public String getProviderUrl() {
    return providerUrl;
  }

  public void setProviderUrl(String providerUrl) {
    this.providerUrl = providerUrl;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public Integer getThumbnailWidth() {
    return thumbnailWidth;
  }

  public void setThumbnailWidth(Integer thumbnailWidth) {
    this.thumbnailWidth = thumbnailWidth;
  }

  public Integer getThumbnailHeight() {
    return thumbnailHeight;
  }

  public void setThumbnailHeight(Integer thumbnailHeight) {
    this.thumbnailHeight = thumbnailHeight;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  @Override
  public String toString() {
    return "Embed{" +
        "title='" + title + '\'' +
        ", contentType=" + contentType +
        '}';
  }
}
