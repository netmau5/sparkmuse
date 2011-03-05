package net.sparkmuse.data;

import net.sparkmuse.data.entity.SparkVO;
import net.sparkmuse.discussion.SparkSearchRequest;

import java.util.List;
import java.util.Collection;

import com.google.common.base.Function;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public interface SparkDao extends CrudDao {
  SparkVO findById(Long id);
  List<SparkVO> loadPopular();
  List<SparkVO> loadRecent();
  List<SparkVO> loadMostDiscussed();
  List<SparkVO> loadTagged(String tag);
  List<SparkVO> search(SparkSearchRequest request);
  void delete(SparkVO spark);
}
