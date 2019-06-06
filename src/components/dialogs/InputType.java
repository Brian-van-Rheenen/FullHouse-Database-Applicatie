package components.dialogs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum InputType {

    NAME {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^[a-zA-Z]+$");
        }
    }, DATE {
        @Override
        boolean isGoodInput(String input) {return
           input.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])\\-(1[012]|0?[1-9])\\-((?:19|20)\\d{2})\\s*$");
        }
    }, NUMBER {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^[0-9]*$");
        }
    }, TELEPHONE_NR {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^(((0)[1-9]{2}[0-9][-]?[1-9][0-9]{5})|((\\\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6}))$");
        }
    }, EMAIL {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        }
    }, POST_CODE {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$");
        }
    }, SINGLE_CHAR {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("[a-zA-Z]");
        }
    };

    public static InputType[] getPersonalDataTypes() {

        InputType[] inputTypes = {NAME, NAME, NUMBER, POST_CODE, NAME, DATE, TELEPHONE_NR, EMAIL};
        return inputTypes;
    }

    /*"Naam", "Straat", "Huisnummer", "Postcode", "Woonplaats", "Geslacht",
            "Geboortedatum",
            "Telefoonnummer",
            "Emailadres"};
*/

    abstract boolean isGoodInput(String input);

}
