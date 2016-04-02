package com.erill;

import com.erill.Entities.Match;
import com.erill.Entities.PlayerMatch;
import com.erill.Views.MainView;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Roger on 01/04/2016.
 */
public class Controller {
    private DatabaseManager db;
    private MainView view;
    private Scanner scanner;

    public Controller() {
        setupDB();
    }

    private void setupDB() {
        db = new DatabaseManager();
        db.setupDB();
    }

    public void start() {
        view.show("Benvingut al ELO Calculator, introdueixi una comanda");
        scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        while (!line.equals("quit")) {
            processInput(line);
            line = scanner.nextLine();
        }

        view.show("Adéu i fins aviat");

    }

    private void processInput(String line) {
        if (line.equals("1")) {
            addMatch();
        }
    }

    private void addMatch() {
        view.show("Creant partida, introdueix joc i data");
        String game = scanner.nextLine();
        String date = scanner.nextLine();
        int id = db.createMatch(game, date);
        if (id == 1) view.show("Error al crear partida");
        else { processMatch(db.getMatch(id)); }
    }

    private void processMatch(Match match) {
        view.show("Partida creada: Jornada " + match.getRound() + " jugant a " + match.getGame());
        db.prepareMatch(match);
        view.show("Introdueix participants, punts i escriu fi quan acabis");
        String line = scanner.nextLine();
        while (!line.equals("fi")) {
            line = processAddingPlayer(match, line);
        }
        List<PlayerMatch> playersPlaying = db.getPlayersPlaying(match);
        view.printGameRanking(playersPlaying);
    }

    private String processAddingPlayer(Match match, String line) {
        String name = line;
        Double points = Double.parseDouble(scanner.nextLine());
        if(db.setPoints(match, name, points) == -1) {
            view.show("Error al introduir els punts per a " + name);
        }
        line = scanner.nextLine();
        return line;
    }

    public MainView getView() {
        return view;
    }

    public void setView(MainView view) {
        this.view = view;
    }

    public void testGame() {
        Match match = createMatch("Catan", "");
        db.setPoints(match, "Rosa", 11.0);
        db.setPoints(match, "Teresa", 11.0);
        db.setPoints(match, "David", 15.0);
        db.setPoints(match, "Maria", 13.0);
        db.setPoints(match, "Jordi", 13.0);
        playMatch(match);

        match = createMatch("Catan", "");
        db.setPoints(match, "Rosa", 14.0);
        db.setPoints(match, "Teresa", 14.0);
        db.setPoints(match, "David", 10.0);
        db.setPoints(match, "Maria", 10.0);
        db.setPoints(match, "Jordi", 7.0);
        playMatch(match);

        match = createMatch("Catan", "28/03/2016");
        db.setPoints(match, "Rosa", 12.0);
        db.setPoints(match, "Teresa", 12.0);
        db.setPoints(match, "David", 15.0);
        db.setPoints(match, "Maria", 15.0);
        db.setPoints(match, "Roger", 10.0);
        db.setPoints(match, "Jordi", 10.0);
        playMatch(match);

    }

    private Match createMatch(String name, String date) {
        int id = db.createMatch(name,date);
        Match match = db.getMatch(id);
        db.prepareMatch(match);
        return match;
    }

    private void playMatch(Match match) {
        List<PlayerMatch> playersPlaying = db.getPlayersPlaying(match);
        PointsCalculator pointsCalculator = new PointsCalculator(playersPlaying);
        pointsCalculator.calculatePoints();
        playersPlaying = pointsCalculator.getPlayers();
        db.updateScores(playersPlaying);
    }
}
