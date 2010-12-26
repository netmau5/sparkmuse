package net.sparkmuse.data;

import com.google.code.twig.annotation.Id;
import com.google.inject.name.Named;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import net.sparkmuse.data.entity.OwnedEntity;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.twig.DatastoreService;
import net.sparkmuse.common.Constants;
import net.sparkmuse.common.UrlFetch;

import java.util.UUID;


/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public class BlobService {

  @Inject
  @Named(Constants.BLOB_SERVER_CREATE_UPLOAD_URL)
  private String uploadTargetUrl;

  @Inject
  @Named(Constants.BLOB_SERVER_SERVE)
  private String endpointBlobServe;

  private DatastoreService datastore;

  @Inject
  public BlobService(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public void recordUpload(UserVO user, String blobKey) {
    datastore.store(new Blob(blobKey, Blob.State.UPLOADED, user));
  }

  //@todo
  public void recordActive(UserVO user, String blobKey) {
    datastore.store(new Blob(blobKey, Blob.State.ACTIVE, user));
  }

  //@todo
  public void recordDelete(UserVO user, String blobKey) {
    datastore.store(new Blob(blobKey, Blob.State.DELETED, user));
  }

  public UploadTargetResponse createUploadTarget(String uploadHandler) {
    final String uuid = UUID.randomUUID().toString();
    return new UploadTargetResponse(
        UrlFetch.asText(uploadTargetUrl + "?uploadHandler=" + uploadHandler + "&uuid=" + uuid),
        uuid
    );
  }

  public String createServeUrl(String blobKey) {
    return endpointBlobServe + blobKey;
  }

  public static class Blob extends OwnedEntity<Blob> {
    String blobKey;
    DateTime created;
    State state;

    private Blob(String blobKey, State state, UserVO user) {
      this.state = state;
      this.blobKey = blobKey;
      this.created = new DateTime();
      setAuthor(user);
    }

    enum State {
      UPLOADED,
      ACTIVE,
      DELETED
    }

    public String getBlobKey() {
      return blobKey;
    }

    public DateTime getCreated() {
      return created;
    }

    public State getState() {
      return state;
    }
  }

  public static class UploadTargetResponse {
    private String url;
    private String uuid;

    private UploadTargetResponse(String url, String uuid) {
      this.url = url;
      this.uuid = uuid;
    }

    public String getUrl() {
      return url;
    }

    public String getUuid() {
      return uuid;
    }
  }

}
