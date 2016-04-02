package com.erill.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Roger on 01/01/2016.
 */
@DatabaseTable(tableName = "Player")
public class Player {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "Name")
    private String name;

    Player() {

    }

    public Player (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
