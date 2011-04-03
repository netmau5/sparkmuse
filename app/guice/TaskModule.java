package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import net.sparkmuse.task.IssueTaskService;
import net.sparkmuse.task.gae.GaeIssueTaskService;
import net.sparkmuse.mail.SendMailService;
import net.sparkmuse.mail.PlaySendMailService;

/**
 * Configuration for tasks and cron jobs; these are typically run in background.
 *
 * @author neteller
 * @created: Jul 10, 2010
 */
public class TaskModule extends AbstractModule {

  protected void configure() {
    bind(SendMailService.class).to(PlaySendMailService.class);
  }

  @Provides
  public IssueTaskService newIssueTaskService(){
    return new GaeIssueTaskService(QueueFactory.getDefaultQueue());
  }

  
}
