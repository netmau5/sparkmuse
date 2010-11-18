package net.sparkmuse.data.gae;

import play.test.UnitTest;
import org.mockito.Mockito;
import org.junit.Test;
import net.sparkmuse.data.CacheDao;
import net.sparkmuse.data.Cache;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.StoredCacheEntry;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.common.CacheKey;
import net.sparkmuse.common.CacheKeyFactory;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 16, 2010
 */
public class DatastoreCacheServiceTest extends UnitTest {

  @Test
  public void shouldReloadOnCacheMiss() {
    final CacheDao cacheDao = Mockito.mock(CacheDao.class);
    final DatastoreWriteThruCacheService service = new DatastoreWriteThruCacheService(
        Mockito.mock(IssueTaskService.class),
        cacheDao,
        Mockito.mock(Cache.class)
    );
    final CacheKey<UserVO> key = CacheKeyFactory.newUserKey(123L);

    assertTrue(null == service.get(key));
    Mockito.verify(cacheDao).find(key);

    Mockito.when(cacheDao.find(key)).thenReturn(new StoredCacheEntry(key, new UserVO()));
    assertTrue(null != service.get(key));
  }

  @Test
  public void shouldIssuePersistenceTaskWhenPut() {
    final CacheDao cacheDao = Mockito.mock(CacheDao.class);
    final IssueTaskService taskService = Mockito.mock(IssueTaskService.class);
    final DatastoreWriteThruCacheService service = new DatastoreWriteThruCacheService(
        taskService,
        cacheDao,
        Mockito.mock(Cache.class)
    );
    final UserVO vo = new UserVO();
    vo.setId(123L);

    service.putAndWrite(vo);
    Mockito.verify(taskService).issueCachePersistTask(vo);
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldFailToPutNonUniqueEntity() {
    final CacheDao cacheDao = Mockito.mock(CacheDao.class);
    final IssueTaskService taskService = Mockito.mock(IssueTaskService.class);
    final DatastoreWriteThruCacheService service = new DatastoreWriteThruCacheService(
        taskService,
        cacheDao,
        Mockito.mock(Cache.class)
    );
    final UserVO vo = new UserVO();

    service.put(vo);
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldFailToPutAndWriteNonUniqueEntity() {
    final CacheDao cacheDao = Mockito.mock(CacheDao.class);
    final IssueTaskService taskService = Mockito.mock(IssueTaskService.class);
    final DatastoreWriteThruCacheService service = new DatastoreWriteThruCacheService(
        taskService, 
        cacheDao,
        Mockito.mock(Cache.class)
    );
    final UserVO vo = new UserVO();

    service.putAndWrite(vo);
  }



}
