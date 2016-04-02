package com.erill;

/**
 * Created by Roger on 06/02/2016.
 */
public class Constants {

    /**
     * Té a veure amb els punts que es reparteixen per partida. A major valor, més punts es repartiran
     */
    public static final int K_NUM = 100;

    /**
     * Com afecta el teu ELO actual alhora de guanyar o perdre punts. Un valor més baix promou que els
     * que van més avall guanyin més punts quan guanyen i que els que van amunt perdin més punts quan perden.
     * Un valor molt alt fa que la teva posició no influeixi alhora de guanyar o perdre punts
     */
    public static final double ELO_DIVISOR = 1000;



    /**
     * Determina com la diferència de puntuació entre dues persones afecta
     * El paràmetre S, que ha d'anar entre 0 i 1. Una diferència de 0 sempre donarà
     * Un valor de S de 0.5. Amb aquest divisor determinem quants punts de diferència
     * otorguen a S un valor de 0 i 1.
     *
     * Un valor més baix fa que les diferències de punts a la partida marquin més
     * els punts de ELO obtinguts.
     *
     * També afecta al nombre de punts que es reparteixen
     *
     * Mínim hauria de ser 5
     *
     */
    public static final double S_DIVISOR = 5;

    public final static String DATABASE_URL = "jdbc:sqlite:database/elocalculator.sqlite";

}
