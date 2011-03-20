package net.sparkmuse.data.entity;

import com.google.common.base.Function;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 18, 2010
 */
public class OwnedEntity<T> extends Entity<T> {

  private UserVO author;
  private Long authorUserId; 

  public UserVO getAuthor() {
    return author;
  }

  public void setAuthor(UserVO author) {
    this.author = author;
    this.authorUserId = author.getId();
  }

  public static Function<Entity, Long> asOwnerIds = new Function<Entity, Long>(){
    public Long apply(Entity entity) {
      if (entity instanceof OwnedEntity) {
        return ((OwnedEntity) entity).authorUserId;
      }
      return null;
    }
  };
}
