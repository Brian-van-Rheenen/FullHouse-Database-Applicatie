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

    public Object [] getTableFormatData(){
        System.out.println(player.getName());

        String betaaldString="Nee";
        if(betaald){
            betaaldString="Ja";
        }

        return new Object[]{player.getId(), player.getName(),betaaldString, player.getAddress().getZipCode()};
    }

    public boolean isBetaald() {
        return betaald;
    }
}
