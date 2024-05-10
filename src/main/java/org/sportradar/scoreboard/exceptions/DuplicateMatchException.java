package org.sportradar.scoreboard.exceptions;

/**
 * @author hesam.karimian
 * @created 10/05/2024
 */
public class DuplicateMatchException extends RuntimeException {
    public DuplicateMatchException(String teamA, String teamB) {
        super(String.format("A Match between team %s and team %b already exists.", teamA, teamB));
    }
}
