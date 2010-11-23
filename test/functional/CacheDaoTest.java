package functional;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import net.sparkmuse.data.CacheDao;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.StoredCacheEntry;
import models.CacheModel;
import com.google.appengine.api.datastore.QueryResultIterator;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class CacheDaoTest extends PluginFunctionalTest {

  private CacheDao cacheDao;

  @Before
  public void setup() {
    super.setup();
    cacheDao = FunctionalTestUtils.getInstance(CacheDao.class);
  }

  @After
  public void tearDown() {
    cacheDao = null;
    super.tearDown();
  }

  private UserVO newUser() {
    UserVO user = UserVO.newUser("1234", "Dave");
    user.setId(1000L);
    return user;
  }

  @Test
  public void shouldSaveCacheable() {
    UserVO user = newUser();
    cacheDao.save(user);

    final QueryResultIterator<CacheModel> resultIterator = datastore.find()
        .type(CacheModel.class)
        .addFilter("key", EQUAL, user.getKey().toString())
        .now();
    assertTrue(resultIterator.hasNext());
    datastore.delete(resultIterator.next());
  }

  @Test
  public void shouldSaveKeyValue() {
    UserVO user = newUser();
    cacheDao.save(user.getKey().toString(), user);

    final QueryResultIterator<CacheModel> resultIterator = datastore.find()
        .type(CacheModel.class)
        .addFilter("key", EQUAL, user.getKey().toString())
        .now();
    assertTrue(resultIterator.hasNext());
    datastore.delete(resultIterator.next());
  }

  @Test
  public void shouldFind() {
    UserVO user = newUser();
    cacheDao.save(user.getKey().toString(), user);
    final StoredCacheEntry<UserVO> entry = cacheDao.find(user.getKey());
    assertTrue(null != entry.getValue());

    final QueryResultIterator<CacheModel> resultIterator = datastore.find()
        .type(CacheModel.class)
        .addFilter("key", EQUAL, user.getKey().toString())
        .now();
    assertTrue(resultIterator.hasNext());
    datastore.delete(resultIterator.next());
  }
  
}
