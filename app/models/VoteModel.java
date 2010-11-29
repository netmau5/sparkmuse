package models;

import com.google.code.twig.annotation.Id;
import net.sparkmuse.user.Votable;
import net.sparkmuse.user.Votables;
import net.sparkmuse.data.entity.UserVO;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class VoteModel {

  @Id public String id; //should be [entityClassName]|[entityId]
  public int voteWeight; //should generally be -1, 0, +1
  public String entityClassName;
  public Long entityId;
  public Long authorUserId;

  public static VoteModel newUpVote(Votable votable, UserVO voter) {
    final VoteModel vm = new VoteModel();
    vm.entityClassName = votable.getClass().getName();
    vm.entityId = votable.getId();
    vm.id = Votables.newKey(votable);
    vm.voteWeight = 1;
    vm.authorUserId = voter.getId();
    return vm;
  }

}
