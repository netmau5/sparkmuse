package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public interface SparkSearchResponse {

  TreeSet<SparkVO> getSparks();

}
