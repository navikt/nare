package no.nav.nare.core.regelsettyper.regler;


import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.input.Soknad;
import no.nav.nare.core.specifications.AbstractSpecification;


public class HarArbeidetSisteMnd extends AbstractSpecification<Soknad> {


    private final int month;

    private HarArbeidetSisteMnd(int mnd){
        this.month = mnd;
    }

    public static HarArbeidetSisteMnd harArbeidetSisteMnd(int mnd){
        return new HarArbeidetSisteMnd(mnd);
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        int mndArbeid = soknad.getHovedsøker().getMndArbeid();
        return (mndArbeid >= month)
                ? ja("Person har jobbet {0} måneder, som er tilstrekkelig", mndArbeid)
                : nei("Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid", mndArbeid, month);
    }

    @Override
    public String identifikator() {
        return "FK_VK_10.x";
    }

    @Override
    public String beskrivelse() {
        return "Har dokumentert sammenhengende arbeid siste " + month + " mnd";
    }

}
