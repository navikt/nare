package no.nav.nare.core.regelsettyper.rules;

import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.input.Person;
import no.nav.nare.input.Rolle;
import no.nav.nare.input.Soknad;
import no.nav.nare.core.specifications.AbstractSpecification;

import java.util.Optional;

public class HarRettTilForeldrePenger extends AbstractSpecification<Soknad> {


    private Rolle rolle;

    private HarRettTilForeldrePenger(Rolle rolle){
        this.rolle = rolle;
    }

    public static HarRettTilForeldrePenger harRettTilForeldrePenger(Rolle rolle){
        return new HarRettTilForeldrePenger(rolle);
    }


    @Override
    public Evaluation evaluate(Soknad soknad) {

        Optional<Person> søker = soknad.getSøker(rolle);
        if (!søker.isPresent()){
             return nei("Ingen søker med rolle {0} ", rolle);
        }else if(!søker.get().harRettTilForeldrepenger()){
            return nei("Søker med rolle {0} har ikke rett til foreldrepenger", rolle);
        }else{
            return ja("Søker med rolle {0} har rett til foreldrepenger", rolle);
        }
    }

    @Override
    public String identity() {
        return  "FK_VK_10.1";    }

    @Override
    public String description() {
        return "Har søker med rolle " + rolle +" rett til foreldrepenger?" ;
    }

}
