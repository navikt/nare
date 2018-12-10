package no.nav.nare.core.regelsettyper;

import no.nav.nare.core.specifications.Specification;

import static no.nav.nare.core.regelsettyper.rules.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.nare.core.regelsettyper.rules.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteAdopsjonl;
import static no.nav.nare.core.regelsettyper.rules.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteFodsel;
import static no.nav.nare.core.regelsettyper.rules.SoknadGjelder.søknadGjelder;
import static no.nav.nare.core.regelsettyper.rules.SoknadGjelderKanskje.søknadGjelderKanskje;
import static no.nav.nare.core.specifications.NotSpecification.ikke;
import static no.nav.nare.input.Rolle.FAR;
import static no.nav.nare.input.Rolle.MOR;
import static no.nav.nare.input.Soknadstype.ADOPSJON;
import static no.nav.nare.input.Soknadstype.FODSEL;
import static no.nav.nare.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.nare.input.Uttaksplan.SAMMENHENGENDE;

/**
 * for å demonstrere hva som skjer når noen regler ikke gir utvetydige svar
 */
public class Kanskjekvote extends Ruleset {


    public Kanskjekvote() {
        specification = getKanskjeRegler();
    }

    public Specification getKanskjeRegler() {

        Specification harBeggeForeldreRettTilForeldrepenger =
                rule("FK_VK_10.1", "Har begge foreldre rett til foreldrepenger?", harRettTilForeldrePenger(MOR).and(harRettTilForeldrePenger(FAR)));

        Specification gjelderSøknadFødsel =
                rule("FK_VK 10.2", "Gjelder søknad fødsel?", søknadGjelder(FODSEL));

        Specification gjelderSøknadNoeGreierViIkkeVetHvaEr =
                rule("FK_VK 10.3", "Gjelder søknad adopsjon?", søknadGjelderKanskje(ADOPSJON));

        Specification harUttaksplanEtterFodsel =
                rule("FK_VK_10.4", "Har mor uttaksplan sammenhengende or tre år etter fødsel?", harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).or(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR)));

        Specification harUttaksplanEtterAdopsjon =
                rule("FK_VK_10.5", "Har mor uttaksplan sammenhengende or tre år etter adopsjon?", harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR));

        Specification vilkårForFødsel =
                rule("FK_VK.10.A", harBeggeForeldreRettTilForeldrepenger.and(gjelderSøknadFødsel).and(harUttaksplanEtterFodsel));

        Specification vilkårForAdopsjon =
                rule("FK_VK.10.B", harBeggeForeldreRettTilForeldrepenger
                        .and(ikke(gjelderSøknadFødsel).medBeskrivelse("søknad gjelder ikke fødsel"))
                        .and(gjelderSøknadNoeGreierViIkkeVetHvaEr)
                        .and(harUttaksplanEtterAdopsjon));

        return rule("FK_VK.10", "Er vilkår for mødrekvote oppfylt for enten fødsel or adopsjon?", vilkårForFødsel.or(vilkårForAdopsjon));
    }



}
