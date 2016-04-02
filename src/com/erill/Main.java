package com.erill;

import com.erill.Views.MainView;
import com.j256.ormlite.logger.LocalLog;

import java.sql.SQLException;

public class Main {

    private DatabaseManager db;

    public static void main(String[] args) {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        new Main().run(args);
    }

    private void run(String[] args) {
        Controller c = new Controller();
        c.setView(new MainView());
        //c.start();
        c.testGame();
    }

    private void setupDB() throws SQLException {

        db = new DatabaseManager();
        db.setupDB();

        /*int idMatch = db.createMatch("Test", "19/02/2016", 7);
        db.addPlayerMatch(1,idMatch,5);
        db.addPlayerMatch(2,idMatch,6);
        db.addPlayerMatch(3,idMatch,12);
        db.addPlayerMatch(4,idMatch,15);
        db.addPlayerMatch(5,idMatch,8);
        db.addPlayerMatch(6,idMatch,9);*/
    }

}
