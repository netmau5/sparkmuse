package net.sparkmuse.data.entity;

import net.sparkmuse.user.Votable;
import net.sparkmuse.discussion.SparkRanking;

import java.util.List;

import org.joda.time.DateTime;
import play.data.validation.Required;
import com.google.code.twig.annotation.Type;
import com.google.appengine.api.datastore.Text;

/**
 * Created by IntelliJ IDEA.
 *
 * @author neteller
 * @created: Jul 5, 2010
 */
public class SparkVO extends OwnedEntity<SparkVO> implements Votable {

  @Required
  private String title;

  @Required
  private Stage stage;

  @Required
  @Type(Text.class)
  private String problem;

  @Required
  @Type(Text.class)
  private String displayProblem;

  @Required
  @Type(Text.class)
  private String solution;

  @Required
  @Type(Text.class)
  private String displaySolution;

  @Required(message="validation.required.tags")
  private List<String> tags;

  private DateTime created;
  private DateTime edited;

  private int votes;

  private double rating;

  private int postCount;

  public enum Stage {
    NEW,
    PROTOTYPE,
    BETA,
    LAUNCHED;

    public boolean hasProgressedNew() {
      return this.ordinal() >= Stage.NEW.ordinal();
    }

    public boolean hasProgressedPrototype() {
      return this.ordinal() >= Stage.PROTOTYPE.ordinal();
    }

    public boolean hasProgressedBeta() {
      return this.ordinal() >= Stage.BETA.ordinal();
    }

    public boolean hasProgressedLaunched() {
      return this.ordinal() >= Stage.LAUNCHED.ordinal();
    }

  }

  public SparkVO() {
    this.created = new DateTime();
    this.edited = new DateTime();
  }

  public int getPostCount() {
    return postCount;
  }

  public void setPostCount(int postCount) {
    this.postCount = postCount;
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

  public String getDisplayProblem() {
    return displayProblem;
  }

  public void setDisplayProblem(String displayProblem) {
    this.displayProblem = displayProblem;
  }

  public String getDisplaySolution() {
    return displaySolution;
  }

  public void setDisplaySolution(String displaySolution) {
    this.displaySolution = displaySolution;
  }

  public DateTime getEdited() {
    return edited;
  }

  public void setEdited(DateTime edited) {
    this.edited = edited;
  }
}
