package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.Test;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.services.impl.ScoreBoardServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
class ScoreBoardServiceTest {

  List<Match> scoreBoard = new ArrayList<>();
  private final ScoreBoardService scoreBoardService = new ScoreBoardServiceImpl(scoreBoard);

  @Test
  void startNewMatch_should_Add_new_match_when_valid_input() {
    int size = scoreBoard.size();
    String homeTeam = "Mexico";
    String awayTeam = "USA";
    scoreBoardService.startNewMatch(homeTeam, awayTeam);
    assertEquals(size + 1, scoreBoard.size());
    assertEquals(0, scoreBoard.get(size).getHomeTeamScore());
    assertEquals(0, scoreBoard.get(size).getAwayTeamScore());
    assertEquals(homeTeam, scoreBoard.get(size).getHomeTeam());
    assertEquals(awayTeam, scoreBoard.get(size).getAwayTeam());
  }

  @Test
  void startNewMatch_should_Throw_InvalidException_when_homeTeam_is_null() {
    int size = scoreBoard.size();
    assertThrows(InvalidInputException.class, () -> scoreBoardService.startNewMatch(null, "USA"));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void startNewMatch_should_Throw_InvalidException_when_homeTeam_is_blank() {
    int size = scoreBoard.size();
    assertThrows(InvalidInputException.class, () -> scoreBoardService.startNewMatch("  ", "USA"));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void startNewMatch_should_Throw_InvalidException_when_awayTeam_is_null() {
    int size = scoreBoard.size();
    assertThrows(InvalidInputException.class, () -> scoreBoardService.startNewMatch("Mexico", null));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void startNewMatch_should_Throw_InvalidException_when_awayTeam_is_Blank() {
    int size = scoreBoard.size();
    assertThrows(InvalidInputException.class, () -> scoreBoardService.startNewMatch("Mexico", "  "));
    assertEquals(size, scoreBoard.size());
  }

}
