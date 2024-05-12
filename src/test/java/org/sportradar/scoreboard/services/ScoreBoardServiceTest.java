package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sportradar.scoreboard.ScoreBoardDAO;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.entities.Team;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
import org.sportradar.scoreboard.exceptions.MatchNotFoundException;
import org.sportradar.scoreboard.exceptions.NotAllowedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
class ScoreBoardServiceTest {

    private List<Match> scoreBoard;
    private ScoreBoardDAO scoreBoardDAO;
    private ScoreBoardService scoreBoardService;

    @BeforeEach
    void init() {
        scoreBoard = new ArrayList<>();
        scoreBoardDAO = new ScoreBoardDAO(scoreBoard);
        scoreBoardService = new ScoreBoardService(scoreBoardDAO);
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
    void startNewMatch_should_ThrowException_When_A_team_has_a_match_on_board() {
        scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(NotAllowedException.class,
                () -> scoreBoardService.startNewMatch("France", "USA"));

        assertThrows(NotAllowedException.class,
                () -> scoreBoardService.startNewMatch("Mexico", "France"));
    }

    @Test
    void startNewMatch_should_ThrowDuplicateMatchException_When_a_Return_match_exists() {
        scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(DuplicateMatchException.class,
                () -> scoreBoardService.startNewMatch("USA", "Mexico"));
    }

    @Test
    void startNewMatch_should_Throw_InvalidInputException_when_HomeTeamName_is_nullOrEmpty() {
        int initialSize = scoreBoard.size();

        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.startNewMatch(null, "USA"));
        assertEquals(initialSize, scoreBoard.size());

        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.startNewMatch("", "USA"));
        assertEquals(initialSize, scoreBoard.size());

        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.startNewMatch("   ", "USA"));
        assertEquals(initialSize, scoreBoard.size());
    }

    @Test
    void startNewMatch_should_Throw_InvalidInputException_when_AwayTeamName_is_nullOrEmpty() {
        int initialSize = scoreBoard.size();

        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.startNewMatch("USA", null));
        assertEquals(initialSize, scoreBoard.size());

        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.startNewMatch("USA", " "));
        assertEquals(initialSize, scoreBoard.size());
    }

    @Test
    void startNewMatch_should_Throw_IllegalArgumentException_when_BothTeams_are_similar() {
        int initialSize = scoreBoard.size();
        assertThrows(IllegalArgumentException.class,
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
    void updateScore_should_Throw_IllegalArgumentException_when_id_Is_Null() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(null, 5, 2));
    }

    @Test
    void updateScore_should_Throw_IllegalArgumentException_when_negative_id() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(-1, 5, 2));
    }

    @Test
    void updateScore_should_Throw_IllegalArgumentException_when_Negative_scores() {
        Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(id, -1, 0));
    }

    @Test
    void updateScore_should_Throw_IllegalArgumentException_when_update_To_Zero_scores() {
        Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(id, 0, 0));
    }

    @Test
    void updateScore_should_Throw_IllegalArgumentException_when_update_to_current_score() {
        Integer id = scoreBoardService.startNewMatch("Mexico", "USA");
        scoreBoardService.updateScore(id, 3, 3);
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.updateScore(id, 3, 3));
    }

    @Test
    void updateScore_should_Throw_IllegalArgumentException_when_Second_scores_are_lower() {
        int id = scoreBoardService.startNewMatch("Mexico", "USA");
        scoreBoardService.updateScore(id, 2, 2);
        assertThrows(IllegalArgumentException.class,
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
        assertThrows(MatchNotFoundException.class,
                () -> scoreBoardService.finishMatch(10));
    }

    @Test
    void finishMatch_should_ThrowException_when_Match_id_null() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.finishMatch(null));
    }

    @Test
    void finishMatch_should_ThrowException_when_Match_id_negative() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.finishMatch(-10));
    }

    @Test
    void finishMatch_should_ThrowException_when_Match_id_Zero() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreBoardService.finishMatch(0));
    }

    @Test
    void finishMatch_should_ThrowException_when_call_finish_twoTimes() {
        //GIVEN
        int id = scoreBoardService.startNewMatch("USA", "France");

        //WHEN
        scoreBoardService.finishMatch(id);

        //THEN
        assertThrows(MatchNotFoundException.class,
                () -> scoreBoardService.finishMatch(id));
    }

    @Test
    void finishMatch_should_remove_Match_when_Match_Exist() {
        //GIVEN
        int id = scoreBoardService.startNewMatch("USA", "France");

        //WHEN
        scoreBoardService.finishMatch(id);

        //THEN
        assertTrue(scoreBoardDAO.findById(id).isEmpty());
        assertEquals(0, scoreBoard.size());
    }

    @Test
    void getSummary_should_return_ordered_in_progress_matches() {
        //GIVEN
        int id1 = scoreBoardService.startNewMatch("Mexico", "Canada");
        scoreBoardService.updateScore(id1, 0, 5);

        int id2 = scoreBoardService.startNewMatch("Spain", "Brazil");
        scoreBoardService.updateScore(id2, 10, 2);

        int id3 = scoreBoardService.startNewMatch("Germany", "France");
        scoreBoardService.updateScore(id3, 2, 2);

        int id4 = scoreBoardService.startNewMatch("Uruguay", "Italy");
        scoreBoardService.updateScore(id4, 6, 6);

        int id5 = scoreBoardService.startNewMatch("Argentina", "Australia");
        scoreBoardService.updateScore(id5, 3, 1);

        //WHEN
        List<Match> result = scoreBoardService.getSummary();
        assertNotNull(result);
        assertEquals(scoreBoard.size(), result.size());
        assertTrue(scoreBoardDAO.findById(id4).isPresent());
        assertEquals(scoreBoardDAO.findById(id4).get(), result.get(0));
        assertTrue(scoreBoardDAO.findById(id2).isPresent());
        assertEquals(scoreBoardDAO.findById(id2).get(), result.get(1));
        assertTrue(scoreBoardDAO.findById(id1).isPresent());
        assertEquals(scoreBoardDAO.findById(id1).get(), result.get(2));
        assertTrue(scoreBoardDAO.findById(id5).isPresent());
        assertEquals(scoreBoardDAO.findById(id5).get(), result.get(3));
        assertTrue(scoreBoardDAO.findById(id3).isPresent());
        assertEquals(scoreBoardDAO.findById(id3).get(), result.get(4));
    }

    @Test
    void getSummary_should_return_Immutable_list() {
        //GIVEN
        int id1 = scoreBoardService.startNewMatch("Mexico", "Canada");

        //WHEN
        List<Match> result = scoreBoardService.getSummary();
        assertThrows(UnsupportedOperationException.class, () -> result.remove(1));
        assertThrows(UnsupportedOperationException.class, () -> result.add(Match.getNewMatch("USA", "France")));
    }
}
