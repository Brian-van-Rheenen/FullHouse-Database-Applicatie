package models.exceptions;

/**
 * Thrown to indicate that the application has attempted to convert
 * a string to one of {@link models.Gender}, but that the string does not
 * have the appropriate format.
 *
 * @author  Lucas Spits
 * @see     models.Gender#parse(String)
 */
public class GenderFormatException extends IllegalArgumentException {
    public GenderFormatException(String message) {
        super(message);
    }
}
