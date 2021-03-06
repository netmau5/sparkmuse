package controllers;

import com.google.code.twig.ObjectDatastore;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import net.sparkmuse.data.entity.Feedback;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Invitation;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.BlobService;
import net.sparkmuse.ajax.AjaxResponse;
import net.sparkmuse.discussion.WishSearchRequest;
import org.apache.commons.lang.StringUtils;

public class Application extends SparkmuseController {

  @Inject static ObjectDatastore datastore;

  @Inject static BlobService blobService;

  public static void home() {
    UserVO user = Authorization.getUserFromSession();

    if (null == user) {
      Foundry.index(WishSearchRequest.Filter.RECENT, 1);
    }

    if (user.isUser()) {
      Home.index();
    }
    else if (user.hasFoundryAccess()) {
      Foundry.index(WishSearchRequest.Filter.RECENT, 1);
    }
    else {
      Landing.index();
    }
  }

  public static void logout() {
    UserVO user = Authorization.getUserFromSession();
    session.clear();
    if (null != user && user.isUser()) Landing.index();
    else Foundry.index(WishSearchRequest.Filter.RECENT, 1);
  }
  
  public static void credits(){
    render();
  }

  public static void about() {
    render();
  }

  public static void guidelines() {
    render();
  }

  public static void why() {
    render();
  }

  public static void invitation(String groupName) {
    if (StringUtils.isNotEmpty(groupName)) {
      Invitation invitation = userFacade.findInvitationBy(groupName.replaceAll("-", " ")); //url encoding
      renderTemplate("Application/invitationGroup.html", invitation);
    }
    else {
      render();
    }
  }

  public static void partners() {
    render();
  }

  public static void feedback(String appName) {
    if (appName.equals("Gift.io") || appName.equals("Digest.io") || appName.equals("TextMunch") || appName.equals("Invincibilitee")) {
      render(appName);
    }
    else {
      Feedback feedback = StringUtils.isEmpty(appName) ? null : datastore.load(Feedback.class, appName);
      final UserVO user = Authorization.getUserFromSession();
      if (!feedback.isPrivate() || (null != user && user.isAuthorizedFor(AccessLevel.DIETY))) {
        renderTemplate("/Application/feedback2.html", feedback);
      }
      else {
        Landing.index();
      }
    }
  }

  public static void favicon() throws FileNotFoundException {
    File icon = play.Play.getFile("public/images/favicon.png");
    InputStream is = new FileInputStream(icon);
    response.setHeader("Content-Length", icon.length() + "");
    response.cacheFor("2h");
    response.contentType = "image/x-icon";
    response.direct = is; // renderBinary() will override any caching headers.
  }

  public static void robots() throws FileNotFoundException {
    File icon = play.Play.getFile("public/robots.txt");
    InputStream is = new FileInputStream(icon);
    response.setHeader("Content-Length", icon.length() + "");
    response.cacheFor("2h");
    response.contentType = "text/plain";
    response.direct = is; // renderBinary() will override any caching headers.
  }

  public static void get(String blobKey) {
    redirect(blobService.createServeUrl(blobKey));
  }

  //user denied access to app, tell them we're sorry
  public static void farewell(){
    render();
  }

  public static void farewellFeedback(String medicine) {
    userFacade.farewell(medicine);
    renderJSON(new AjaxResponse());
  }
  
}