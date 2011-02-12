package net.sparkmuse.mail;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author neteller
 * @created: Feb 5, 2011
 */
public class InvitationEmail implements EmailTemplate {

  private final String inviter;
  private final String invitee;
  private final String toEmail;

  public InvitationEmail(String inviter, String invitee, String toEmail) {
    this.inviter = inviter;
    this.invitee = invitee;
    this.toEmail = toEmail;
  }

  public String getToEmail() {
    return toEmail;
  }

  public String getSubject() {
    return getInviter() + " has invited you to Sparkmuse!";
  }

  public String getTemplate() {
    return "Mail/Invitation.html";
  }

  public String getUpdateeName() {
    return invitee;
  }

  public String getInviter() {
    return inviter;
  }

  public Map<String, Object> getTemplateArguments() {
    final Map<String, Object> args = Maps.newHashMap();
    args.put("update", this);
    return args;
  }
}
