package com.erill;

import com.erill.Entities.Player;

import java.util.Comparator;

/**
 * Created by Roger on 13/02/2016.
 */
class RankingComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Player p1 = (Player) o1;
        Player p2 = (Player) o2;
        //return (int)p2.getElo() - (int)p1.getElo();
        return p2.getName().compareTo(p1.getName());
    }
}
