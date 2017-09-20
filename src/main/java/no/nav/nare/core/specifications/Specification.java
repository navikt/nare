package no.nav.nare.core.specifications;


import no.nav.nare.core.evaluation.Evaluation;


public interface Specification<T> {


    Evaluation evaluate(T t);

    Specification<T> og(Specification<T> specification);
    Specification<T> eller(Specification<T> specification);

    String identifikator();
    String beskrivelse();

    RuleDescription ruleDescription();

    Specification medBeskrivelse(String beskrivelse);
    Specification medID(String id);
}
