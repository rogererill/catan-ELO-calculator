package com.erill.Views;

import com.erill.Entities.PlayerMatch;

import java.util.List;

/**
 * Created by Roger on 01/04/2016.
 */
public class MainView {

    public MainView() {

    }

    public void show(String message) {
        System.out.println(message);
    }

    public void printGameRanking(List<PlayerMatch> playersPlaying) {
        System.out.println("--- CLASSIFICACIÓ DE LA PARTIDA ---");
        for (int i=0; i < playersPlaying.size(); ++i) {
            int position = i+1;
            System.out.println(position + " - " + playersPlaying.get(i).getPlayer().getName() + "  " + playersPlaying.get(i).getPoints());
        }
    }
}
