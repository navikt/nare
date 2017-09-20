package no.nav.nare.core.evaluation.booleans;

import no.nav.nare.core.evaluation.AggregatedEvaluation;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.Result;

public class AndEvaluation extends AggregatedEvaluation {


    public AndEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.AND, id, ruleDescription, children);
    }

    @Override
    public Result result() {
        return first().result().and(second().result());
    }

    @Override
    public String reason() {
        if (result().equals(Result.YES)){
            return "Satisfies both " + first().ruleIdentification() + " and " + second().ruleIdentification();
        }else{
            return "Does not satisfy both " + first().ruleIdentification() + " and " + second().ruleIdentification();
        }



    }


}
