package net.sparkmuse.common;

import org.joda.time.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 2, 2010
 */
public class DatePrinter {

  private static Duration ONE_MINUTE = Duration.standardMinutes(1);
  private static Duration ONE_HOUR = Duration.standardHours(1);
  private static Duration ONE_DAY = Duration.standardHours(24);
  private static Duration ONE_MONTH = Duration.standardHours(30 * 24);

  public static String timeElapsedSince(final DateTime date) {
    DateTime now = new DateTime();
    Duration duration = new Duration(date, now);

    if (duration.isShorterThan(ONE_MINUTE)) {
      return duration.getStandardSeconds() + " seconds ago";
    }
    else if (duration.isShorterThan(ONE_HOUR)) {
      int minutes = Minutes.minutesBetween(date, now).getMinutes();
      return minutes + " " + (minutes == 1 ? "minute ago" : "minutes ago");
    }
    else if (duration.isShorterThan(ONE_DAY)) {
      int hours = Hours.hoursBetween(date, now).getHours();
      return hours + " " + (hours == 1 ? "hour ago" : "hours ago");
    }
    else if (duration.isShorterThan(ONE_MONTH)) {
      int days = Days.daysBetween(date, now).getDays();
      return days + " " + (days == 1 ? "day ago" : "days ago");
    }
    else {
      int months = Months.monthsBetween(date, now).getMonths();
      return months + " " + (months == 1 ? "month ago" : "months ago");
    }
  }

}
