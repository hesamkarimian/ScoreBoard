package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.sportradar.scoreboard.ScoreBoardDAO;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.entities.Team;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.exceptions.MatchNotFoundException;
import org.sportradar.scoreboard.services.impl.ScoreBoardServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
class ScoreBoardServiceTest {

    private Set<Match> scoreBoard;
    private ScoreBoardDAO scoreBoardDAO;
    private ScoreBoardService scoreBoardService;

    @BeforeEach
    void init() {
        scoreBoard = new HashSet<>();
        scoreBoardDAO = new ScoreBoardDAO(scoreBoard);
        scoreBoardService = new ScoreBoardServiceImpl(scoreBoardDAO);
    }

    @Test
    void startNewMatch_should_Add_new_match_when_valid_input() {
        int size = scoreBoard.size();
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("USA");
        int id = scoreBoardService.startNewMatch("Mexico", "USA");
        size++;
        assertEquals(size, scoreBoard.size());
        assertTrue(id > 0);
        assertTrue(scoreBoardDAO.findById(id).isPresent());
        Match match = scoreBoardDAO.findById(id).get();
        assertEquals(0, match.getHomeTeam().getScore());
        assertEquals(0, match.getAwayTeam().getScore());
        assertEquals(homeTeam.getName(), match.getHomeTeam().getName());
        assertEquals(awayTeam.getName(), match.getAwayTeam().getName());
    }

    @Test
    void startNewMatch_should_ThrowDuplicateMatchException_When_add_a_match_two_times() {
        scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(DuplicateMatchException.class,
                () -> scoreBoardService.startNewMatch("Mexico", "USA"));
    }

    @Test
    void startNewMatch_should_ThrowDuplicateMatchException_When_a_match_exist_with_same_teams() {
        scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(DuplicateMatchException.class,
                () -> scoreBoardService.startNewMatch("USA", "Mexico"));
    }

    @Test
    void startNewMatch_should_Throw_InvalidInputException_when_HomeTeamName_is_nullOrEmpty() {
        int initialSize = scoreBoard.size();

        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.startNewMatch(null, "USA"));
        assertEquals(initialSize, scoreBoard.size());

        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.startNewMatch("", "USA"));
        assertEquals(initialSize, scoreBoard.size());
    }

    @Test
    void startNewMatch_should_Throw_InvalidInputException_when_AwayTeamName_is_nullOrEmpty() {
        int initialSize = scoreBoard.size();

        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.startNewMatch("USA", null));
        assertEquals(initialSize, scoreBoard.size());

        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.startNewMatch("USA", " "));
        assertEquals(initialSize, scoreBoard.size());
    }

    @Test
    void startNewMatch_should_Throw_InvalidInputException_when_BothTeams_are_similar() {
        int initialSize = scoreBoard.size();
        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.startNewMatch("USA", "USA"));
        assertEquals(initialSize, scoreBoard.size());
    }

    @Test
    void updateScore_should_success_when_valid_input() {
        //GIVEN
        int id = scoreBoardService.startNewMatch("Mexico", "USA");

        //WHEN
        scoreBoardService.updateScore(id, 5, 2);

        //THEN
        assertTrue(scoreBoardDAO.findById(id).isPresent());
        Match match = scoreBoardDAO.findById(id).get();
        assertEquals(5, match.getHomeTeam().getScore());
        assertEquals(2, match.getAwayTeam().getScore());
    }

  @Test
  void updateScore_should_Throw_InvalidInputException_when_id() {
    assertThrows(IllegalArgumentException.class,
                 () -> scoreBoardService.updateScore(null, 5, 2));
  }


    @Test
    void updateScore_should_Throw_InvalidInputException_when_Negative_scores() {
        Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(id, -1, 0));
    }

  @Test
  void updateScore_should_Throw_InvalidInputException_when_update_To_Zero_scores() {
    Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
    assertThrows(IllegalArgumentException.class,
            () -> scoreBoardService.updateScore(id, 0, 0));
  }

    @Test
    void updateScore_should_Throw_InvalidInputException_when_update_to_current_score() {
        Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
        scoreBoardService.updateScore(id, 3, 3);
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(id, 3, 3));
    }

    @Test
    void updateScore_should_Throw_InvalidInputException_when_Second_scores_are_lower() {
        int id = scoreBoardService.startNewMatch("Mexico", "USA");
        scoreBoardService.updateScore(id, 2, 2);
        assertThrows(InvalidInputException.class,
                () -> scoreBoardService.updateScore(id, 1, 2));
    }

    @Test
    void updateScore_should_ThrowMatchNotFoundException_when_Match_does_notExist() {
        //GIVEN
        scoreBoardService.startNewMatch("Mexico", "USA");

        //THEN
        assertThrows(MatchNotFoundException.class,
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
        scoreBoardService.updateScore(id, 2, 2);

        id = scoreBoardService.startNewMatch("Uruguay", "Italy");
        scoreBoardService.updateScore(id, 6, 6);

        id = scoreBoardService.startNewMatch("Argentina", "Australia");
        scoreBoardService.updateScore(id, 3, 1);

        //WHEN
        List<Match> result = scoreBoardService.getSummary();
//    assertNotNull(result);
//    assertEquals(scoreBoard.size(), result.size());
//    assertEquals(scoreBoard.get(3), result.get(0));
//    assertEquals(scoreBoard.get(1), result.get(1));
//    assertEquals(scoreBoard.get(0), result.get(2));
//    assertEquals(scoreBoard.get(4), result.get(3));
//    assertEquals(scoreBoard.get(2), result.get(4));
    }

}
