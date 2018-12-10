package no.nav.nare.core.regelsettyper;


import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.input.Soknad;
import no.nav.nare.core.specifications.Specification;

/**
 * Short hands for Ruleset
 */
public class Ruleset {

    protected Specification specification;

    public static Ruleset modrekvote() {
        return new Modrekvote();
    }

    public static Ruleset kanskjekvote() {
        return new Kanskjekvote();
    }

    public Specification rule(String id, String beskrivelse, Specification specification) {
        return specification.medBeskrivelse(beskrivelse).medID(id);
    }

    public Specification rule(String id, Specification specification) {
        return specification.medID(id);
    }

    public Evaluation evaluer(Soknad soknad) {
        return specification.evaluate(soknad);
    }

}
