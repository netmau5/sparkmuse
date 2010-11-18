package guice;

import net.sparkmuse.common.Constants;
import com.google.inject.name.Names;
import play.Play;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 4, 2010
 */
public class LocalTwitterModule extends TwitterModule {

  @Override
  protected void configure() {
    if (Play.mode == Play.Mode.DEV) {
      bind(String.class).annotatedWith(Names.named(Constants.TWITTER_CALLBACK_URI))
        .toInstance("http://71.12.176.17:9000/authorize");
    }
  }
}
