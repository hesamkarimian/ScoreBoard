package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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

  @ParameterizedTest
  @CsvSource({
    "null, USA",
    "Blank, USA",
    "Mexico, null",
    "Mexico, Blank"
  })
  void startNewMatch_should_Throw_InvalidException_when_homeTeam_is_null(String firstParam, String secondParam) {
    int size = scoreBoard.size();
    if ("null".equals(firstParam)) {
      firstParam = null;
    }else if("Blank".equals(firstParam)){
      firstParam = "   ";
    }
    if ("null".equals(secondParam)) {
      secondParam = null;
    }else if("Blank".equals(secondParam)){
      secondParam = "   ";
    }
    String homeTeam = firstParam;
    String awayTeam = secondParam;
    assertThrows(InvalidInputException.class, () -> scoreBoardService.startNewMatch(homeTeam, awayTeam));
    assertEquals(size, scoreBoard.size());
  }

}
