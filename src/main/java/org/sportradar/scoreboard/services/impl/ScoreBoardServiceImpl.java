package org.sportradar.scoreboard.services.impl;

import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.entities.Team;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.exceptions.MatchNotFoundException;
import org.sportradar.scoreboard.services.ScoreBoardService;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    return scoreBoard.stream().sorted(Comparator.reverseOrder()).toList();
  }

  @Override
  public Integer startNewMatch(String homeTeamName, String awayTeamName) {
    validateTeamName(homeTeamName);
    validateTeamName(awayTeamName);
    validateDifferentTeamNames(homeTeamName, awayTeamName);
    Match newMatch = Match.getNewMatch(homeTeamName, awayTeamName);
    validateNoMatchExist(homeTeamName, awayTeamName);
    scoreBoard.add(newMatch);
    return null;
  }

  private static void validateDifferentTeamNames(String homeTeamName, String awayTeamName) {
    if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
      throw new InvalidInputException("Team Name");
    }
  }

  private static void validateTeamName(String teamName) {
    if (teamName == null || teamName.isBlank()) {
      throw new InvalidInputException("Team Name");
    }
  }

  @Override
  public void updateScore(Integer matchId, int homeTeamScore,int awayTeamScore) {
//    validateTeamName(homeTeam);
//    validateTeamName(awayTeam);

    if (homeTeamScore < 0 || awayTeamScore < 0 || homeTeamScore + awayTeamScore == 0) {
      throw new InvalidInputException("Team Score");
    }

//    Match match = findMatch(homeTeam, awayTeam);
//    if (homeTeamScore < match.getHomeTeamScore() || awayTeamScore < match.getAwayTeamScore()) {
//      throw new InvalidInputException("Team Score");
//    }
//    match.setHomeTeamScore(homeTeamScore);
//    match.setAwayTeamScore(awayTeamScore);
  }

  private void validateNoMatchExist(String teamA, String teamB) {
    Match match = Match.getNewMatch(teamA, teamB);
    int index = scoreBoard.indexOf(match);
    if (index >= 0) {
      throw new DuplicateMatchException(teamA, teamB);
    }
  }

  private Match findMatch(String homeTeam, String awayTeam) {
    Match match = Match.getNewMatch(homeTeam, awayTeam);
    int index = scoreBoard.indexOf(match);
    if (index < 0) {
      throw new MatchNotFoundException(homeTeam, awayTeam);
    }
    return scoreBoard.get(index);
  }

  @Override
  public void finishMatch(Integer matchId) {
//    Match match = findMatch(homeTeam, awayTeam);
//    scoreBoard.remove(match);
  }

}
