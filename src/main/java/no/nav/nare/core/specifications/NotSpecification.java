package no.nav.nare.core.specifications;


import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.booleans.NotEvaluation;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    public static NotSpecification ikke(final Specification spec1) {
        return new NotSpecification(spec1);
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new NotEvaluation(identity(), description(), spec1.evaluate(t));
    }

    @Override
    public String identity() {
        if (id.isEmpty()) {
            return "(NOT " + spec1.identity() + ")";
        } else {
            return id;
        }
    }



    @Override
    public String description() {
        if (beskrivelse.isEmpty()){
            return "(NOT " + spec1.description() + ")";
        }else{
            return beskrivelse;
        }

    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.NOT, identity(), description(), spec1.ruleDescription());
    }
}
