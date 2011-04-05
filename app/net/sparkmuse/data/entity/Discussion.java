package net.sparkmuse.data.entity;

import net.sparkmuse.common.DiscussionType;

/**
 * @author neteller
 * @created: Apr 4, 2011
 */
public class Discussion extends OwnedEntity<Discussion> {

  private String title;

  private DiscussionType discussionType;

  private int votes;
  private boolean notified;

}
