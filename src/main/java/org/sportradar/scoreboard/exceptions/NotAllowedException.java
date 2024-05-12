package org.sportradar.scoreboard.exceptions;

/**
 * @author hesam.karimian
 * @created 12/05/2024
 */
public class NotAllowedException extends RuntimeException {

    public NotAllowedException(String action, String reason) {
        super(String.format("It is not allowed to %s when %s", action, reason));
    }
}
