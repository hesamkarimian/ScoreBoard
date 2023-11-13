package org.sportradar.scoreboard.services;

import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.InvalidInputException;

import java.util.List;

/**
 * A service keeping the score board and the operation on that for starting or updating a match
 * and showing a summary of score board
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public interface ScoreBoardService {

    /**
     * Get summary of the ongoing matches in an ordering way.
     * @return An ordered list of {@link Match}
     */
    List<Match> getSummary();

    /**
     * Start a new match and add it to the score board
     * @param homeTeam: name of the team that plays at home.
     * @param awayTeam: name of the team that plays away.
     * @throws InvalidInputException if teamName is null or blank
     */
    void startNewMatch(String homeTeam, String awayTeam);

    /**
     * Updates the score board with new scores.
     * @param homeTeam name of the team that plays at home.
     * @param homeTeamScore number of goals home team had already scored.
     * @param awayTeam name of the team that plays away.
     * @param awayTeamScore number of goals away team had already scored.
     * @throws InvalidInputException if teamNames are null or blank
     * @throws InvalidInputException if new scores are 0-0 or negative values.
     * @throws InvalidInputException if new scores are lower than previous values.
     * @throws InvalidInputException if mach does not exist on the board with given HomeTeam and AwayTeam.
     */
    void updateScore(String homeTeam, int homeTeamScore, String awayTeam, int awayTeamScore);

    /**
     * Finishes a match and remove it from the score board.
     * @param homeTeam name of the team that plays at home.
     * @param awayTeam name of the team that plays away.
     * @throws InvalidInputException if mach does not exist on the board with given HomeTeam and AwayTeam.
     */
    void finishMatch(String homeTeam, String awayTeam);

}
