package components.dialogs;

public enum InputType {

    NAME {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
        }
    }, CITY {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]+$");
        }
    }, DATE {
        @Override
        boolean isGoodInput(String input) {return
           input.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])\\-(1[012]|0?[1-9])\\-((?:19|20)\\d{2})\\s*$");
        }
    }, NUMBER {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^\\d+$");
        }
    }, TELEPHONE_NR {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^((\\+|00(\\s|\\s?\\-\\s?)?)31(\\s|\\s?\\-\\s?)?(\\(0\\)[\\-\\s]?)?|0)[1-9]((\\s|\\s?\\-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]$");
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
    }, TIME {
        @Override
        boolean isGoodInput(String input) { return input.matches("(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"); }
    };

    abstract boolean isGoodInput(String input);

}
