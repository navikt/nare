package no.nav.nare.core.evaluation;

public interface Evaluation {

    Result result();
    String reason();
    String ruleDescription();
    String ruleIdentification();

}
