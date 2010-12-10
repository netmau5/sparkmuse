package net.sparkmuse.discussion;

import play.test.UnitTest;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;

/**
 * @author neteller
 * @created: Dec 9, 2010
 */
public class LinkMetadataTest extends UnitTest {

  @Test
  public void shouldExtractTitle() {
    final LinkMetadata linkMetadata = LinkMetadata.newMetadata("<title>Some title</TITLE>");
    MatcherAssert.assertThat(linkMetadata.getTitle(), equalTo("Some title"));
  }

  @Test
  public void shouldExtractDescription() {
    final LinkMetadata linkMetadata = LinkMetadata.newMetadata("<meta name='description' content='a description'/>");
    MatcherAssert.assertThat(linkMetadata.getDescription(), equalTo("a description"));
  }

  @Test
  public void shouldExtractMetadata() {
    final LinkMetadata linkMetadata = LinkMetadata.newMetadata(
        "<html>\n\n\n" +
            "<head>\n" +
            "<title>\n" +
            "\tA\nSweet\n  Title\n" +
            "</title>\n" +
            "<script type='text/javascript' src='http://earth'></script>\n" +
            "<meta name='description' \n" +
            "content='a\ndescription'>\n" +
            "</head>\n\n\n\n\n\n"
    );
    MatcherAssert.assertThat(linkMetadata.getTitle(), equalTo("A Sweet Title"));
    MatcherAssert.assertThat(linkMetadata.getDescription(), equalTo("a description"));
  }

  @Test
  public void shouldGetGoogleMetadata() {
    final LinkMetadata linkMetadata = LinkMetadata.lookup("http://www.google.com");
    MatcherAssert.assertThat(linkMetadata.getTitle(), equalTo("Google"));
  }

}
