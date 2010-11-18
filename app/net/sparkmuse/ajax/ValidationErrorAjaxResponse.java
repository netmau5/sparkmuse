package net.sparkmuse.ajax;

import java.util.List;
import java.util.Map;
import play.data.validation.Error;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import net.sparkmuse.common.ResponseCode;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 14, 2010
 */
public class ValidationErrorAjaxResponse extends AjaxResponse {

  private Map<String, List<String>> validationErrors;

  public ValidationErrorAjaxResponse(final Map<String, List<Error>> errorsByField) {
    super(ResponseCode.BAD_REQUEST, Type.VALIDATION_ERROR);

    this.validationErrors = Maps.transformValues(errorsByField, new Function<List<Error>, List<String>>(){
      public List<String> apply(List<Error> errors) {
        return Lists.newArrayList(Iterables.transform(errors, new Function<Error, String>(){
          public String apply(Error error) {
            return error.message();
          }
        }));
      }
    });
  }

  public Map<String, List<String>> getValidationErrors() {
    return validationErrors;
  }
}
