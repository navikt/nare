package no.nav.nare.core.specifications;

import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Operator;
import no.nav.nare.core.evaluation.booleans.OrEvaluation;

/**
 * OR specification, used to create a new specifcation that is the OR of two other specifications.
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;


    public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new OrEvaluation(identity(), description(),spec1.evaluate(t), spec2.evaluate(t));
    }


    @Override
    public String identity() {
        if (id.isEmpty()) {
            return "(" + spec1.identity() + " OR " + spec2.identity() + ")";
        } else {
            return id;
        }
    }



    @Override
    public String description() {
        if (beskrivelse.isEmpty()) {
            return "(" + spec1.description() + " OR " + spec2.description() + ")";
        } else {
            return beskrivelse;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.OR, identity(), description(), spec1.ruleDescription(), spec2.ruleDescription());
    }


}
