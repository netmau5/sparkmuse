package net.sparkmuse.discussion;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.data.paging.PagingState;
import net.sparkmuse.data.paging.PagingSize;
import net.sparkmuse.data.paging.CachePages;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Nov 25, 2010
 */
public interface SparkSearchResponse {

  public static final int MAX_CACHE_SIZE = SparkVO.class.getAnnotation(CachePages.class).value() * SparkVO.class.getAnnotation(PagingSize.class).value();

  TreeSet<SparkVO> getSparks();

  TreeSet<SparkVO> getSparks(PagingState state);

}
