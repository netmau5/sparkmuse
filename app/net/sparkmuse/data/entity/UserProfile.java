package net.sparkmuse.data.entity;

import com.google.code.twig.annotation.Parent;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;
import play.data.validation.CheckWith;
import net.sparkmuse.client.NoScriptCheck;

/**
 * @author neteller
 * @created: Jan 9, 2011
 */
public class UserProfile extends Entity<UserProfile> {

  @Parent
  private UserVO user;

  @Type(Text.class)
  private String bio;

  @Type(Text.class)
  @CheckWith(value= NoScriptCheck.class, message="validation.noscript")
  private String displayBio;

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

  public static UserProfile newProfile(UserVO storedNewUser) {
    final UserProfile p = new UserProfile();
    p.user = storedNewUser;
    return p;
  }
}
