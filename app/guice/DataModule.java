package guice;

import com.google.inject.AbstractModule;
import com.google.common.collect.Lists;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.data.*;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.mapper.FieldMapperFactory;
import net.sparkmuse.data.twig.*;
import net.sparkmuse.data.play.PlayCache;
import net.sparkmuse.data.entity.*;

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
    bind(ObjectMapper.class).toInstance(newObjectMapper());
    bind(ObjectDatastore.class).to(PlayAnnotationObjectDatastore.class);
    bind(Cache.class).to(PlayCache.class);
    bind(FieldMapperFactory.class).toInstance(newFieldMapperFactory());
  }

  private ObjectMapper newObjectMapper() {
    //avoid classpath scanning, it is slow on GAE startup
    final FieldMapperFactory fieldMapperFactory = newFieldMapperFactory();
    return new ObjectMapper(
        fieldMapperFactory,
        UserVO.class,
        SparkVO.class,
        PostVO.class
    );
  }

  private FieldMapperFactory newFieldMapperFactory() {
    final FieldMapperFactory fieldMapperFactory = new FieldMapperFactory(Lists.newArrayList(
        new FieldMapperFactory.StandardMapper(),
        new FieldMapperFactory.EnumMapper(),
        new FieldMapperFactory.DateTimeMapper(),
        new FieldMapperFactory.ClassMapper()
    ));
    return fieldMapperFactory;
  }

}
