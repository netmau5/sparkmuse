package net.sparkmuse.data.mapper;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.joda.time.DateTime;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.UserDao;
import net.sparkmuse.data.Cache;

/**
 * presently unused, see ownedEntityProvider aspect
 *
 * @author neteller
 * @created: Nov 16, 2010
 */
public class UserMapper implements FieldMapper {

  private Cache cache;
  private UserDao userDao;

  @Inject
  public UserMapper(Cache cache, UserDao userDao) {
    this.cache = cache;
    this.userDao = userDao;
  }

  public Class getEntityFieldType() {
    return UserVO.class;
  }

  public Object toModelField(Class targetClass, Object object) {
    if (null == object) return null;

    Preconditions.checkArgument(targetClass == Long.class);
    Preconditions.checkArgument(object instanceof UserVO);
    return ((UserVO) object).getId();
  }

  public Object toEntityField(Class targetClass, Object object) {
    if (null == object) return null;

    Preconditions.checkArgument(targetClass == UserVO.class);
    Preconditions.checkArgument(object instanceof Long);

    final Long id = (Long) object;
    final Object cachedUser = cache.get(UserVO.cacheKeyFor(id).toString());
    if (null != cachedUser) return cachedUser;
    else return userDao.findUserBy(id);
  }

}
