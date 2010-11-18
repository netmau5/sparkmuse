package guice;

import com.google.inject.AbstractModule;
import com.google.appengine.api.datastore.*;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.StoreCommand;
import com.vercer.engine.persist.FindCommand;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import net.sparkmuse.data.*;
import net.sparkmuse.data.twig.TwigUserDao;
import net.sparkmuse.data.twig.TwigCacheDao;
import net.sparkmuse.data.twig.TwigSparkDao;
import net.sparkmuse.data.twig.TwigPostDao;
import net.sparkmuse.data.play.PlayCache;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.PostVO;
import net.sparkmuse.data.entity.VoteVO;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.gae.DatastoreWriteThruCacheService;

import java.util.Map;
import java.util.Collection;
import java.lang.reflect.Type;

import play.modules.twig.PlayAnnotationObjectDatastore;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(PostDao.class).to(TwigPostDao.class);
    bind(SparkDao.class).to(TwigSparkDao.class);
    bind(UserDao.class).to(TwigUserDao.class);
    bind(CacheDao.class).to(TwigCacheDao.class);
    bind(WriteThruCacheService.class).to(DatastoreWriteThruCacheService.class);
    bind(ObjectMapper.class).toInstance(newObjectMapper());
    bind(ObjectDatastore.class).to(PlayAnnotationObjectDatastore.class);
    bind(Cache.class).to(PlayCache.class);
  }

  private ObjectMapper newObjectMapper() {
    //avoid classpath scanning, it is slow on GAE startup
    return new ObjectMapper(
        UserVO.class,
        SparkVO.class,
        PostVO.class,
        VoteVO.class
    );
  }

  
}
