package controllers;

import play.mvc.With;
import play.data.validation.Validation;
import play.data.validation.Required;
import play.data.validation.Valid;
import filters.AdminAuthorizationFilter;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.ajax.ValidationErrorAjaxResponse;
import net.sparkmuse.ajax.JsonAjaxResponse;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.common.Reflections;
import net.sparkmuse.mail.MailFacade;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import com.google.appengine.api.datastore.Query;

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
  @Inject static MailFacade mailFacade;

  public static void home() {
    render();
  }

  public static void manageFeedback(String key) {
    Feedback feedback = StringUtils.isEmpty(key) ? null : datastore.load(Feedback.class, key);
    List<Feedback> feedbacks = Lists.newArrayList(datastore.find(Feedback.class));
    if (null != feedback) {
      render(feedbacks, feedback);
    }
    else {
      render(feedbacks);
    }
  }

  public static void saveFeedback(String key, String title, String content, String displayContent, boolean isPrivate, List<String> imageKeys) {
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



  public static void invitations() {
    List<Invitation> invitations = datastore.find().type(Invitation.class)
        .addSort("created", Query.SortDirection.ASCENDING)
        .returnAll()
        .now();

    int used = Iterables.size(Iterables.filter(invitations, new Predicate<Invitation>(){
      public boolean apply(Invitation invitation) {
        return invitation.isUsed();
      }
    }));

    renderTemplate("Admin/displayInvitationCodes.html", invitations, used);
  }

  public static void editInvitation(Long id) {
    Invitation invitation = datastore.load(Invitation.class, id);
    render(invitation);
  }

  public static void updateInvitation(Invitation invitation) {
    Invitation existingInvite = datastore.load(Invitation.class, invitation.getId());
    datastore.update(Reflections.overlay(existingInvite, invitation));
    editInvitation(invitation.getId());
  }

  public static void manageNotifications() {
    render();
  }

  public static void addNotification(@Required String userName, @Required String displayNotification) {
    UserVO user = userFacade.addNotification(userName, displayNotification);
    renderJSON(new JsonAjaxResponse(user));
  }

  public static void getUser(String userName) {
    UserProfile userProfile = userFacade.getUserProfile(userName);
    renderJSON(new JsonAjaxResponse(null != userProfile ? userProfile.getUser() : null));
  }

  //EMAIL

  public static void emails() {
    List<Mailing> mailings = mailFacade.getAllMailings();
    render(mailings);
  }

  public static void createEmail() {
    renderTemplate("Admin/email.html");
  }

  public static void editEmail(Long id) {
    Mailing mailing = mailFacade.getMailingBy(id);
    renderTemplate("Admin/email.html", mailing);
  }

  public static void saveEmail(@Valid Mailing mailing) {
    Mailing savedMailing = mailFacade.save(mailing);
    Admin.editEmail(savedMailing.getId());
  }

}
