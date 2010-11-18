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
public interface SparkDao {
  SparkVO findById(Long id);
  SparkVO create(SparkVO spark);
  List<SparkVO> loadPopular();
  String transform(final Function<SparkVO, SparkVO> transformation, final String cursor);
}
