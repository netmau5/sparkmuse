package net.sparkmuse.data.mapper;

import play.test.UnitTest;
import org.junit.Test;
import org.apache.commons.collections.CollectionUtils;
import net.sparkmuse.data.entity.UserVO;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 17, 2010
 */
public class EntityMetadataTest extends UnitTest {

  @Test
  public void shouldExtractMetadata() {
    final List<FieldMapper> mappers = Lists.<FieldMapper>newArrayList(new FieldMapperFactory.StandardMapper());
    final EntityMetadata metadata = new EntityMetadata(UserVO.class, new FieldMapperFactory(mappers));
    assertTrue(CollectionUtils.size(metadata.getFields()) > 0);
    assertTrue(CollectionUtils.size(metadata.getFields()) > 0);
  }

}
