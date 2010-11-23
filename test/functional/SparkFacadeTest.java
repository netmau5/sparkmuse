package functional;

import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.data.entity.PostVO;
import net.sparkmuse.data.util.Posts;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Ignore;
import org.joda.time.DateTime;
import static org.hamcrest.Matchers.*;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import models.SparkModel;
import models.PostModel;
import models.UserModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkFacadeTest extends PluginFunctionalTest {

  private SparkFacade sparkFacade;
  private Long sparkId;
  private Long post1Id;
  private Long post2Id;
  private Long post3Id;

  @Before
  public void setup() {
    super.setup();
    sparkFacade = FunctionalTestUtils.getInstance(SparkFacade.class);

    SparkModel sparkModel = new SparkModel();
    sparkModel.title = "Sup";
    sparkModel.authorUserId = 123L;
    datastore.store(sparkModel);
    sparkId = sparkModel.id;

    PostModel p1 = new PostModel();
    p1.created = new DateTime().getMillis();
    p1.postContent = "p1";
    p1.sparkId = sparkModel.id;
    p1.votes = 3;
    datastore.store(p1);
    post1Id = p1.id;

    PostModel p2 = new PostModel();
    p2.created = new DateTime().getMillis();
    p2.postContent = "p2";
    p2.sparkId = sparkModel.id;
    p2.votes = 6;
    datastore.store(p2);
    post2Id = p2.id;

    PostModel p3 = new PostModel();
    p3.created = new DateTime().getMillis();
    p3.postContent = "p3";
    p3.sparkId = sparkModel.id;
    p3.votes = 6;
    p3.inReplyToId = p2.id;
    datastore.store(p3);
    post3Id = p3.id;

    UserModel userModel = new UserModel();
    userModel.id = 123L;
    datastore.store(userModel);
  }

  @After
  public void tearDown() {
    sparkFacade = null;
    super.tearDown();
  }

  @Test
  public void shouldFindSparkById() {
    assertThat(sparkFacade.findSparkBy(sparkId), notNullValue());
  }

  @Test
  public void shouldFindPostsBySpark() {
    Posts posts = sparkFacade.findPostsFor(sparkFacade.findSparkBy(sparkId));
    assertTrue(posts.countRootPosts() == 2);
    assertTrue(posts.countTotalPosts() == 3);

    PostVO p1 = Iterables.find(posts.getPosts(), new Predicate<PostVO>(){
      public boolean apply(PostVO post) {
        return post.getId().equals(post1Id);
      }
    });
    PostVO p2 = Iterables.find(posts.getPosts(), new Predicate<PostVO>(){
      public boolean apply(PostVO post) {
        return post.getId().equals(post2Id);
      }
    });

    assertTrue(p2.getReplies().size() == 1);
    assertTrue(posts.getPosts().iterator().next() == p2); //first post should be p2 (higher votes)
  }

  @Test
  public void shouldHaveAuthorUser() {
    assertTrue(sparkFacade.findSparkBy(sparkId).getAuthor() != null);
  }

}
