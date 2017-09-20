package no.nav.nare.core.evaluation;


public enum Resultat {

    JA(-1, "NEI"),
    NEI(1, "JA");

    private final int weight;
    private String inverse;


    Resultat(int weight, String inverse) {
        this.weight = weight;
        this.inverse = inverse;
    }



    public Resultat and(Resultat resultat) {
        return (this.weight > resultat.weight) ? this : resultat;
    }

    public Resultat or(Resultat resultat) {
        return (this.weight < resultat.weight) ? this : resultat;
    }

    public Resultat not(){
        return Resultat.valueOf(this.inverse);

    }
}
