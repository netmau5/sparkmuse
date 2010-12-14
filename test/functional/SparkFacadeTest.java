package functional;

import net.sparkmuse.discussion.SparkFacade;
import net.sparkmuse.data.entity.PostVO;
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

    PostVO p1 = new PostVO();
    p1.setCreated(new DateTime());
    p1.setPostContent("p1");
    p1.setSparkId(sparkId);
    p1.setVotes(3);
    datastore.store(p1);
    post1Id = p1.getId();

    PostVO p2 = new PostVO();
    p2.setCreated(new DateTime());
    p2.setPostContent("p2");
    p2.setSparkId(sparkId);
    p2.setVotes(6);
    datastore.store(p2);
    post2Id = p2.getId();

    PostVO p3 = new PostVO();
    p3.setCreated(new DateTime());
    p3.setPostContent("p3");
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
