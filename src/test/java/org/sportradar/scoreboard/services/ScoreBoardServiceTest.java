package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

  private List<Match> scoreBoard;
  private ScoreBoardService scoreBoardService;

  @BeforeEach
  void init() {
    scoreBoard = new ArrayList<>();
    scoreBoardService = new ScoreBoardServiceImpl(scoreBoard);
  }

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
  void startNewMatch_should_Throw_InvalidInputException_when_teamName_is_invalid(String firstParam,
                                                                                 String secondParam) {
    int size = scoreBoard.size();
    Result result = getResult(firstParam, secondParam);
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.startNewMatch(result.firstParam(), result.secondParam()));
    assertEquals(size, scoreBoard.size());
  }

  private static Result getResult(String firstParam, String secondParam) {
    if ("null".equals(firstParam)) {
      firstParam = null;
    } else if ("Blank".equals(firstParam)) {
      firstParam = "   ";
    }
    if ("null".equals(secondParam)) {
      secondParam = null;
    } else if ("Blank".equals(secondParam)) {
      secondParam = "   ";
    }
    return new Result(firstParam, secondParam);
  }

  private record Result(String firstParam, String secondParam) {

  }

  @Test
  void updateScore_should_success_when_valid_input() {
    //GIVEN
    String homeTeam = "Mexico";
    String awayTeam = "USA";
    scoreBoardService.startNewMatch(homeTeam, awayTeam);
    Match match = new Match(homeTeam, awayTeam);

    //WHEN
    scoreBoardService.updateScore(homeTeam, 5, awayTeam, 2);

    //THEN
    int matchIndex = scoreBoard.indexOf(match);
    assertEquals(5, scoreBoard.get(matchIndex).getHomeTeamScore());
    assertEquals(2, scoreBoard.get(matchIndex).getAwayTeamScore());
  }

  @ParameterizedTest
  @CsvSource({
    "null, USA",
    "Blank, USA",
    "Mexico, null",
    "Mexico, Blank"
  })
  void updateScore_should_Throw_InvalidInputException_when_invalid_teamNames(String firstParam, String secondParam) {
    Result result = getResult(firstParam, secondParam);
    assertThrows(InvalidInputException.class, () -> scoreBoardService.updateScore(result.firstParam, 5, result.secondParam, 2));
  }

  @ParameterizedTest
  @CsvSource({
    "-1, 0",
    "0, 0",
    "0, -1"
  })
  void updateScore_should_Throw_InvalidInputException_when_invalid_scores(String homeTeamScore, String awayTeamScore) {
    scoreBoardService.startNewMatch("Mexico", "USA");
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore("Mexico", Integer.parseInt(homeTeamScore), "USA",
                                                     Integer.parseInt(awayTeamScore)));
  }

  @ParameterizedTest
  @CsvSource({
    "2, 2, 1, 2",
    "2, 2, 2, 1"
  })
  void updateScore_should_Throw_InvalidInputException_when_Second_scores_are_lower(Integer firstHomeTeamScore,
                                                                          Integer firstAwayTeamScore,
                                                                          Integer secondHomeTeamScore,
                                                                          Integer secondAwayTeamScore) {
    String homeTeam = "Mexico";
    String awayTeam = "USA";
    scoreBoardService.startNewMatch(homeTeam, awayTeam);
    scoreBoardService.updateScore(homeTeam, firstHomeTeamScore, awayTeam, firstAwayTeamScore);
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore("Mexico", secondAwayTeamScore, "USA", secondHomeTeamScore));
  }

}
