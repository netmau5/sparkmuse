package controllers;

import play.mvc.With;
import play.data.validation.Validation;
import filters.AdminAuthorizationFilter;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.user.UserFacade;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Collection;

/**
 * @author neteller
 * @created: Dec 20, 2010
 */
@With(AdminAuthorizationFilter.class)
public class Admin extends SparkmuseController {

  @Inject static ObjectDatastore datastore;
  @Inject static UserFacade userFacade;

  public static final void manageFeedback(String key) {
    Feedback feedback = StringUtils.isEmpty(key) ? null : datastore.load(Feedback.class, key);
    List<Feedback> feedbacks = Lists.newArrayList(datastore.find(Feedback.class));
    if (null != feedback) {
      render(feedbacks, feedback);
    }
    else {
      render(feedbacks);
    }
  }

  public static final void saveFeedback(String key, String title, String content, String displayContent, boolean isPrivate, List<String> imageKeys) {
    if (Validation.hasErrors()) {
      renderJSON(new ValidationErrorAjaxResponse(validation.errorsMap()));
    }
   }

  public static void users() {
    Collection<UserProfile> profiles = userFacade.getAllProfiles();
    render(profiles);
  }

  public static void user(String userName) {
    final UserProfile profile = userFacade.getUserProfile(userName);
    render(profile);
  }

  public static void createUser(String userName) {
    userFacade.createUser(userName);
    users();
  }

  public static void updateUser(long userId, AccessLevel accessLevel, int invites) {
    userFacade.updateUser(userId, accessLevel, invites);
    users();
  }

}
