package org.sportradar.scoreboard.exceptions;

/**
 * @author hesam.karimian
 * @created 10/05/2024
 */
public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String teamA, String teamB) {
        super(String.format("No Match Found between team %s and team %b ", teamA, teamB));
    }

    public MatchNotFoundException(Integer matchId) {
        super(String.format("No Match Found with id: %d ", matchId));
    }
}
