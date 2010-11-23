package functional.controllers;

import static com.google.appengine.api.datastore.Query.FilterOperator.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.hamcrest.Matchers;
import org.hamcrest.MatcherAssert;
import net.sparkmuse.data.WriteThruCacheService;
import net.sparkmuse.data.mapper.ObjectMapper;
import net.sparkmuse.data.mapper.FieldMapperFactory;
import net.sparkmuse.data.entity.UserVO;
import models.UserModel;
import functional.PluginFunctionalTest;
import functional.FunctionalTestUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class TaskControllerTest extends PluginFunctionalTest {

  @Test
  public void shouldStoreEntity() {
    final WriteThruCacheService cache = FunctionalTestUtils.getInstance(WriteThruCacheService.class);
    final ObjectMapper map = new ObjectMapper(FunctionalTestUtils.getInstance(FieldMapperFactory.class), UserVO.class);
    final UserModel model = map.fromEntity(UserVO.newUser("authId", "Dave")).to(UserModel.class);
    datastore.store(model);

    final UserVO user = map.fromModel(model).to(UserVO.class);
    user.setUserName("Peter");

    cache.put(user);
    datastore.disassociate(model); //disassociate here because another datastore is going to update

    GET("/Task/persistCacheValue?cacheKey=" + user.getKey().toString());

    MatcherAssert.assertThat(
        datastore.load(UserModel.class, user.getId()).userName,
        Matchers.equalTo("Peter")
    );
  }

}
