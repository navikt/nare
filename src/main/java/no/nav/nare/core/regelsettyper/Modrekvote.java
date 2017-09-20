package no.nav.nare.core.regelsettyper;

import no.nav.nare.core.specifications.Specification;

import static no.nav.nare.input.Rolle.FAR;
import static no.nav.nare.input.Rolle.MOR;
import static no.nav.nare.input.Soknadstype.ADOPSJON;
import static no.nav.nare.input.Soknadstype.FODSEL;
import static no.nav.nare.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.nare.input.Uttaksplan.SAMMENHENGENDE;

import static no.nav.nare.core.regelsettyper.regler.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.nare.core.regelsettyper.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteAdopsjonl;
import static no.nav.nare.core.regelsettyper.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteFodsel;
import static no.nav.nare.core.regelsettyper.regler.SoknadGjelder.søknadGjelder;
import static no.nav.nare.core.specifications.NotSpecification.ikke;

public class Modrekvote extends Ruleset {


    public Modrekvote() {
        specification = getModreKvote();
    }

    public Specification getModreKvote() {

        Specification harBeggeForeldreRettTilForeldrepenger =
                regel("FK_VK_10.1", "Har begge foreldre rett til foreldrepenger?", harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));

        Specification gjelderSøknadFødsel =
                regel("FK_VK 10.2", "Gjelder søknad fødsel?", søknadGjelder(FODSEL));

        Specification gjelderSøknadAdopsjon =
                regel("FK_VK 10.3", "Gjelder søknad adopsjon?", søknadGjelder(ADOPSJON));

        Specification harUttaksplanEtterFodsel =
                regel("FK_VK_10.4", "Har mor uttaksplan sammenhengende eller tre år etter fødsel?", harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR)));

        Specification harUttaksplanEtterAdopsjon =
                regel("FK_VK_10.5", "Har mor uttaksplan sammenhengende eller tre år etter adopsjon?", harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR));

        Specification vilkårForFødsel =
                regel("FK_VK.10.A", harBeggeForeldreRettTilForeldrepenger.og(gjelderSøknadFødsel).og(harUttaksplanEtterFodsel));

        Specification vilkårForAdopsjon =
                regel("FK_VK.10.B", harBeggeForeldreRettTilForeldrepenger
                        .og(ikke(gjelderSøknadFødsel).medBeskrivelse("søknad gjelder ikke fødsel"))
                        .og(gjelderSøknadAdopsjon)
                        .og(harUttaksplanEtterAdopsjon));

        return regel("FK_VK.10", "Er vilkår for mødrekvote oppfylt for enten fødsel eller adopsjon?", vilkårForFødsel.eller(vilkårForAdopsjon));
    }


}
