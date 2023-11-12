package org.sportradar.scoreboard.services;

import org.junit.jupiter.api.Test;
import org.sportradar.scoreboard.services.impl.ScoreBoardServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Hesam.Karimian
 * @since 12.11.2023
 */
class ScoreBoardServiceTest {

  @Test
  void getNumberOfActiveMatches_should_return_Zero_when_just_created() {
    ScoreBoardService emptyScoreBoardService = new ScoreBoardServiceImpl();
    assertEquals(0, emptyScoreBoardService.getNumberOfActiveMatches());
  }


}
