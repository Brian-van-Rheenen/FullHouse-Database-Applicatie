package models;

import java.util.ArrayList;

public class Ronde {

    private Tournament tournament;

    private int nr;

    private ArrayList<Table> tables = new ArrayList<>();


    public Ronde(Tournament tournament, int nr) {
        this.tournament = tournament;
        this.nr = nr;
    }



}
