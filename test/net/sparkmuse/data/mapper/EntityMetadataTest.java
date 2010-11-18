package net.sparkmuse.data.mapper;

import play.test.UnitTest;
import org.junit.Test;
import org.apache.commons.collections.CollectionUtils;
import net.sparkmuse.data.entity.UserVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class EntityMetadataTest extends UnitTest {

  @Test
  public void shouldExtractMetadata() {
    final EntityMetadata metadata = new EntityMetadata(UserVO.class);
    assertTrue(CollectionUtils.size(metadata.gettersByPropertyName) > 0);
    assertTrue(CollectionUtils.size(metadata.settersByPropertyName) > 0);
  }

}
