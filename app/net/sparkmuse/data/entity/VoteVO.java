package net.sparkmuse.data.entity;

import net.sparkmuse.data.mapper.Model;
import net.sparkmuse.data.mapper.Property;
import net.sparkmuse.data.Votable;
import net.sparkmuse.common.CacheKey;
import models.VoteModel;
import com.google.common.base.Preconditions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
@Model(VoteModel.class)
public class VoteVO extends Entity<VoteVO> {

  @Property("voteWeight") private int voteWeight;
  @Property("entityClassName") private Class entity;
  @Property("entityId") private long entityId;
  @Property("userId") private Long userId;


  public static VoteVO newUpVote(Votable votable, Long userId){
    Preconditions.checkNotNull(userId);

    final VoteVO vo = new VoteVO();
    vo.voteWeight = 1;
    vo.entity = votable.getClass();
    vo.entityId = votable.getId();
    vo.userId = userId;
    return vo;
  }

  @Override
  public CacheKey<VoteVO> getKey() {
    //@todo does entityId need to be non-primitive for cache key constructor
    return new CacheKey(this.getClass(), entity, entityId, userId);
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public int getVoteWeight() {
    return voteWeight;
  }

  public void setVoteWeight(int voteWeight) {
    this.voteWeight = voteWeight;
  }

  public Class getEntity() {
    return entity;
  }

  public void setEntity(Class entity) {
    this.entity = entity;
  }

  public long getEntityId() {
    return entityId;
  }

  public void setEntityId(long entityId) {
    this.entityId = entityId;
  }
}

