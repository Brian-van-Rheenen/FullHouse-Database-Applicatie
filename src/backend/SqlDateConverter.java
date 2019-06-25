package backend;

import java.sql.Date;
import java.text.*;

public class SqlDateConverter {

    public static String convertSqlDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static java.sql.Date convertStringToSqlDate(String dateString) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        java.util.Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
