package com.erill;

import com.erill.Entities.Match;
import com.erill.Entities.Player;
import com.erill.Entities.PlayerMatch;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 13/02/2016.
 */
public class DatabaseManager {
    private Dao<Player, Integer> playerDao;
    private Dao<Match, Integer> matchDao;
    private Dao<PlayerMatch, Integer> playerMatchDao;

    DatabaseManager() {

    }

    public void setupDB() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(Constants.DATABASE_URL);
            playerDao = DaoManager.createDao(connectionSource, Player.class);
            matchDao = DaoManager.createDao(connectionSource, Match.class);
            playerMatchDao = DaoManager.createDao(connectionSource, PlayerMatch.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  Dao<Player, Integer> getPlayerDao() {
        return playerDao;
    }

    public  Dao<Match, Integer> getMatchDao() {
        return matchDao;
    }

    public  Dao<PlayerMatch, Integer> getPlayerMatchDao() {
        return playerMatchDao;
    }

    public int createEntity(Dao d, Object o) {
        try {
            return d.create(o);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int updateEntity(Dao d, Object o) {
        try {
            return d.update(o);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int saveMatch(Match match, Match previousMatch) {
        int operationOk = createEntity(matchDao, match);
        if (operationOk > 0) {
            operationOk = updateEntity(matchDao, previousMatch);
            if (operationOk > 0) {
                return match.getId();
            }
        }
        return -1;
    }

    public Pair<Integer, Integer> savePlayerMatch(PlayerMatch playerMatch) {
        int insertOk = createEntity(playerMatchDao, playerMatch);
        if (insertOk > 0) {
            return new Pair<>(playerMatch.getPlayer().getId(), playerMatch.getMatch().getId());
        }
        return new Pair<>(-1,-1);
    }

    public void createMatch(String game, String date, int round, Match previousMatch) {
        Match match = new Match(game, date, round, previousMatch, null);
        saveMatch(match, previousMatch);
    }

    public int createMatch(String game, String date, int round) {
        QueryBuilder<Match, Integer> statementBuilder = matchDao.queryBuilder();
        try {
            statementBuilder.orderBy("PreviousMatch", false).queryForFirst();
            Match previousMatch = matchDao.queryForFirst(statementBuilder.prepare());
            Match match = new Match(game, date, round, previousMatch, null);
            previousMatch.setNextMatch(match);
            return saveMatch(match, previousMatch);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int createMatch(String game, String date) {
        QueryBuilder<Match, Integer> statementBuilder = matchDao.queryBuilder();
        try {
            statementBuilder.orderBy("PreviousMatch", false).queryForFirst();
            Match previousMatch = matchDao.queryForFirst(statementBuilder.prepare());
            Match match = new Match(game, date, previousMatch.getRound()+1, previousMatch, null);
            previousMatch.setNextMatch(match);
            return saveMatch(match, previousMatch);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Match getMatch(int id) {
        try {
            return matchDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<Integer, Integer> addPlayerMatch(int idPlayer, int idMatch, double gamePoints) {
        try {
            PlayerMatch playerMatch = new PlayerMatch(playerDao.queryForId(idPlayer), matchDao.queryForId(idMatch), gamePoints);
            return savePlayerMatch(playerMatch);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Pair<>(-1,-1);
        }
    }

    public int addPlayersMatch(ArrayList<PlayerMatch> players) {
        int errors = 0;
        Pair<Integer, Integer> error = new Pair<>(-1,-1);
        for (PlayerMatch player : players) {
            if (addPlayerMatch(player.getPlayer().getId(), player.getMatch().getId(), player.getPoints()) == error) {
                ++errors;
            }
        }
        return errors;
    }

    public void setInitialElo(ArrayList<PlayerMatch> players) {

        QueryBuilder<PlayerMatch, Integer> statementBuilder = playerMatchDao.queryBuilder();
        for (PlayerMatch player: players) {
            Match currentMatch = player.getMatch();
            Match previousMatch = currentMatch.getPreviousMatch();
            try {
                statementBuilder
                        .where()
                        .eq("IdPlayer", player.getPlayer().getId())
                        .and()
                        .eq("IdMatch", previousMatch.getId());

                double currentElo = playerMatchDao.queryForFirst(statementBuilder.prepare()).getTotalPoints();
                player.setTotalPoints(currentElo);
                playerMatchDao.update(player);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareMatch(Match match) {
        try {
            List<Player> players = playerDao.queryForAll();
            Match previousMatch = match.getPreviousMatch();
            for (Player player : players) {
                PlayerMatch playerMatch = new PlayerMatch(player, match, null);
                PlayerMatch previousGame = getMatchPlayer(previousMatch, player);
                setPlayerInitialPoints(playerMatch, previousGame);
                savePlayerMatch(playerMatch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerInitialPoints(PlayerMatch playerMatch, PlayerMatch previousGame) {
        if (previousGame != null) {
            playerMatch.setTotalPoints(previousGame.getTotalPoints());
        }
        else {
            playerMatch.setTotalPoints(1000);
        }
    }

    private PlayerMatch getMatchPlayer(Match match, Player player) throws SQLException {
        QueryBuilder<PlayerMatch, Integer> statementBuilder = playerMatchDao.queryBuilder();
        statementBuilder
                .where()
                .eq("IdPlayer", player.getId())
                .and()
                .eq("IdMatch", match.getId());
        return playerMatchDao.queryForFirst(statementBuilder.prepare());
    }

    public int setPoints(Match match, String playerName, Double points) {
        try {
            Player player = playerDao.queryForEq("Name", playerName).get(0);
            PlayerMatch playerMatch = getMatchPlayer(match, player);
            if (playerMatch != null) {
                playerMatch.setPoints(points);
                int result = updateEntity(playerMatchDao, playerMatch);
                if (result > 0) return 0;
                return -1;
            }
            else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<PlayerMatch> getPlayersPlaying(Match match) {
        try {
            QueryBuilder<PlayerMatch, Integer> statementBuilder = playerMatchDao.queryBuilder();
            statementBuilder
                    .where()
                    .eq("IdMatch", match.getId())
                    .and()
                    .isNotNull("Points");
            List<PlayerMatch> players = playerMatchDao.query(statementBuilder.prepare());
            for (int i=0; i < players.size(); ++i) {
                Player playerInfo = getPlayerInformation(players.get(i));
                Match matchInfo = getMatchInformation(players.get(i));
                players.get(i).setPlayer(playerInfo);
                players.get(i).setMatch(matchInfo);
            }
            players.sort(new MatchComparator());
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Player getPlayerInformation(PlayerMatch playerMatch) throws SQLException {
        Player playerInfo = playerMatch.getPlayer();
        int idPlayer = playerInfo.getId();
        playerInfo = playerDao.queryForId(idPlayer);
        return playerInfo;
    }

    private Match getMatchInformation(PlayerMatch playerMatch) throws SQLException {
        Match matchInfo = playerMatch.getMatch();
        int idMatch = matchInfo.getId();
        matchInfo = matchDao.queryForId(idMatch);
        return matchInfo;
    }

    public void updateScores(List<PlayerMatch> playersPlaying) {
        for (PlayerMatch player : playersPlaying) {
            int result = updateEntity(playerMatchDao, player);
            if (result == -1) System.out.println("Error a l'actualitzar jugador " + player.getId() + "-" + player.getPlayer().getName());
        }
    }
}
