package net.sparkmuse.data;

import net.sparkmuse.data.entity.SparkVO;

import java.util.List;

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
}
