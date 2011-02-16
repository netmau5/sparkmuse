package net.sparkmuse.task;

import com.google.appengine.api.datastore.Cursor;
import com.google.inject.internal.Nullable;
import com.google.inject.Inject;
import com.google.code.twig.ObjectDatastore;
import net.sparkmuse.user.UserFacade;
import net.sparkmuse.data.entity.Invitation;

/**
 * @author neteller
 * @created: Feb 15, 2011
 */
public class GenerateInvitationCodesTask extends Task {

  //characters chosen to not overlap in handwritten style
  private static final String CHARACTERS = "ABCDEFHKMNPQTWXYZ34689";

  private final UserFacade userFacade;
  private final ObjectDatastore datastore;

  @Inject
  public GenerateInvitationCodesTask(UserFacade userFacade, ObjectDatastore datastore) {
    super(datastore);
    this.userFacade = userFacade;
    this.datastore = datastore;
  }

  protected Cursor runTask(@Nullable Cursor cursor) {
    //generate 10 unique codes
    for (int i = 0; i < 10; i++) {
      String code;
      do {
        code = newCode();
      } while(isCollision(code));
      datastore.store().instance(newInvitation(code)).later();
    }

    return null;
  }

  private Invitation newInvitation(String code) {
    Invitation invitation = new Invitation();
    invitation.setCode(code);
    return invitation;
  }

  /**
   * Create 6 character codes based on CHARACTERS.
   *
   * @return
   */
  private String newCode() {
    String toReturn = "";
    for (int i = 0; i < 6; i++) {
      int charPos = (int) (Math.random() * 100) % CHARACTERS.length();
      toReturn += CHARACTERS.charAt(charPos);
    }
    return toReturn;
  }

  private boolean isCollision(String code) {
    return userFacade.verifyInvitationCode(code) != null;
  }

}
