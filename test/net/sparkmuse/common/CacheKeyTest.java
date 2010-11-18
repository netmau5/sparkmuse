package net.sparkmuse.common;

import play.test.UnitTest;
import org.junit.Test;
import org.hamcrest.Matchers;
import org.hamcrest.MatcherAssert;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 7, 2010
 */
public class CacheKeyTest extends UnitTest {

  @Test
  public void shouldCreateSingletonKey() {
    MatcherAssert.assertThat(
        new CacheKey(Integer.class).toString(),
        Matchers.equalTo(Integer.class.getName())
    );
  }

  @Test
  public void shouldCreateKeyWithLongId() {
    MatcherAssert.assertThat(
        new CacheKey(Integer.class, new Long(40)).toString(),
        Matchers.equalTo(Integer.class.getName() + "|" + 40)
    );
  }

  @Test
  public void shouldCreateKeyWithMultipleUniqueFields() {
    MatcherAssert.assertThat(
        new CacheKey(Integer.class, "Name", "Place").toString(),
        Matchers.equalTo(Integer.class.getName() + "|Name|Place")
    );
  }

}
