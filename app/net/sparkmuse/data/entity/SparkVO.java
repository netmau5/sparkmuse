package net.sparkmuse.data.entity;

import net.sparkmuse.data.Votable;
import net.sparkmuse.data.mapper.Property;
import net.sparkmuse.data.mapper.Model;
import net.sparkmuse.discussion.SparkRanking;

import java.util.List;

import org.joda.time.DateTime;
import models.SparkModel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
@Model(SparkModel.class)
public class SparkVO extends OwnedEntity<SparkVO> implements Votable {

  @Property("title") private String title;
  @Property("stage") private Stage stage;
  @Property("problem") private String problem;
  @Property("solution") private String solution;
  @Property("tags") private List<String> tags;
  @Property("created") private DateTime created;
  @Property("votes") private int votes;
  @Property("rating") private double rating;

  public enum Stage {
    NEW,
    PROTOTYPE,
    BETA,
    LAUNCHED
  }

  public static SparkVO newSpark(
      final UserVO author,
      final String title,
      final String stage,
      final String problem,
      final String solution,
      final List<String> tags){
    final SparkVO spark = new SparkVO();
    spark.setAuthor(author);
    spark.title = title;
    spark.stage = Stage.valueOf(stage);
    spark.problem = problem;
    spark.solution = solution;
    spark.tags = tags;
    spark.created = new DateTime();
    return spark;
  }

  public void upVote() {
    votes += 1;
    this.setRating(SparkRanking.calculateRating(this));
  }

  public void downVote() {
    votes -= 1;
    this.setRating(SparkRanking.calculateRating(this));
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public String getProblem() {
    return problem;
  }

  public void setProblem(String problem) {
    this.problem = problem;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public int getVotes() {
    return votes;
  }

  public void setVotes(int votes) {
    this.votes = votes;
  }
}
