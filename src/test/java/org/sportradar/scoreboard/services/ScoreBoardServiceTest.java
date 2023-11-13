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

import static org.junit.jupiter.api.Assertions.*;

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
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore(result.firstParam, 5, result.secondParam, 2));
  }

  @ParameterizedTest
  @CsvSource({
    "-1, 0",
    "0, 0",
    "0, -1"
  })
  void updateScore_should_Throw_InvalidInputException_when_invalid_scores(Integer homeTeamScore,
                                                                          Integer awayTeamScore) {
    scoreBoardService.startNewMatch("Mexico", "USA");
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore("Mexico", homeTeamScore, "USA", awayTeamScore));
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

  @Test
  void updateScore_should_ThrowException_when_Match_does_notExist() {
    //GIVEN
    String homeTeam = "Mexico";
    String awayTeam = "USA";
    scoreBoardService.startNewMatch(homeTeam, awayTeam);

    //THEN
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore("France", 5, "USA", 2));

  }

  @Test
  void finishMatch_should_ThrowException_when_Match_does_notExist() {
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.finishMatch("USA", "France"));
  }

  @Test
  void finishMatch_should_remove_Match_when_Match_Exist() {
    //GIVEN
    String homeTeam = "USA";
    String awayTeam = "France";
    scoreBoardService.startNewMatch(homeTeam, awayTeam);

    //WHEN
    scoreBoardService.finishMatch("USA", "France");

    //THEN
    assertEquals(0, scoreBoard.size());
  }

  @Test
  void getSummary_should_return_ordered_in_progress_matches() throws InterruptedException {
    //GIVEN
    scoreBoardService.startNewMatch("Mexico", "Canada");
    scoreBoardService.updateScore("Mexico", 0, "Canada", 5);

    Thread.sleep(10);
    scoreBoardService.startNewMatch("Spain", "Brazil");
    scoreBoardService.updateScore("Spain", 10, "Brazil", 2);

    Thread.sleep(10);
    scoreBoardService.startNewMatch("Germany", "France");
    scoreBoardService.updateScore("Germany", 2, "France", 2);

    Thread.sleep(10);
    scoreBoardService.startNewMatch("Uruguay", "Italy");
    scoreBoardService.updateScore("Uruguay", 6, "Italy", 6);

    Thread.sleep(10);
    scoreBoardService.startNewMatch("Argentina", "Australia");
    scoreBoardService.updateScore("Argentina", 3, "Australia", 1);

    //WHEN
    List<Match> result = scoreBoardService.getSummary();
    assertNotNull(result);
    assertEquals(scoreBoard.size(), result.size());
    assertEquals(scoreBoard.get(3), result.get(0));
    assertEquals(scoreBoard.get(1), result.get(1));
    assertEquals(scoreBoard.get(0), result.get(2));
    assertEquals(scoreBoard.get(4), result.get(3));
    assertEquals(scoreBoard.get(2), result.get(4));
  }

}
