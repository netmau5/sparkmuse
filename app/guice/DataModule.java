package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Inject;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.code.twig.conversion.SpecificConverter;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.data.*;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.mapper.FieldMapperFactory;
import net.sparkmuse.data.twig.*;
import net.sparkmuse.data.play.PlayCache;
import net.sparkmuse.data.entity.*;
import net.sparkmuse.data.gae.DatastoreWriteThruCacheService;
import net.sparkmuse.common.CacheKey;

import java.util.*;

import play.modules.twig.PlayAnnotationObjectDatastore;
import org.joda.time.DateTime;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
    bind(FieldMapperFactory.class).toInstance(newFieldMapperFactory());

    final UserOwnedEntityProvider ownedEntityProvider = new UserOwnedEntityProvider();
    requestInjection(ownedEntityProvider);
    bindInterceptor(new DaoMatcher(), Matchers.any(), ownedEntityProvider);
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

  private static class UserOwnedEntityProvider implements MethodInterceptor {

    @Inject private UserDao userDao;
    @Inject private Cache cache;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
      final Object o = methodInvocation.proceed();
      return provideOwnedUser(o);
    }

    public Object provideOwnedUser(Object o)  {
      if (o instanceof OwnedEntity) {
        final OwnedEntity owned = (OwnedEntity) o;
        if (null != owned.getUserId()) {
          final CacheKey cacheKey = UserVO.cacheKeyFor(owned.getUserId());
          final UserVO userVO = cache.get(cacheKey.toString(), UserVO.class);
          if (null != userVO) owned.setUserVO(userVO);
          else owned.setUserVO(userDao.findUserBy(owned.getUserId()));
        }
      }
      return o;
    }
  }

  private static class DaoMatcher implements Matcher {
    public boolean matches(Object o) {
      return o instanceof TwigDao && !(o instanceof UserDao);
    }

    public Matcher and(Matcher matcher) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Matcher or(Matcher matcher) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
  }

  
}
