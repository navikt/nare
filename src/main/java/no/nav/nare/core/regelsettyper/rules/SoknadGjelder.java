package no.nav.nare.core.regelsettyper.rules;

import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.input.Soknad;
import no.nav.nare.input.Soknadstype;
import no.nav.nare.core.specifications.AbstractSpecification;

public class SoknadGjelder extends AbstractSpecification<Soknad> {


    private final Soknadstype soknadstype;

    private SoknadGjelder(Soknadstype soknadstype){
        this.soknadstype = soknadstype;
    }

    public static SoknadGjelder søknadGjelder(Soknadstype soknadstype){
        return new SoknadGjelder(soknadstype);
    }


    @Override
    public Evaluation evaluate(Soknad soknad) {
        if (soknad.getSoknadstype().equals(soknadstype)){
            return ja("Søknad gjelder {0}", soknadstype);
        }else{
            return nei("Søknad gjelder ikke {0}", soknadstype);
        }
    }

}
