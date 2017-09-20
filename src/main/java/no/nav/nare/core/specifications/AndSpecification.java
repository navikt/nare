package no.nav.nare.core.specifications;


import no.nav.nare.core.evaluation.*;
import no.nav.nare.core.evaluation.booleans.AndEvaluation;

/**
 * AND specification, used to create a new specifcation that is the AND of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;


    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    public Evaluation evaluate(final T t) {
        return new AndEvaluation(identity(), description(), spec1.evaluate(t), spec2.evaluate(t));
    }

    @Override
    public String identity() {
        if (id.isEmpty()) {
            return "(" + spec1.identity() + " AND " + spec2.identity() +")";
        } else {
            return id;
        }
    }

    @Override
    public String description() {
        if (beskrivelse.isEmpty()){
            return "(" + spec1.description() + " AND " +  spec2.description() + ")";
        }else{
            return beskrivelse;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.AND, identity(), description(), spec1.ruleDescription(),spec2.ruleDescription());
    }

}
