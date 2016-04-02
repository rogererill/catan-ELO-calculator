package com.erill;

import com.erill.Entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 01/01/2016.
 */
public class Utils {
    public static double getS (double pointsA, double pointsB) {
        double diff = pointsA - pointsB;
        double valorS = Math.pow((diff/Constants.S_DIVISOR),3) + 0.5;
        //System.out.println("Valor S entre " + pointsA + " vs " + pointsB + " = " + valorS);
        return  Math.pow((diff/Constants.S_DIVISOR),3) + 0.5;
    }

    public static void printRanking(ArrayList<Player> players) {
        List<Player> copy = new ArrayList<>(players);
        //Collections.copy(copy, players);
        copy.sort(new RankingComparator());
        for (int i=0; i < copy.size(); ++i) {
            int position = i+1;
            System.out.println(position + "- " + copy.get(i).toString());
        }
        System.out.println();
        System.out.println("===============");
        System.out.println();
        System.out.println();
    }
}
