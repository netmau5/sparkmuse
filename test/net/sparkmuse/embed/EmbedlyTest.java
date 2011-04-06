package net.sparkmuse.embed;

import play.test.UnitTest;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;

/**
 * @author neteller
 * @created: Apr 5, 2011
 */
public class EmbedlyTest extends UnitTest {

  @Test
  public void shouldReturnYoutubeData() {
    Embed embed = Embedly.lookup("http://vimeo.com/10179697");
    MatcherAssert.assertThat(embed.getType(), equalTo(Embed.ContentType.video));
    MatcherAssert.assertThat(embed.getHtml(), notNullValue());
    MatcherAssert.assertThat(embed.getTitle(), notNullValue());
    MatcherAssert.assertThat(embed.getDescription(), notNullValue());
    MatcherAssert.assertThat(embed.getWidth(), notNullValue());
    MatcherAssert.assertThat(embed.getHeight(), notNullValue());
  }

}
