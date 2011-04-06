package functional;

import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.data.entity.Post;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.Posts;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.joda.time.DateTime;
import static org.hamcrest.Matchers.*;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;

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

    UserVO u = new UserVO();
    u.setId(123L);
    u.setUserName("Dave");
    datastore.store(u);

    SparkVO sparkModel = new SparkVO();
    sparkModel.setTitle("Sup");
    sparkModel.setAuthor(u);
    sparkModel.setRating(1d);
    sparkModel.setPostCount(1);
    sparkModel.setCreated(new DateTime());
    datastore.store(sparkModel);
    sparkId = sparkModel.getId();

    Post p1 = new Post();
    p1.setCreated(new DateTime());
    p1.setContent("p1");
    p1.setSparkId(sparkId);
    p1.setVotes(3);
    datastore.store(p1);
    post1Id = p1.getId();

    Post p2 = new Post();
    p2.setCreated(new DateTime());
    p2.setContent("p2");
    p2.setSparkId(sparkId);
    p2.setVotes(6);
    datastore.store(p2);
    post2Id = p2.getId();

    Post p3 = new Post();
    p3.setCreated(new DateTime());
    p3.setContent("p3");
    p3.setSparkId(sparkId);
    p3.setVotes(6);
    p3.setInReplyToId(p2.getId());
    datastore.store(p3);
    post3Id = p3.getId();
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
    assertTrue(posts.sizeRootComments() == 2);
    assertTrue(posts.sizeTotalComments() == 3);

    Post p1 = Iterables.find(posts.getComments(), new Predicate<Post>(){
      public boolean apply(Post post) {
        return post.getId().equals(post1Id);
      }
    });
    Post p2 = Iterables.find(posts.getComments(), new Predicate<Post>(){
      public boolean apply(Post post) {
        return post.getId().equals(post2Id);
      }
    });

    assertTrue(p2.getReplies().size() == 1);
    assertTrue(posts.getComments().iterator().next() == p2); //first post should be p2 (higher votes)
  }

  @Test
  public void shouldHaveAuthorUser() {
    assertTrue(sparkFacade.findSparkBy(sparkId).getAuthor() != null);
  }

}
