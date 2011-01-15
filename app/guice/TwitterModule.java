package guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.common.Constants;
import play.Play;
import play.Logger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 3, 2010
 */
public class TwitterModule extends AbstractModule {
  protected void configure() {
    final String domain = Play.configuration.getProperty("url.root");
    Logger.info("Using property [url.root]: " + domain);
    bind(String.class).annotatedWith(Names.named(Constants.TWITTER_CALLBACK_URI))
      .toInstance("http://" + domain + "/authorize");
  }
}
