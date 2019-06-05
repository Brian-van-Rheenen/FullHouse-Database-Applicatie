package models;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Ronde {

    private Toernooi toernooi;

    private int nr;

    private ArrayList<Table> tables = new ArrayList<>();


    public Ronde(Toernooi toernooi, int nr) {
        this.toernooi = toernooi;
        this.nr = nr;
    }



}
