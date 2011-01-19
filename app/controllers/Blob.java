package controllers;

import play.mvc.Router;
import play.mvc.With;
import filters.AuthorizationFilter;
import net.sparkmuse.common.Cache;
import net.sparkmuse.data.BlobService;
import net.sparkmuse.data.entity.UserVO;

import javax.inject.Inject;

/**
 * Handler for uploading blobs.  Serving a blob is performed in Application.
 *
 * @author neteller
 * @created: Dec 15, 2010
 */
@With(AuthorizationFilter.class)
public class Blob extends SparkmuseController {

  @Inject static BlobService blobService;
  @Inject static Cache cache;

  public static void createUploadTarget() {
    final Router.ActionDefinition definition = Router.reverse("Blob.handleUpload");
    definition.absolute();
    renderJSON(blobService.createUploadTarget(definition.url));
  }

  public static void handleUpload(String blobKey, String uuid) {
    final UserVO user = Authorization.getUserFromSessionOrAuthenticate(true);
    cache.safeSet(user.getId() + "|" + uuid, blobKey);
    blobService.recordUpload(user, blobKey);
    renderText(""); //blank response
  }

  public static void lastUpload(String uuid) {
    Object key = cache.get(Authorization.getUserFromSessionOrAuthenticate(true).getId() + "|" + uuid);
    if (null == key) {
      response.status = 404; //not found
    }
    renderJSON(key);
  }

}
