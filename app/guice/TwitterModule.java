package guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.common.Constants;
import play.Play;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 3, 2010
 */
public class TwitterModule extends AbstractModule {
  protected void configure() {
    bind(UserFacade.class);

    final String domain = Play.configuration.getProperty("url.root");
    bind(String.class).annotatedWith(Names.named(Constants.TWITTER_CALLBACK_URI))
      .toInstance("http://" + domain + "/authorize");
  }
}
