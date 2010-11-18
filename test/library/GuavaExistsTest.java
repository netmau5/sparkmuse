package library;

import play.test.BaseTest;
import org.junit.Test;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import static org.hamcrest.Matchers.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 2, 2010
 */
public class GuavaExistsTest extends BaseTest {

    @Test
    public void libraryShouldBeAvailable() {
      assertTrue(0 == Iterables.size(Lists.newArrayList()));
    }

}
