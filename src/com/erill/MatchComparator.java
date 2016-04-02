package com.erill;

import com.erill.Entities.PlayerMatch;

import java.util.Comparator;

/**
 * Created by Roger on 13/02/2016.
 */
class MatchComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        PlayerMatch p1 = (PlayerMatch) o1;
        PlayerMatch p2 = (PlayerMatch) o2;
        if (p2.getPoints() > p1.getPoints()) return 1;
        else if (p2.getPoints() < p1.getPoints()) return -1;
        return 0;
    }
}
