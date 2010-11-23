package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.Cache;
import net.sparkmuse.data.UserDao;
import net.sparkmuse.common.CacheKeyFactory;

import java.util.Set;
import java.util.List;
import java.util.Map;

import com.google.common.collect.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 22, 2010
 */
public class UserProvider {

  private final UserDao userDao;
  private final Cache cache;

  public UserProvider(UserDao userDao, Cache cache) {
    this.userDao = userDao;
    this.cache = cache;
  }

  public UserVO get(final Long id) {
    UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(id).toString(), UserVO.class);
    if (null != cachedUser) {
      return cachedUser;
    }

    return userDao.findUserBy(id);
  }

  public Map<Long, UserVO> get(final Iterable<Long> userIds) {
    Set<Long> toQuery = Sets.newHashSet();
    Set<UserVO> cachedUsers = Sets.newHashSet();
    for (Long userId: userIds) {
      UserVO cachedUser = cache.get(CacheKeyFactory.newUserKey(userId).toString(), UserVO.class);
      if (null != cachedUser) {
        cachedUsers.add(cachedUser);
      }
      else {
        toQuery.add(userId);
      }
    }

    final Map<Long, UserVO> usersMap = userDao.findUsersBy(toQuery);
    usersMap.putAll(Maps.uniqueIndex(cachedUsers, UserVO.asUserIds));

    return usersMap;
  }

}
