package models;

import java.util.List;

import com.vercer.engine.persist.annotation.Key;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkModel {

  @Key public Long id;
  public Long authorUserId;
  public String title;
  public String stage;
  public String problem;
  public String solution;
  public List<String> tags;
  public Long created;
  public int votes;
  public double rating;

}
