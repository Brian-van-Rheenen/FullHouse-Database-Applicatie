package models;

import java.util.ArrayList;

public class Round {

    private Tournament tournament;

    private int nr;
    private ArrayList<Table> tables = new ArrayList<>();

    public Round(Tournament tournament, int nr) {
        this.tournament = tournament;
        this.nr = nr;
    }

    public int getNr() {
        return nr;
    }
}
