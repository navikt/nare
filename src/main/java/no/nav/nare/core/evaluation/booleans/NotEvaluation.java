package no.nav.nare.core.evaluation.booleans;

import no.nav.nare.core.evaluation.AggregatedEvaluation;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.Resultat;

public class NotEvaluation extends AggregatedEvaluation {


    public NotEvaluation(String id, String ruleDescription, Evaluation child) {
        super(Operator.NOT, id, ruleDescription, child);
    }

    @Override
    public Resultat result() {
        return first().result().not();
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)){
            return "Tilfredstiller det motsatte av " + first().ruleIdentification();
        }else{
            return "Tilfredstiller ikke det motsatte av " + first().ruleIdentification();
        }


    }

}
