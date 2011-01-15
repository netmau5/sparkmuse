package net.sparkmuse.ajax;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

import play.data.validation.Error;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;
import net.sparkmuse.common.ResponseCode;
import org.apache.commons.lang.StringUtils;

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

    final Function<Error, String> formatter = new ErrorMessageFormatter();
    this.validationErrors = Maps.transformValues(errorsByField, new Function<List<Error>, List<String>>(){
      public List<String> apply(List<Error> errors) {
        return Lists.newArrayList(Iterables.transform(errors, formatter));
      }
    });
  }

  private ValidationErrorAjaxResponse() {
    super(ResponseCode.BAD_REQUEST, Type.VALIDATION_ERROR);
  }

  /**
   * Create a new error response with a single validation error message.
   *
   * @param fieldKey
   * @param errorMessage
   * @return
   */
  public static ValidationErrorAjaxResponse only(String fieldKey, String errorMessage) {
    final ValidationErrorAjaxResponse response = new ValidationErrorAjaxResponse();
    response.validationErrors = ImmutableMap.<String, List<String>>of(fieldKey, Lists.newArrayList(errorMessage));
    return response;
  }

  public Map<String, List<String>> getValidationErrors() {
    return validationErrors;
  }

  private static class ErrorMessageFormatter implements Function<Error, String> {
    public String apply(Error error) {
      String s = error.message();
      if (StringUtils.isNotBlank(s)) {
        final Iterable<String> words = Splitter.on(" ").split(s);
        final Iterator<String> wordsIterator = words.iterator();

        //get rid of any dot notation on variables, newSpark.message should just be message
        String firstWord = wordsIterator.next().replaceAll("\\w*\\.", "");

        //capitalize first letter
        firstWord = firstWord.substring(0, 1).toUpperCase() + firstWord.substring(1);

        s = firstWord + " " + Joiner.on(" ").join(Lists.newArrayList(wordsIterator));
      }
      return s;
    }
  }
}
