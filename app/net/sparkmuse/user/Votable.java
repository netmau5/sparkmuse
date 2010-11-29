package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVO;

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

  void upVote();

  void downVote();

}
