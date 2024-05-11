package org.sportradar.scoreboard.exceptions;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
public class InvalidInputException extends IllegalArgumentException {

  public InvalidInputException(String inputName) {
    super("Invalid value for " + inputName);
  }

}
