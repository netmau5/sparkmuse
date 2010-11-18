package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import org.joda.time.Days;
import org.joda.time.DateTime;


/**
 * Calculates a ranking for a post based on the following formula:
 * (p - 1) / (t + 2)^1.25
 * p = votes (points) from users
 * t = time since submission in days (0 for day 0)
 * p is subtracted by 1 to negate submitters vote.
 * age factor is (time since submission in hours plus two) to the power of 1.5.
 *
 * Ratings should be recalculated on votes and decay for all Sparks
 * should be recalculated on a daily basis as the variable 't' is
 * measured in days.
 *
 * @author neteller
 * @created: Jul 18, 2010
 */
public class SparkRanking {

  public static double calculateRating(final SparkVO spark) {
    final int days = Days.daysBetween(spark.getCreated(), new DateTime()).getDays();
    return (spark.getVotes() - 1) / (Math.pow(days + 2.0, 1.25));
  }

}
