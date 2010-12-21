package controllers;

import play.mvc.Router;
import play.mvc.With;
import filters.AuthorizationFilter;
import net.sparkmuse.common.UrlFetch;
import net.sparkmuse.common.Cache;

import javax.inject.Inject;

/**
 * Handler for uploading blobs.  Serving a blob is performed in Application.
 *
 * @author neteller
 * @created: Dec 15, 2010
 */
@With(AuthorizationFilter.class)
public class Blob extends SparkmuseController {

  public static final String BLOB_SERVER = "http://a.sparkmuse.com";

  @Inject static Cache cache;

  public static void createUploadTarget() {
    renderText(uploadTarget());
  }

  public static void handleUpload(String blobKey) {
    cache.put(Authorization.getUserFromSessionOrAuthenticate(true).getId() + "|LastUpload", blobKey);
    renderText(""); //blank response
  }

  public static void lastUpload() {
    renderText(cache.get(Authorization.getUserFromSessionOrAuthenticate(true).getId() + "|LastUpload"));
  }

  private static String uploadTarget() {
    final Router.ActionDefinition definition = Router.reverse("Blob.handleUpload");
    definition.absolute();
    return UrlFetch.asText(BLOB_SERVER + "/createUploadTarget?uploadHandler=" + definition.url);
  }

}
