package backend;

public class DB_Statements {

    public static final String Q_ALLPLAYERS =
            "SELECT speler_id, naam, geslacht, gebdatum, a.straatnaam, a.huisnummer, a.postcode, a.woonplaats, telefoon, email, rating\n" +
                    "FROM speler\n" +
                    "INNER JOIN adres a on speler.adres_id = a.adres_id\n" +
                    "ORDER BY speler.speler_id;";

    public static final String Q_ADDPLAYER = "START TRANSACTION;\n" +
            "INSERT INTO adres (woonplaats, straatnaam, huisnummer, postcode)\n" +
            "SELECT ?, ?, ?, ? FROM adres WHERE NOT EXISTS(SELECT * FROM adres WHERE woonplaats = 'stad' AND straatnaam = 'straat' AND huisnummer = 1 AND postcode = '1234AB')\n" +
            "LIMIT 1;\n" +
            "INSERT INTO speler (adres_id, naam, gebdatum, geslacht, telefoon, email)\n" +
            "VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?);\n" +
            "COMMIT;";

    public static final String Q_DELETEPLAYER = "START TRANSACTION; UPDATE speler SET adres_id = 0, naam = 'VERWIJDERD', gebdatum = '1970-01-01', geslacht = 'O', telefoon = 'VERWIJDERD', email = 'VERWIJDERD', rating = 0 WHERE speler_id = ?; DELETE FROM adres WHERE adres_id NOT IN (SELECT adres_id FROM speler) AND adres_id != 0; COMMIT;";

    public static final String Q_ALLTOURNAMENTS = "SELECT l.stad                                           AS Locatie,\n" +
            "       DATE_FORMAT(begintijd, '%d-%m-%Y')               AS Begindatum,\n" +
            "       TIME_FORMAT(begintijd, '%H:%i')                  AS Begintijd,\n" +
            "       DATE_FORMAT(eindtijd, '%d-%m-%Y')                AS Einddatum,\n" +
            "       TIME_FORMAT(eindtijd, '%H:%i')                   AS Eindtijd,\n" +
            "       DATE_FORMAT(uiterste_inschrijfdatum, '%d-%m-%Y') AS 'Uiterste inschrijfdatum',\n" +
            "       thema                                            AS Thema,\n" +
            "       inschrijfgeld                                    AS Kosten,\n" +
            "       toegang_beperking                                AS Toegangsbeperking\n" +
            "FROM event\n" +
            "         INNER JOIN locatie l on event.locatie = l.idLocatie\n" +
            "         INNER JOIN toernooi t ON idEvent = t.idToernooi\n" +
            "ORDER BY t.idToernooi;";



}
