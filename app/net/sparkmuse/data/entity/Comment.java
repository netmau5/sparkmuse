package net.sparkmuse.data.entity;

import org.joda.time.DateTime;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;
import play.data.validation.Required;
import play.data.validation.CheckWith;
import net.sparkmuse.client.NoScriptCheck;
import net.sparkmuse.user.Votable;
import net.sparkmuse.task.IssueTaskService;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public class Comment extends AbstractComment<Comment> {

  @Required
  private Long wishId;

  public Long getWishId() {
    return wishId;
  }

  public void setWishId(Long wishId) {
    this.wishId = wishId;
  }

}
