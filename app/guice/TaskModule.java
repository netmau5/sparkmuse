package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.gae.GaeIssueTaskService;
import net.sparkmuse.mail.MailService;
import net.sparkmuse.mail.PlayMailService;

/**
 * Configuration for tasks and cron jobs; these are typically run in background.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class TaskModule extends AbstractModule {

  protected void configure() {
    bind(MailService.class).to(PlayMailService.class);
  }

  @Provides
  public IssueTaskService newIssueTaskService(){
    return new GaeIssueTaskService(QueueFactory.getDefaultQueue());
  }

  
}
