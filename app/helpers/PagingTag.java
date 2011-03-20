package helpers;

import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.mvc.Router;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import groovy.lang.Closure;
import net.sparkmuse.data.paging.PagingState;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.lang.StringUtils;

/**
 * @author neteller
 * @created: Mar 15, 2011
 */
public class PagingTag extends FastTags {

  /**
   * Creates a paging tag. The action parameter is expected to take an argument
   * named "page".
   *
   *  args:
        state: pagingState
        action: where to go when a paging link is clicked
        *: any named arguments (matching controller parms)
   */
  public static void _paging(Map<Object, Object> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
    PagingState state = (PagingState) args.get("state");
    String action = (String) args.get("action");
    Map<String, Object> optionalArgs = deriveOptionalArguments(args, "action", "state");

    if (state.currentPage() > 1) {
      Map<String,Object> arguments = Maps.newHashMap();
      arguments.put("page", state.currentPage() - 1);
      arguments.putAll(optionalArgs);
      out.print("<a href=\"" + Router.reverse(action, arguments).url + "\">Previous</a>");
    }

    if (state.currentPage() > 1 && state.hasMorePages()) {
      out.print(" | ");
    }

    if (state.hasMorePages()) {
      Map<String,Object> arguments = Maps.newHashMap();
      arguments.put("page", state.currentPage() + 1);
      arguments.putAll(optionalArgs);
      out.print("<a href=\"" + Router.reverse(action, arguments).url + "\">Next</a>");
    }
  }

  private static Map<String, Object> deriveOptionalArguments(Map<Object, Object> args, String... excludes) {
    Map<String, Object> toReturn = Maps.newHashMap();
    for (Map.Entry<Object, Object> entry: args.entrySet()) {
      toReturn.put(entry.getKey().toString(), entry.getValue());
    }
    for (String exclude: excludes) {
      toReturn.remove(exclude);
    }
    return toReturn;
  }

}
