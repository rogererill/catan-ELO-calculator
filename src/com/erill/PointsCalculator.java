package com.erill;

import com.erill.Entities.PlayerMatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 01/04/2016.
 */
public class PointsCalculator {

    private List<PlayerMatch> players;

    public PointsCalculator(List<PlayerMatch> players) {
        this.players = players;
    }

    public List<PlayerMatch> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerMatch> players) {
        this.players = players;
    }

    public void calculatePoints() {
        int n = players.size();
        float K = Constants.K_NUM / (float)(n - 1);

        List<Double> backupPoints = savePlayerPoints();
        normalizePoints();

        for (int i = 0; i < n; i++)
        {
            double currentPoints = players.get(i).getPoints();
            Double curELO = players.get(i).getTotalPoints();
            int eloChange = 0;
            for (int j = 0; j < n; j++)
            {
                if (i != j)
                {
                    double opponentPoints = players.get(j).getPoints();
                    Double opponentELO   = players.get(j).getTotalPoints();

                    double S = Utils.getS(currentPoints, opponentPoints);

                    //work out EA
                    double eloDifference = opponentELO - curELO;
                    double eloDivision = eloDifference / Constants.ELO_DIVISOR;
                    double power = Math.pow(10.0f, eloDivision);
                    double EA = 1 / (1.0f + power);
                    int multiplication = (int)Math.round(K * (S - EA));
                    //calculate ELO change vs this one opponent, add it to our change bucket
                    //I currently round at this point, this keeps rounding changes symmetrical between EA and EB, but changes K more than it should
                    eloChange += multiplication;
                }
            }
            //add accumulated change to initial ELO for final ELO
            //players.get(i).setEloPost(players.get(i).getEloPre() + eloChange);
            players.get(i).setTotalPoints(curELO + eloChange);
        }

        restoreOriginalPoints(backupPoints);
    }

    private List<Double> savePlayerPoints() {
        List<Double> result = new ArrayList<>();
        for (int i=0; i < players.size(); ++i) {
            result.add(players.get(i).getPoints());
        }
        return result;
    }

    private void restoreOriginalPoints(List<Double> backupPoints) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPoints(backupPoints.get(i));
        }
    }

    public void normalizePoints() {
        double max = 0;
        double ratio;
        int n = players.size();
        for (int i = 0; i < n; i++) {
            if (players.get(i).getPoints() > max) {
                max = players.get(i).getPoints();
            }
        }
        ratio = 10/max;
        for (int i = 0; i < n; i++) {
            players.get(i).setPoints(players.get(i).getPoints()*ratio);
        }
    }


}
