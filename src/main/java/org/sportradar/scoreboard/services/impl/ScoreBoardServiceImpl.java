package org.sportradar.scoreboard.services.impl;

import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.services.ScoreBoardService;

import java.util.List;
import java.util.Objects;

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
      throw new InvalidInputException("homeTeam");
    }
  }

  @Override
  public void updateScore(Match match, int homeTeamScore, int awayTeamScore) {

  }

  @Override
  public void finishMatch(Match match) {

  }

}
