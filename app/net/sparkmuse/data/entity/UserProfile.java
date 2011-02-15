package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Parent;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;
import com.google.common.collect.Lists;
import play.data.validation.CheckWith;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.common.NullTo;
import play.data.validation.URL;
import play.data.validation.Email;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Jan 9, 2011
 */
public class UserProfile extends Entity<UserProfile> {

  @Parent
  private UserVO user;

  private String name;
  private String location;

  @URL
  private String website;

  @Email
  private String email;

  @Type(Text.class)
  private String bio;

  @Type(Text.class)
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayBio;

  private List<Expertise> expertises; //yea, i know, whatever

  private Integer invites;

  private boolean peopleElgible; //has met requirements to be shown on people page

  public UserProfile() {
    this.expertises = Lists.newArrayList();
  }

  public static UserProfile newProfile(UserVO storedNewUser) {
    final UserProfile p = new UserProfile();
    p.user = storedNewUser;
    return p;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setExpertises(List<Expertise> expertises) {
    this.expertises = expertises;
  }

  public List<Expertise> getExpertises() {
    return NullTo.empty(expertises);
  }

  public Integer getInvites() {
    return invites;
  }

  public void setInvites(Integer invites) {
    this.invites = invites;
  }

  public UserVO getUser() {
    return user;
  }

  public void setUser(UserVO user) {
    this.user = user;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getDisplayBio() {
    return displayBio;
  }

  public void setDisplayBio(String displayBio) {
    this.displayBio = displayBio;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean hasEmail() {
    return StringUtils.isNotBlank(email);
  }

  public boolean isPeopleElgible() {
    return peopleElgible;
  }

  public void setPeopleElgible(boolean peopleElgible) {
    this.peopleElgible = peopleElgible;
  }
  
}
