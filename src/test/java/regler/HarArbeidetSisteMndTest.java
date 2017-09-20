package regler;


import no.nav.nare.core.evaluation.Resultat;
import no.nav.nare.core.regelsettyper.regler.HarArbeidetSisteMnd;
import no.nav.nare.input.Person;
import no.nav.nare.input.Soknad;
import org.junit.Before;
import org.junit.Test;

import static no.nav.nare.core.regelsettyper.regler.HarArbeidetSisteMnd.harArbeidetSisteMnd;
import static org.assertj.core.api.Assertions.assertThat;

public class HarArbeidetSisteMndTest {

    private static final int THRESHOLD = 12;
    private HarArbeidetSisteMnd unit;

    @Before
    public void init(){
        unit = harArbeidetSisteMnd(THRESHOLD);
    }

    @Test
    public void threshold_months_should_yield_yes() throws Exception {
        Soknad hararbeidet12mnd = createInput(THRESHOLD);
        assertThat(unit.identifikator()).isEqualTo(unit.evaluate(hararbeidet12mnd).ruleIdentification());
        assertThat(unit.evaluate(hararbeidet12mnd).result()).isEqualTo(Resultat.JA);
    }


    @Test
    public void less_than_threshold_months_should_yield_no() throws Exception {
        assertThat(unit.evaluate(createInput(THRESHOLD-1)).result()).isEqualTo(Resultat.NEI);
    }

    @Test
    public void more_than_threshold_months_should_yield_yes() throws Exception {
        assertThat(unit.evaluate(createInput(THRESHOLD+1)).result()).isEqualTo(Resultat.JA);
    }

    private Soknad createInput(int mmdArbeid){
        return Soknad.fodselSøknad(new Person(null, null, null, 0, mmdArbeid, null, false));
    }
}