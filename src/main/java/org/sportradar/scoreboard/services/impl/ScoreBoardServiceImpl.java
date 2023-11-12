package org.sportradar.scoreboard.services.impl;

import org.sportradar.scoreboard.entities.Match;
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

  }

  @Override
  public void updateScore(Match match, int homeTeamScore, int awayTeamScore) {

  }

  @Override
  public void finishMatch(Match match) {

  }

}
