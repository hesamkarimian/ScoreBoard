package org.sportradar.scoreboard.entities;

import java.util.Date;
import java.util.Objects;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public class Match implements Comparable<Match> {

  private String homeTeam;
  private String awayTeam;
  private Integer homeTeamScore;
  private Integer awayTeamScore;
  private Date matchTime;

  public Match(String homeTeam, String awayTeam) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.homeTeamScore = 0;
    this.awayTeamScore = 0;
    this.matchTime = new Date();
  }

  public String getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(String homeTeam) {
    this.homeTeam = homeTeam;
  }

  public String getAwayTeam() {
    return awayTeam;
  }

  public void setAwayTeam(String awayTeam) {
    this.awayTeam = awayTeam;
  }

  public Integer getHomeTeamScore() {
    return homeTeamScore;
  }

  public void setHomeTeamScore(Integer homeTeamScore) {
    this.homeTeamScore = homeTeamScore;
  }

  public Integer getAwayTeamScore() {
    return awayTeamScore;
  }

  public void setAwayTeamScore(Integer awayTeamScore) {
    this.awayTeamScore = awayTeamScore;
  }

  public Date getMatchTime() {
    return matchTime;
  }

  public void setMatchTime(Date matchTime) {
    this.matchTime = matchTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Match that)) {
      return false;
    }
    return Objects.equals(this.getAwayTeam(), that.getAwayTeam()) &&
      Objects.equals(this.getHomeTeam(), that.getHomeTeam());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getAwayTeam(), getHomeTeam());
  }

  @Override
  public int compareTo(Match o) {
    if (o == null) {
      return 1;
    }
    int compareResult = this.getMatchTotalScore().compareTo(o.getMatchTotalScore());
    if (compareResult != 0) {
      return compareResult;
    }
    return this.getMatchTime().compareTo(o.getMatchTime());
  }

  private Integer getMatchTotalScore() {
    return this.getAwayTeamScore() + this.getHomeTeamScore();
  }

}
