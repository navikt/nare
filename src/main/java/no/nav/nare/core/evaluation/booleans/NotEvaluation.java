package no.nav.nare.core.evaluation.booleans;

import no.nav.nare.core.evaluation.AggregatedEvaluation;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.Result;

public class NotEvaluation extends AggregatedEvaluation {


    public NotEvaluation(String id, String ruleDescription, Evaluation child) {
        super(Operator.NOT, id, ruleDescription, child);
    }

    @Override
    public Result result() {
        return first().result().not();
    }

    @Override
    public String reason() {
        if (result().equals(Result.YES)){
            return "Satisfies the inverse of " + first().ruleIdentification();
        }else{
            return "Does not satisfy the inverse of " + first().ruleIdentification();
        }


    }

}
