package library;

import play.test.BaseTest;
import org.junit.Test;
import org.joda.time.DateTime;

import static org.hamcrest.Matchers.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 2, 2010
 */
public class JodaExistsTest extends BaseTest {

    @Test
    public void libraryShouldBeAvailable() {
        assertThat(new DateTime(), notNullValue());
    }

}
