package net.sparkmuse.client;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.context.FieldContext;

import java.util.Collection;
import java.util.List;

import play.data.validation.Check;
import play.data.validation.Validation;

/**
 * @author neteller
 * @created: Jan 8, 2011
 */
public class CollectionMemberCheck extends Check {

  public boolean isSatisfied(Object validatedObject, Object value) {
    if (value != null && value instanceof Collection) {
      String key = "?"; //ValidationPlugin.keys.get().get(validatedObject); //should be in play.data.validation package

      for (Object member: (Collection) value) {
        final List<ConstraintViolation> violations = new Validator().validate(member);
        for (ConstraintViolation violation : violations) {
          if (violation.getContext() instanceof FieldContext) {
            final FieldContext ctx = (FieldContext) violation.getContext();
            final String fkey = (key == null ? "" : key + ".") + ctx.getField().getName();
            Validation.addError(fkey, violation.getMessage(), violation.getMessageVariables() == null ? new String[0] : violation.getMessageVariables().values().toArray(new String[0]));
          }
        }
        return violations.size() == 0;
      }
    }

    return true;
  }
}
