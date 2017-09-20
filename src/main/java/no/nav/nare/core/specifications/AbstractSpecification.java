package no.nav.nare.core.specifications;


import com.google.gson.GsonBuilder;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.evaluation.Result;
import no.nav.nare.core.evaluation.SingleEvaluation;

public abstract class AbstractSpecification<T> implements Specification<T> {
    protected String beskrivelse="";
    protected String id="";

    protected AbstractSpecification(){
    }

    public Specification<T> and(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    public Specification<T> or(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }


    public Evaluation ja(String reason, Object... stringformatArguments){
        return new SingleEvaluation(Result.YES, identity(), description(), reason, stringformatArguments);
    }

    public Evaluation nei(String reason, Object... stringformatArguments){
        return  new SingleEvaluation(Result.NO, identity(), description(), reason, stringformatArguments);
    }


    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(identity(), description());
    }

    @Override
    public String identity() {
        if (id.isEmpty()) {
            return Integer.toString(this.hashCode());
        } else {
            return id;
        }
    }

    @Override
    public String description(){
        return beskrivelse;
    }

    @Override
    public Specification medBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
        return this;
    }

    @Override
    public Specification medID(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        System.out.println(this.description());
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
