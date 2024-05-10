package org.sportradar.scoreboard.entities;

import java.util.Objects;

/**
 * @author hesam.karimian
 * @created 10/05/2024
 */
public final class Team {

    private final String name;
    private Integer score;

    public Team(String name) {
        this.name = name;
        this.score = 0;
    }

    public Team(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team that)) {
            return false;
        }
        return Objects.equals(this.getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

}
