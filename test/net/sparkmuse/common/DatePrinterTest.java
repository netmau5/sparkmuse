package net.sparkmuse.common;

import play.test.BaseTest;
import org.junit.Test;
import org.joda.time.DateTime;
import static org.hamcrest.Matchers.*;
import net.sparkmuse.common.DatePrinter;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 2, 2010
 */
public class DatePrinterTest extends BaseTest {

  @Test
  public void testTimeElapsedSinceForSeconds() {
    DateTime dateTime = new DateTime().minusSeconds(20);
    assertThat(DatePrinter.timeElapsedSince(dateTime), endsWith(" seconds ago"));
  }

  @Test
  public void testTimeElapsedSinceForMinutes() {
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusMinutes(20)), endsWith(" minutes ago"));
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusMinutes(1)), endsWith(" minute ago"));
  }

  @Test
  public void testTimeElapsedSinceForHours() {
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusHours(20)), endsWith(" hours ago"));
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusHours(1)), endsWith(" hour ago"));
  }

  @Test
  public void testTimeElapsedSinceForDays() {
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusDays(20)), endsWith(" days ago"));
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusDays(1)), endsWith(" day ago"));
  }

  @Test
  public void testTimeElapsedSinceForMonths() {
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusMonths(20)), endsWith(" months ago"));
    assertThat(DatePrinter.timeElapsedSince(new DateTime().minusDays(30)), endsWith(" month ago"));
  }

}
