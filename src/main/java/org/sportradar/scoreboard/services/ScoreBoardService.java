package org.sportradar.scoreboard.services;

import org.sportradar.scoreboard.ScoreBoardDAO;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
import org.sportradar.scoreboard.exceptions.MatchNotFoundException;
import org.sportradar.scoreboard.exceptions.NotAllowedException;

import java.util.List;

/**
 * A service keeping the score board and the operation on that for starting or updating a match
 * and showing a summary of score board
 *
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public class ScoreBoardService {

    private final ScoreBoardDAO scoreBoardDAO;

    public ScoreBoardService(ScoreBoardDAO scoreBoardDAO) {
        this.scoreBoardDAO = scoreBoardDAO;
    }

    /**
     * Get summary of the ongoing matches in an ordering way.
     *
     * @return An ordered list of {@link Match}
     */
    public List<Match> getSummary() {
        return scoreBoardDAO.findByAll();
    }

    /**
     * Start a new match and save it to the score board
     *
     * @param homeTeamName: name of the team that plays at home.
     * @param awayTeamName: name of the team that plays away.
     * @throws IllegalArgumentException if teamName is null or blank
     */
    public Integer startNewMatch(String homeTeamName, String awayTeamName) {
        validateTeamName(homeTeamName);
        validateTeamName(awayTeamName);
        validateDifferentTeamNames(homeTeamName, awayTeamName);
        Match newMatch = Match.getNewMatch(homeTeamName, awayTeamName);
        validateNoDuplicate(newMatch);
        scoreBoardDAO.save(newMatch);
        return newMatch.getId();
    }

    /**
     * Updates the score board with new scores.
     *
     * @param matchId       id of the match.
     * @param homeTeamScore number of goals home team had already scored.
     * @param awayTeamScore number of goals away team had already scored.
     * @throws IllegalArgumentException if teamNames are null or blank
     * @throws IllegalArgumentException if new scores are 0-0 or negative values.
     * @throws IllegalArgumentException if new scores are lower than previous values.
     * @throws MatchNotFoundException   if mach does not exist on the board with given HomeTeam and AwayTeam.
     */
    public void updateScore(Integer matchId, int homeTeamScore, int awayTeamScore) {
        if (matchId == null || matchId < 0) {
            throw new IllegalArgumentException("Match Id should be a valid positive number.");
        }
        Match match = findMatch(matchId);
        if (homeTeamScore < match.getHomeTeam().getScore() || awayTeamScore < match.getAwayTeam().getScore()) {
            throw new IllegalArgumentException("Team Scores can not be lower that previous scores.");
        }
        if (homeTeamScore + awayTeamScore == match.getHomeTeam().getScore() + match.getAwayTeam().getScore()) {
            throw new IllegalArgumentException("Team Scores can not be similar to previous scores.");
        }
        Match updatedMath = Match.getNewMatch(match, homeTeamScore, awayTeamScore);
        scoreBoardDAO.save(updatedMath);
    }

    /**
     * Finishes a match and remove it from the score board.
     *
     * @param matchId id of the match to be finished.
     * @throws MatchNotFoundException if mach does not exist on the board with given HomeTeam and AwayTeam.
     */
    public void finishMatch(Integer matchId) {
        if (matchId == null || matchId <= 0) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        Match match = findMatch(matchId);
        scoreBoardDAO.delete(match);
    }

    private static void validateDifferentTeamNames(String homeTeamName, String awayTeamName) {
        if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
            throw new IllegalArgumentException("Team Name should not be similar.");
        }
    }

    private static void validateTeamName(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team Name should not be null or empty.");
        }
    }

    private void validateNoDuplicate(Match match) {
        scoreBoardDAO.findByMatch(match).ifPresent(m ->
        {
            throw new DuplicateMatchException(match.getHomeTeam().getName(), match.getAwayTeam().getName());
        });
        scoreBoardDAO.findByMatch(getReverseMatch(match)).ifPresent(match1 -> {
            throw new DuplicateMatchException(match.getAwayTeam().getName(), match.getHomeTeam().getName());
        });

        scoreBoardDAO.findByTeam(match.getHomeTeam().getName())
                .ifPresent(m -> {
                    throw new NotAllowedException("Adding new match", match.getHomeTeam().getName() + " is already playing with another team");
                });
        scoreBoardDAO.findByTeam(match.getAwayTeam().getName())
                .ifPresent(m -> {
                    throw new NotAllowedException("Adding new match", match.getHomeTeam().getName() + " is already playing with another team");
                });
    }

    private static Match getReverseMatch(Match match) {
        return Match.getNewMatch(match.getAwayTeam().getName(), match.getHomeTeam().getName());
    }

    private Match findMatch(Integer matchId) {
        return scoreBoardDAO.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

}
