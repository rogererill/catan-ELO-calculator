package com.erill.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Roger on 13/02/2016.
 */
@DatabaseTable(tableName = "PlayerMatch")
public class PlayerMatch {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "IdPlayer", foreign = true)
    private Player player;

    @DatabaseField(columnName = "IdMatch", foreign = true)
    private Match match;

    @DatabaseField(columnName = "Points")
    private Double points;

    @DatabaseField(columnName = "TotalPoints")
    private double totalPoints;

    PlayerMatch() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public PlayerMatch(Player player, Match match, Double points) {
        this.player = player;
        this.match = match;
        this.points = points;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }
}
