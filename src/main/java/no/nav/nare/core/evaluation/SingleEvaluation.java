package no.nav.nare.core.evaluation;

import java.text.MessageFormat;

public class SingleEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Result result;
    private String reason;

    public SingleEvaluation(Result result, String ruleIdentification, String ruleDescription, String reason, Object... stringformatArguments) {
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.result = result;
        this.reason = MessageFormat.format(reason, stringformatArguments);
    }

    @Override
    public Result result() {
        return result;
    }

    @Override
    public String ruleDescription() {
        return ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentification;
    }

    @Override
    public String reason() {
        return reason;
    }


}
