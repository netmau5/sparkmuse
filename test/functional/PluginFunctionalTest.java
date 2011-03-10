package functional;

import play.test.FunctionalTest;
import play.Play;
import play.PlayPlugin;
import play.modules.gae.GAEPlugin;
import org.junit.Before;
import org.junit.After;
import com.google.apphosting.api.ApiProxy;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.data.twig.DatastoreService;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public abstract class PluginFunctionalTest extends FunctionalTest {

  public ObjectDatastore datastore;
  public DatastoreService datastoreService;

  static {
    File root = new File("/Users/neteller/dev/sparkmuse");
    root.canWrite();
    Play.init(root, "FunctionTests");
  }

  @Before
  public void setup(){
    for(PlayPlugin plugin : Play.plugins) {
      if(plugin.getClass().getSimpleName().equals("GAEPlugin")) {
        final GAEPlugin gaePlugin = (GAEPlugin) plugin;
        if (null == gaePlugin.devEnvironment) {
          Play.loadPlugins();
        }
        ApiProxy.setEnvironmentForCurrentThread(gaePlugin.devEnvironment);
        break;
      }
    }

    datastore = FunctionalTestUtils.getInstance(ObjectDatastore.class);
    datastoreService = FunctionalTestUtils.getInstance(DatastoreService.class);
    FunctionalTestUtils.deleteAll();
  }

  @After
  public void tearDown(){
    FunctionalTestUtils.deleteAll();
    datastore = null;
  }

}
