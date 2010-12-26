package guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.sparkmuse.common.Constants;

/**
 * @author neteller
 * @created: Dec 26, 2010
 */
public class BlobModule extends AbstractModule {

  private static final String BLOB_SERVER = "http://a.sparkmuse.com";

  protected void configure() {
    bind(String.class).annotatedWith(Names.named(Constants.BLOB_SERVER_SERVE))
      .toInstance(BLOB_SERVER + "/serve/");
    bind(String.class).annotatedWith(Names.named(Constants.BLOB_SERVER_CREATE_UPLOAD_URL))
      .toInstance(BLOB_SERVER + "/createUploadTarget");
  }
}
