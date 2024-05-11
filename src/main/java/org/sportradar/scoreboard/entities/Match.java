package org.sportradar.scoreboard.entities;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public final class Match implements Comparable<Match> {

  private static final AtomicInteger globalId = new AtomicInteger(0);

  private final Integer id;
  private final Team homeTeam;
  private final Team awayTeam;

  public static Match getNewMatch(String homeTeamName, String awayTeamName) {
    Team homeTeam = new Team(homeTeamName);
    Team awayteam = new Team(awayTeamName);
    Integer newId = globalId.incrementAndGet();
    return new Match(newId, homeTeam, awayteam);
  }

  public static Match getNewMatch(Match match, int homeTeamScore, int awayTeamScore) {
    Team homeTeam = new Team(match.getHomeTeam().getName(), homeTeamScore);
    Team awayteam = new Team(match.getAwayTeam().getName(), awayTeamScore);
    return new Match(match.getId(), homeTeam, awayteam);
  }

  private Match(Integer id, Team homeTeam, Team awayTeam) {
    this.id = id;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }

  public Integer getId() {
    return id;
  }

  public Team getHomeTeam() {
    return new Team(homeTeam.getName(), homeTeam.getScore());
  }

  public Team getAwayTeam() {
    return new Team(awayTeam.getName(), awayTeam.getScore());
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
           Objects.equals(this.getHomeTeam(), that.getHomeTeam()) ||
           Objects.equals(this.getAwayTeam(), that.getHomeTeam()) &&
           Objects.equals(this.getHomeTeam(), that.getAwayTeam()) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getAwayTeam().getName(), getHomeTeam().hashCode());
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
    return this.getId().compareTo(o.getId());
  }

  private Integer getMatchTotalScore() {
    return this.getAwayTeam().getScore() + this.getHomeTeam().getScore();
  }

}
