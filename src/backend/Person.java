package backend;

public class Person {

    private String naam;
    private String geboortedatum;
    private String geslacht;
    private String adres;
    private String telefoon;
    private String email;

    public Person(String naam, String geboortedatum, String geslacht, String adres, String telefoon, String email) {
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.email = email;
    }

}
