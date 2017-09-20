package no.nav.nare.core.evaluation.booleans;

import no.nav.nare.core.evaluation.AggregatedEvaluation;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.Result;

public class OrEvaluation extends AggregatedEvaluation {


    public OrEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.OR, id, ruleDescription, children);
    }

    @Override
    public Result result() {
        return first().result().or(second().result());
    }

    @Override
    public String reason() {
        if (result().equals(Result.YES)){
            return "Satisfies " + ruleOrIdentification();
        }else{
            return "Does not satisfy either " + first().ruleIdentification() + " nor " + second().ruleIdentification();
        }

    }


    private String ruleOrIdentification() {
        String firstID = first().result().equals(Result.YES) ? first().ruleIdentification() : "";
        String secondID = second().result().equals(Result.YES) ? second().ruleIdentification() : "";
        if (firstID.isEmpty()) return secondID;
        if (secondID.isEmpty()) return firstID;
        return firstID + " OG " + secondID;
    }


}
