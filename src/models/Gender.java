package models;

import models.exceptions.GenderFormatException;

import java.util.Arrays;
import java.util.Optional;

public enum Gender {
    MALE("M"),
    FEMALE("V"),
    OTHER("O");

    Gender(String databaseRepresentation) {
        this.databaseRepresentation = databaseRepresentation;
    }

    private String databaseRepresentation;

    public String getDatabaseRepresentation() {
        return databaseRepresentation;
    }

    //region parsing

    /**
     * Attempt to parse the string to a {@link Gender}
     * @param input the {@link Gender in database format}
     * @return {@link Optional} of {@link Gender}
     */
    public static Optional<Gender> tryParse(String input) {
        return Arrays.stream(Gender.values())
                .filter(enumMember -> enumMember.databaseRepresentation.equals(input))
                .findFirst();
    }

    /**
     * Attempt to parse the string to a {@link Gender}
     * @param input the {@link Gender in database format}
     * @return {@link Gender} on success
     * @throws GenderFormatException on failure
     */
    public static Gender parse(String input) {
        Optional<Gender> possibleGender = tryParse(input);
        if(possibleGender.isPresent())
            return possibleGender.get();
        else
            throw new GenderFormatException("the input did not match any known Gender");
    }

    //endregion parsing
}
