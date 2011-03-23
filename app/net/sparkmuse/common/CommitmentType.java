package net.sparkmuse.common;

import net.sparkmuse.data.util.Counters;

/**
 * @author neteller
 * @created: Mar 21, 2011
 */
public enum CommitmentType {

  //You'd like to be notified when if this problem gets solved.
  SEE(Counters.WISH_SEECOMMIT_COUNTER),
  //you'd use a solution to this problem and want to be registered
  //for any betas being offered for it.
  TRY(Counters.WISH_TRYCOMMIT_COUNTER),
  //happy to give feedback or explain the problem in more depth
  //to entrepreneurs trying to figure out how to solve this problem.
  SURVEY(Counters.WISH_SURVEYCOMMIT_COUNTER),
  //You want this problem solved so badly that you'd be willing to pay
  //for it to exist.
  BUY(Counters.WISH_BUYCOMMIT_COUNTER);

  private final Counters.EntityCounter counter;

  CommitmentType(Counters.EntityCounter counter) {
    this.counter = counter;
  }

  public Counters.EntityCounter getCounter() {
    return counter;
  }
}
