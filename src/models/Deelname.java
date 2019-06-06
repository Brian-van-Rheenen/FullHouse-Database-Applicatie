package models;

public class Deelname {

    private Player player;
    private boolean betaald;

    public Deelname(Player player, boolean betaald) {
        this.player = player;
        this.betaald = betaald;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isBetaald() {
        return betaald;
    }
}
