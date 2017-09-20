package no.nav.nare.core.evaluation;

public interface Evaluation {

    Resultat result();
    String reason();
    String ruleDescription();
    String ruleIdentification();

}
