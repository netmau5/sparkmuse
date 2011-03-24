package net.sparkmuse.mail;

import net.sparkmuse.data.entity.Mailing;
import net.sparkmuse.data.entity.UserProfile;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author neteller
 * @created: Mar 24, 2011
 */
public class MailingTemplate implements EmailTemplate {

  private final Mailing mailing;
  private final UserProfile recipient;

  public MailingTemplate(Mailing mailing, UserProfile recipient) {
    this.mailing = mailing;
    this.recipient = recipient;
  }

  public String getToEmail() {
    return recipient.getEmail();
  }

  public String getSubject() {
    return mailing.getSubject();
  }

  public String getUpdateeName() {
    throw new UnsupportedOperationException();
  }

  public String getTemplate() {
    return "Mail/Mailing.html";
  }

  public Map<String, Object> getTemplateArguments() {
    final Map<String, Object> args = Maps.newHashMap();
    args.put("mailing", mailing);
    args.put("recipient", recipient);
    return args;
  }
}
