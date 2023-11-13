package org.sportradar.scoreboard.services;

import org.sportradar.scoreboard.entities.Match;

import java.util.List;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public interface ScoreBoardService {

    List<Match> getSummary();

    void startNewMatch(String homeTeam, String awayTeam);

    void updateScore(String homeTeam, int homeTeamScore, String awayTeam, int awayTeamScore);

    void finishMatch(Match match);

}
