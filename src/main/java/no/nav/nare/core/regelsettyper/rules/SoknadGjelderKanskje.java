package no.nav.nare.core.regelsettyper.rules;

import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.core.specifications.AbstractSpecification;
import no.nav.nare.input.Soknad;
import no.nav.nare.input.Soknadstype;

public class SoknadGjelderKanskje extends AbstractSpecification<Soknad> {


    private final Soknadstype soknadstype;

    private SoknadGjelderKanskje(Soknadstype soknadstype){
        this.soknadstype = soknadstype;
    }

    public static SoknadGjelderKanskje søknadGjelderKanskje(Soknadstype soknadstype){
        return new SoknadGjelderKanskje(soknadstype);
    }


    @Override
    public Evaluation evaluate(Soknad soknad) {
        if (soknad.getSoknadstype().equals(soknadstype)){
            return kanskje("Søknad gjelder kanskje {0}", soknadstype);
        }else{
            return nei("Søknad gjelder ikke {0}", soknadstype);
        }
    }

}
