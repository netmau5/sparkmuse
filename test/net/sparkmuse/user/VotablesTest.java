package net.sparkmuse.user;

import org.junit.Test;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.data.paging.PagingState;
import net.sparkmuse.discussion.SparkSearchResponse;
import net.sparkmuse.common.Orderings;
import net.sparkmuse.task.IssueTaskService;

import java.util.TreeSet;
import java.util.ArrayList;

import com.google.common.collect.Lists;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 28, 2010
 */
public class VotablesTest {

  @Test
  public void shouldReturnNewKey() {
    final Votable votable = new Votable() {
      public Long getId() {
        return 12L;
      }

      public UserVO getAuthor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
      }

      public int getVotes() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
      }

      public void upVote(UserVote userVote, IssueTaskService issueTaskService) {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      public void downVote() {
        //To change body of implemented methods use File | Settings | File Templates.
      }
    };
    final String key = Votables.newKey(votable);
    MatcherAssert.assertThat(key, Matchers.equalTo(votable.getClass().getName() + "|12"));
  }

  @Test
  public void shouldCollectVotablesFromSparkSearchResponse() {
    final SparkSearchResponse response = new SparkSearchResponse() {
      public TreeSet<SparkVO> getSparks() {
        SparkVO s1 = new SparkVO();
        s1.setId(1L);
        SparkVO s2 = new SparkVO();
        s2.setId(2L);
        ArrayList<SparkVO> sparks = Lists.newArrayList(
            s1,
            s2
        );
        TreeSet treeSet = new TreeSet(new Orderings.ByRecency());
        treeSet.addAll(sparks);
        return treeSet;
      }

      public TreeSet<SparkVO> getSparks(PagingState state) {
        return getSparks();
      }
    };

    MatcherAssert.assertThat(Votables.collect(response), Matchers.hasSize(2));
  }

  @Test
  public void shouldCollectVotablesFromSpark() {
    MatcherAssert.assertThat(Votables.collect(new SparkVO()), Matchers.hasSize(1));
  }

}
