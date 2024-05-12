package org.sportradar.scoreboard;

import org.sportradar.scoreboard.entities.Match;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author hesam.karimian
 * @created 10/05/2024
 */
public class ScoreBoardDAO {

    private final List<Match> scoreBoard;

    public ScoreBoardDAO(List<Match> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void save(Match match) {
        int index = scoreBoard.indexOf(match);
        if (index >= 0) {
            scoreBoard.set(index, match);
        } else {
            scoreBoard.add(match);
        }
    }

    public void delete(Match match) {
        scoreBoard.remove(match);
    }

    public Optional<Match> findById(Integer matchId) {
        return scoreBoard.stream().filter(m -> m.getId().equals(matchId)).findFirst();
    }

    public Optional<Match> findByMatch(Match match) {
        return scoreBoard.stream().filter(m -> m.equals(match)).findFirst();
    }

    public List<Match> findByAll() {
        return scoreBoard.stream().sorted(Comparator.reverseOrder()).toList();
    }

}
