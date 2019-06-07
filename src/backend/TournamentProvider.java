package backend;

import models.Toernooi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TournamentProvider {

    private DatabaseConnection databaseConnection;

    public TournamentProvider() {
        getDBconnection();
    }

    public ArrayList<Toernooi> getTournaments() throws SQLException {
        ArrayList<Toernooi> res = new ArrayList<>();

        ResultSet rs = databaseConnection.executeQueryAndGetData(DB_Statements.Q_ALLTOURNAMENTS);
        while(rs.next()){
            res.add(Toernooi.readTournament(rs));
        }


        return res;
    }


    private void getDBconnection() {
        try{
            databaseConnection = new DatabaseConnection();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

}
