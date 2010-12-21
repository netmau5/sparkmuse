package net.sparkmuse.data.entity;

import play.test.UnitTest;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;

/**
 * @author neteller
 * @created: Dec 20, 2010
 */
public class FeedbackTest extends UnitTest {

  @Test
  public void shouldReplaceMultipleImages() {
    String content = "Lets put an image here ${123} and here ${abc}";
    MatcherAssert.assertThat(
        Feedback.addImages(content),
        equalTo("Lets put an image here " + Feedback.IMG.replace(Feedback.IMG_TOKEN, "123") + " and here " + Feedback.IMG.replace(Feedback.IMG_TOKEN, "abc"))
    );
  }

  @Test
  public void shouldReplaceBlobstoreStyleId() {
    String id = "amifv95o1e0gf6dbnndxtwufsqhxytuwmwaqiwndumpmojroznu_qd0hj9vdnmbyj-ywvb7k25gtqhwp0jjzzedzfbjlyqlb1ncanv8bbc5thc3izkiw4psvrd9gcgcvjgw41owawxwfkyk0cpfcmmkphrxak1vlkq";
    String content = "${" + id + "}";
    MatcherAssert.assertThat(
        Feedback.addImages(content),
        equalTo(Feedback.IMG.replace(Feedback.IMG_TOKEN, id))
    );
  }



}
