package org.sportradar.scoreboard.services.impl;

import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.services.ScoreBoardService;

import java.util.List;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public class ScoreBoardServiceImpl implements ScoreBoardService {

  private final List<Match> scoreBoard;

  public ScoreBoardServiceImpl(List<Match> scoreBoard) {
    this.scoreBoard = scoreBoard;
  }

  @Override
  public List<Match> getSummary() {
    return null;
  }

  @Override
  public void startNewMatch(String homeTeam, String awayTeam) {
    validateTeamName(homeTeam);
    validateTeamName(awayTeam);
    Match newMatch = new Match(homeTeam, awayTeam);
    scoreBoard.add(newMatch);
  }

  private static void validateTeamName(String teamName) {
    if (teamName == null || teamName.isBlank()) {
      throw new InvalidInputException("Team Name");
    }
  }

  @Override
  public void updateScore(String homeTeam, int homeTeamScore, String awayTeam, int awayTeamScore) {
    validateTeamName(homeTeam);
    validateTeamName(awayTeam);

    if (homeTeamScore < 0 || awayTeamScore < 0 || homeTeamScore + awayTeamScore == 0) {
      throw new InvalidInputException("Team Score");
    }

    Match match = findMatch(homeTeam, awayTeam);
    if (homeTeamScore < match.getHomeTeamScore() || awayTeamScore < match.getAwayTeamScore()) {
      throw new InvalidInputException("Team Score");
    }
    match.setHomeTeamScore(homeTeamScore);
    match.setAwayTeamScore(awayTeamScore);
  }

  private Match findMatch(String homeTeam, String awayTeam) {
    Match match = new Match(homeTeam, awayTeam);
    int index = scoreBoard.indexOf(match);
    if (index < 0) {
      throw new InvalidInputException("Team Name");
    }
    return scoreBoard.get(index);
  }

  @Override
  public void finishMatch(Match match) {

  }

}
