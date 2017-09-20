package no.nav.nare.core.specifications;


import no.nav.nare.core.evaluation.Evaluation;


public interface Specification<T> {


    Evaluation evaluate(T t);

    Specification<T> and(Specification<T> specification);
    Specification<T> or(Specification<T> specification);

    String identity();
    String description();

    RuleDescription ruleDescription();

    Specification medBeskrivelse(String beskrivelse);
    Specification medID(String id);
}
