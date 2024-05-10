package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.entities.Team;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
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
    Team homeTeam = new Team("Mexico");
    Team awayTeam = new Team("USA");
    int id = scoreBoardService.startNewMatch("Mexico", "USA");
    size++;
    assertEquals(size, scoreBoard.size());
    assertEquals(0, scoreBoard.get(size-1).getHomeTeam().getScore());
    assertEquals(0, scoreBoard.get(size-1).getAwayTeam().getScore());
    assertEquals(homeTeam.getName(), scoreBoard.get(size-1).getHomeTeam().getName());
    assertEquals(awayTeam.getName(), scoreBoard.get(size-1).getAwayTeam().getName());
  }

  @Test
  void startNewMatch_should_ThrowInvalidInput_When_add_a_match_two_times() {
    int size = scoreBoard.size();
    Team homeTeam = new Team("Mexico");
    Team awayTeam = new Team("USA");
    scoreBoardService.startNewMatch("Mexico", "USA");
    size++;
    assertEquals(size, scoreBoard.size());
    assertEquals(0, scoreBoard.get(size-1).getHomeTeam().getScore());
    assertEquals(0, scoreBoard.get(size-1).getAwayTeam().getScore());
    assertEquals(homeTeam.getName(), scoreBoard.get(size-1).getHomeTeam().getName());
    assertEquals(awayTeam.getName(), scoreBoard.get(size-1).getAwayTeam().getName());
    assertThrows(DuplicateMatchException.class,
            () -> scoreBoardService.startNewMatch("Mexico", "USA"));
  }

  @Test
  void startNewMatch_should_Throw_InvalidInputException_when_HomeTeamName_is_nullOrEmpty() {
    int size = scoreBoard.size();

    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.startNewMatch(null, "USA"));
    assertEquals(size, scoreBoard.size());

    assertThrows(InvalidInputException.class,
            () -> scoreBoardService.startNewMatch("", "USA"));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void startNewMatch_should_Throw_InvalidInputException_when_AwayTeamName_is_nullOrEmpty() {
    int size = scoreBoard.size();

    assertThrows(InvalidInputException.class,
            () -> scoreBoardService.startNewMatch("USA", null));
    assertEquals(size, scoreBoard.size());

    assertThrows(InvalidInputException.class,
            () -> scoreBoardService.startNewMatch("USA", " "));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void startNewMatch_should_Throw_InvalidInputException_when_BothTeams_are_similar() {
    int size = scoreBoard.size();
    assertThrows(InvalidInputException.class,
            () -> scoreBoardService.startNewMatch("USA", "USA"));
    assertEquals(size, scoreBoard.size());
  }

  @Test
  void updateScore_should_success_when_valid_input() {
    //GIVEN
    int id = scoreBoardService.startNewMatch("Mexico", "USA");
    Match match = Match.getNewMatch("Mexico", "USA");

    //WHEN
    scoreBoardService.updateScore(id, 5, 2);

    //THEN
    int matchIndex = scoreBoard.indexOf(match);
    assertEquals(5, scoreBoard.get(matchIndex).getHomeTeam().getScore());
    assertEquals(2, scoreBoard.get(matchIndex).getAwayTeam().getScore());
  }

//  @ParameterizedTest
//  @CsvSource({
//    "null, USA",
//    "Blank, USA",
//    "Mexico, null",
//    "Mexico, Blank"
//  })
//  void updateScore_should_Throw_InvalidInputException_when_invalid_teamNames(String firstParam, String secondParam) {
//    Result result = getResult(firstParam, secondParam);
//    assertThrows(InvalidInputException.class,
//                 () -> scoreBoardService.updateScore(result.firstParam, 5, result.secondParam, 2));
//  }

//  @ParameterizedTest
//  @CsvSource({
//    "-1, 0",
//    "0, 0",
//    "0, -1"
//  })
//  void updateScore_should_Throw_InvalidInputException_when_invalid_scores(Integer homeTeamScore,
//                                                                          Integer awayTeamScore) {
//    scoreBoardService.startNewMatch("Mexico", "USA");
//    assertThrows(InvalidInputException.class,
//                 () -> scoreBoardService.updateScore("Mexico", homeTeamScore, "USA", awayTeamScore));
//  }

  @ParameterizedTest
  @CsvSource({
    "2, 2, 1, 2",
    "2, 2, 2, 1"
  })
  void updateScore_should_Throw_InvalidInputException_when_Second_scores_are_lower(Integer firstHomeTeamScore,
                                                                                   Integer firstAwayTeamScore,
                                                                                   Integer secondHomeTeamScore,
                                                                                   Integer secondAwayTeamScore) {
    Team homeTeam = new Team("Mexico");
    Team awayTeam = new Team("USA");
    int id = scoreBoardService.startNewMatch("Mexico", "USA");
    scoreBoardService.updateScore(id, firstHomeTeamScore,firstAwayTeamScore);
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore(id, secondAwayTeamScore, secondHomeTeamScore));
  }

  @Test
  void updateScore_should_ThrowException_when_Match_does_notExist() {
    //GIVEN
    scoreBoardService.startNewMatch("Mexico", "USA");

    //THEN
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.updateScore(10, 5, 2));

  }

  @Test
  void finishMatch_should_ThrowException_when_Match_does_notExist() {
    assertThrows(InvalidInputException.class,
                 () -> scoreBoardService.finishMatch(10));
  }

  @Test
  void finishMatch_should_remove_Match_when_Match_Exist() {
    //GIVEN
    String homeTeam = "USA";
    String awayTeam = "France";
    int id = scoreBoardService.startNewMatch(homeTeam, awayTeam);

    //WHEN
    scoreBoardService.finishMatch(id);

    //THEN
    assertEquals(0, scoreBoard.size());
  }

  @Test
  void getSummary_should_return_ordered_in_progress_matches() throws InterruptedException {
    //GIVEN
    int id = scoreBoardService.startNewMatch("Mexico", "Canada");
    scoreBoardService.updateScore(id, 0, 5);

    id = scoreBoardService.startNewMatch("Spain", "Brazil");
    scoreBoardService.updateScore(id, 10, 2);

    id = scoreBoardService.startNewMatch("Germany", "France");
    scoreBoardService.updateScore(id, 2,2);

    id = scoreBoardService.startNewMatch("Uruguay", "Italy");
    scoreBoardService.updateScore(id, 6, 6);

    id = scoreBoardService.startNewMatch("Argentina", "Australia");
    scoreBoardService.updateScore(id, 3, 1);

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
