package guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.sparkmuse.common.Constants;
import play.Play;

/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public class BlobModule extends AbstractModule {

  protected void configure() {
    String blobServerDomain = Play.configuration.getProperty("url.blob-server");

    bind(String.class).annotatedWith(Names.named(Constants.BLOB_SERVER_SERVE))
      .toInstance("http://" + blobServerDomain + "/serve/");
    bind(String.class).annotatedWith(Names.named(Constants.BLOB_SERVER_CREATE_UPLOAD_URL))
      .toInstance("http://" + blobServerDomain + "/createUploadTarget");
  }
}
