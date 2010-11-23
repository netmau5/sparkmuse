package models;

import com.google.code.twig.annotation.Id;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkModel {

  @Id
  public Long id;
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
