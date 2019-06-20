package models;

public class Participant {

    private Player player;
    private boolean hasPaid;

    public Participant(Player player, boolean hasPaid) {
        this.player = player;
        this.hasPaid = hasPaid;
    }

    public Player getPlayer() {
        return player;
    }

    public Object [] getTableFormatData(){
        System.out.println(player.getName());

        String hasPaidString="Nee";
        if(hasPaid){
            hasPaidString="Ja";
        }

        return new Object[]{player.getId(), player.getName(),hasPaidString, player.getAddress().getZipCode()};
    }

    public void setHasPaid(boolean input){
        hasPaid=input;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }
}
