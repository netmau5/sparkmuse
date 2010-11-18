package library;

import play.test.UnitTest;
import org.junit.Test;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonGenerationException;

import java.io.IOException;

import static org.hamcrest.Matchers.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 2, 2010
 */
public class JacksonExistsTest extends UnitTest {

  static class JacksonTestPojo {
    private String name;

    JacksonTestPojo(final String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  @Test
  public void shouldMapObject() throws IOException, JsonGenerationException {
    assertEquals(
        new ObjectMapper().writeValueAsString(new JacksonTestPojo("Dave")),
        "{\"name\":\"Dave\"}"
    );
  }

}
