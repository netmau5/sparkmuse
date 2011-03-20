package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVO;
import net.sparkmuse.data.entity.UserVote;
import net.sparkmuse.task.IssueTaskService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface Votable {

  /**
   * Id of the entity
   *
   * @return
   */
  Long getId();

  /**
   * User who created this entity.
   *
   * @return
   */
  UserVO getAuthor();

  int getVotes();

  void upVote(UserVote userVote, IssueTaskService issueTaskService);

  void downVote();

}
