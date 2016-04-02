package com.erill.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Roger on 01/01/2016.
 */
@DatabaseTable(tableName = "Match")
public class Match {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "Game")
    private String game;

    @DatabaseField(columnName = "DatePlayed")
    private String date;

    @DatabaseField(columnName = "Round")
    private int round;

    @DatabaseField(columnName = "PreviousMatch", foreign = true)
    private Match previousMatch;

    @DatabaseField(columnName = "NextMatch", foreign = true)
    private Match nextMatch;

    Match() {

    }

    public Match(String game, String date, int round, Match previousMatch, Match nextMatch) {
        this.game = game;
        this.date = date;
        this.round = round;
        this.previousMatch = previousMatch;
        this.nextMatch = nextMatch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Match getPreviousMatch() {
        return previousMatch;
    }

    public void setPreviousMatch(Match previousMatch) {
        this.previousMatch = previousMatch;
    }

    public Match getNextMatch() {
        return nextMatch;
    }

    public void setNextMatch(Match nextMatch) {
        this.nextMatch = nextMatch;
    }

    @Override
    public String toString() {
        return "[" + this.round + "]  " + this.date + "  " + this.game;
    }
}
