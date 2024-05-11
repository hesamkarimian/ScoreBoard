package org.sportradar.scoreboard;

import org.sportradar.scoreboard.entities.Match;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author hesam.karimian
 * @created 10/05/2024
 */
public class ScoreBoardDAO {

    private final Set<Match> scoreBoard;

    public ScoreBoardDAO(Set<Match> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public int getNumberOfGames() {
//        return scoreBoard.size();
        return 0;
    }

    public void save(Match match) {
        scoreBoard.add(match);
    }

    public Optional<Match> findById(Integer matchId) {
        return scoreBoard.stream().filter(m -> m.getId().equals(matchId)).findFirst();
    }

    public Optional<Match> findByTeams(String homeTeam, String awayTeam) {
//        Match match = Match.getNewMatch(homeTeam, awayTeam);
//        return scoreBoard.stream().filter(m -> m.equals(match)).findFirst();
        return Optional.empty();
    }

    public Optional<Match> findByMatch(Match match) {
        return scoreBoard.stream().filter(m -> m.equals(match)).findFirst();
    }

    public List<Match> findByAll() {
//        return scoreBoard; //stream().sorted(Comparator.reverseOrder()).toList()
        return List.of();
    }

}
