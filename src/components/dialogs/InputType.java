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
            return input.matches("^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)");
        }
    }, POST_CODE {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$");
        }
    }, CAPACITY {
        @Override
        boolean isGoodInput(String input) {
            return input.matches("\\b(0?[1-9]|1[0-9]|2[0-5])\\b");
        }
    }, TIME {
        @Override
        boolean isGoodInput(String input) { return input.matches("(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"); }
    };

    abstract boolean isGoodInput(String input);

}
