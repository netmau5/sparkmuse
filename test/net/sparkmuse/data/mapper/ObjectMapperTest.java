package net.sparkmuse.data.mapper;

import org.junit.Test;
import org.apache.commons.collections.CollectionUtils;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.Entity;
import net.sparkmuse.data.util.AccessLevel;
import net.sparkmuse.data.mapper.ObjectMapper;
import models.UserModel;
import functional.PluginFunctionalTest;
import play.test.UnitTest;

/**
 * Tests the object mapper.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class ObjectMapperTest extends UnitTest {

  @Test
  public void shouldInitializeMetadata() {
    assertTrue(CollectionUtils.size(new ObjectMapper(UserVO.class).classToMetadataMap.keySet()) > 0);
  }

  @Test
  public void shouldMapEntityToModel() {
    final ObjectMapper mapper = new ObjectMapper(UserVO.class);

    final UserVO vo = UserVO.newUser("userId", "userName");
    final UserModel userModel = mapper.fromEntity(vo).to(UserModel.class);

    assertTrue(userModel.userId.equals("userId"));
    assertTrue(userModel.userName.equals("userName"));
  }

  @Test
  public void shouldMapModelToEntity() {
    final ObjectMapper mapper = new ObjectMapper(UserVO.class);

    final UserModel model = new UserModel();
    model.userId = "something";
    model.authorization = AccessLevel.ADMIN.toString();
    model.reputation = 5;
    model.id = 12L;
    model.userName = "Dave";
    final UserVO vo = mapper.fromModel(model).to(UserVO.class);

    assertTrue(vo.getUserName().equals("Dave"));
  }

  @Test
  public void shouldReturnNullEntityForNullModel() {
    final ObjectMapper mapper = new ObjectMapper(UserVO.class);
    assertTrue(mapper.fromModel(null).to(UserVO.class) == null);
  }

  @Test
  public void shouldReturnNullModelForNullEntity() {
    final ObjectMapper mapper = new ObjectMapper(UserVO.class);
    final UserVO user = null;
    assertTrue(mapper.fromEntity(user).to(UserModel.class) == null);
  }

}
