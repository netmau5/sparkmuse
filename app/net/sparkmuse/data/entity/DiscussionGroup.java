package net.sparkmuse.data.entity;

import play.data.validation.Required;
import play.data.validation.CheckWith;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import com.google.common.collect.Lists;
import net.sparkmuse.client.NoScriptCheck;

import java.util.List;

import org.joda.time.DateTime;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author neteller
 * @created: Apr 9, 2011
 */
public class DiscussionGroup extends Entity<DiscussionGroup> {

  public static DiscussionGroup GENERAL_DISCUSSION = new DiscussionGroup("General");

  @Required
  private String name;

  @Required
  @Type(Text.class)
  @JsonIgnore
  private String info;

  @Required
  @Type(Text.class)
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayInfo;

  @JsonIgnore
  private List<UserVO> admins;

  @JsonIgnore
  private DateTime created;

  public DiscussionGroup() {
    this.admins = Lists.newArrayList();
    this.created = new DateTime();
  }

  public DiscussionGroup(String name) {
    this();
    this.name = name;
  }

  public String getDisplayInfo() {
    return displayInfo;
  }

  public void setDisplayInfo(String displayInfo) {
    this.displayInfo = displayInfo;
  }

  public List<UserVO> getAdmins() {
    return admins;
  }

  public void setAdmins(List<UserVO> admins) {
    this.admins = admins;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

}
