package org.sportradar.scoreboard.services.impl;

import org.sportradar.scoreboard.ScoreBoardDAO;
import org.sportradar.scoreboard.entities.Match;
import org.sportradar.scoreboard.exceptions.DuplicateMatchException;
import org.sportradar.scoreboard.exceptions.InvalidInputException;
import org.sportradar.scoreboard.exceptions.MatchNotFoundException;
import org.sportradar.scoreboard.services.ScoreBoardService;

import java.util.List;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public class ScoreBoardServiceImpl implements ScoreBoardService {

    private final ScoreBoardDAO scoreBoardDAO;

    public ScoreBoardServiceImpl(ScoreBoardDAO scoreBoardDAO) {
        this.scoreBoardDAO = scoreBoardDAO;
    }

    @Override
    public List<Match> getSummary() {
        return scoreBoardDAO.findByAll();
    }

    @Override
    public Integer startNewMatch(String homeTeamName, String awayTeamName) {
        validateTeamName(homeTeamName);
        validateTeamName(awayTeamName);
        validateDifferentTeamNames(homeTeamName, awayTeamName);
        Match newMatch = Match.getNewMatch(homeTeamName, awayTeamName);
        validateNoDuplicate(newMatch);
        scoreBoardDAO.save(newMatch);
        return newMatch.getId();
    }

    private static void validateDifferentTeamNames(String homeTeamName, String awayTeamName) {
        if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
            throw new IllegalArgumentException("Team Name should not be similar.");
        }
    }

    private static void validateTeamName(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new InvalidInputException("Team Name");
        }
    }

    @Override
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

    private void validateNoDuplicate(Match match) {
        scoreBoardDAO.findByMatch(match).ifPresent(m ->
        {
            throw new DuplicateMatchException(match.getHomeTeam().getName(), match.getAwayTeam().getName());
        });
        scoreBoardDAO.findByMatch(getReverseMatch(match)).ifPresent(match1 -> {
            throw new DuplicateMatchException(match.getHomeTeam().getName(), match.getAwayTeam().getName());
        });
    }

    private static Match getReverseMatch(Match match) {
        return Match.getNewMatch(match.getAwayTeam().getName(), match.getHomeTeam().getName());
    }

    private Match findMatch(Integer matchId) {
        return scoreBoardDAO.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
    }

    private Match findMatch(String homeTeam, String awayTeam) {
//    Match match = Match.getNewMatch(homeTeam, awayTeam);
//    int index = scoreBoard.indexOf(match);
//    if (index < 0) {
//      throw new MatchNotFoundException(homeTeam, awayTeam);
//    }
//    return scoreBoard.get(index);
        return null;
    }

    @Override
    public void finishMatch(Integer matchId) {
    Match match = findMatch(matchId);
    scoreBoardDAO.delete(match);
    }

}
