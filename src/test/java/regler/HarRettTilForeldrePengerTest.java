package regler;


import no.nav.nare.core.evaluation.Result;
import no.nav.nare.core.regelsettyper.rules.HarRettTilForeldrePenger;
import no.nav.nare.input.Person;
import no.nav.nare.input.Rolle;
import no.nav.nare.input.Soknad;
import org.junit.Test;

import static no.nav.nare.core.regelsettyper.rules.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.nare.input.Rolle.MOR;
import static org.assertj.core.api.Assertions.assertThat;

public class HarRettTilForeldrePengerTest {

    private HarRettTilForeldrePenger unit;


    @Test
    public void mor_har_rett_til_foreldrepenger_for_mor_should_yield_yes() throws Exception {
        unit = harRettTilForeldrePenger(MOR);
        Soknad harrettTilForeldrepenger = createInput(Rolle.MOR, true);
        assertThat(unit.identity()).isEqualTo(unit.evaluate(harrettTilForeldrepenger).ruleIdentification());
        assertThat(unit.evaluate(harrettTilForeldrepenger).result()).isEqualTo(Result.YES);
    }

    @Test
    public void mor_har_ikke_rett_til_foreldrepenger_for_mor_should_yield_no() throws Exception {
        unit = harRettTilForeldrePenger(MOR);
        Soknad harrettTilForeldrepenger = createInput(Rolle.MOR, false);
        assertThat(unit.evaluate(harrettTilForeldrepenger).result()).isEqualTo(Result.NO);
    }

    @Test
    public void far_har_rett_til_foreldrepenger_for_far_should_yield_yes() throws Exception {
        unit = harRettTilForeldrePenger(Rolle.FAR);
        Soknad harrettTilForeldrepenger = createInput(Rolle.FAR, true);
        assertThat(unit.identity()).isEqualTo(unit.evaluate(harrettTilForeldrepenger).ruleIdentification());
        assertThat(unit.evaluate(harrettTilForeldrepenger).result()).isEqualTo(Result.YES);
    }

    @Test
    public void far_har_ikke_rett_til_foreldrepenger_for_far_should_yield_no() throws Exception {
        unit = harRettTilForeldrePenger(Rolle.FAR);
        Soknad harrettTilForeldrepenger = createInput(Rolle.FAR, false);
        assertThat(unit.evaluate(harrettTilForeldrepenger).result()).isEqualTo(Result.NO);
    }

    private Soknad createInput(Rolle rolle, boolean rettTilForeldrepenger){
        return Soknad.fodselSøknad(new Person(null,rolle, null, 0, 12, null, rettTilForeldrepenger));
    }

}