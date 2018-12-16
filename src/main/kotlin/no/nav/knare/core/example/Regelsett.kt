package no.nav.knare.core.example

import no.nav.knare.core.evaluations.Evaluation
import no.nav.knare.core.evaluations.Evaluation.Companion.ja
import no.nav.knare.core.evaluations.Evaluation.Companion.nei
import no.nav.knare.core.specifications.Specification

class HarArbeidetSisteMnd(
        val month: Int,
        override val description: String = "Har dokumentert sammenhengende arbeid siste $month mnd",
        override val identity: String = "FK_VK_10.x"
) : Specification<Soknad>({
    if (it.hovedsoker.mndArbeid >= month) Evaluation.ja("Person har jobbet ${it.hovedsoker.mndArbeid} måneder, som er tilstrekkelig", description, identity)
    else Evaluation.nei("Person er oppfort med ${it.hovedsoker.mndArbeid} mnd arbeid. Dekker ikke kravet til ${month} mnd med arbeid", description, identity)
})

class HarRettTilForeldrePenger(
        rolle: Rolle,
        override val description: String = "Har søker med rolle $rolle rett til foreldrepenger?",
        override val identity: String = "FK_VK_10.1"
) : Specification<Soknad>({
    val sokerIRolle = it.hentSøkerIRolle(rolle)
    when {
        sokerIRolle == null -> nei("Ingen søker med rolle $rolle", description, identity)
        !sokerIRolle.rettTilFp -> nei("Søker med rolle $rolle har ikke rett til foreldrepenger", description, identity)
        else -> ja("Søker med rolle $rolle har rett til foreldrepenger", description, identity)
    }
})

class HarUttaksplanForModreKvote(
        soknadstype: Soknadstype,
        uttaksplan: Uttaksplan,
        override val description: String = "",
        override val identity: String = "FK_VK 10.4/FK_VK 10.5/FK_VK 10.6"
) : Specification<Soknad>({
    val mor = it.hentSøkerIRolle(Rolle.MOR)
    when {
        mor == null -> nei("Ingen søker med rolle ${Rolle.MOR}", description, identity)
        mor.uttaksplan == null -> nei("Det foreligger ingen uttaksplan for ${Rolle.MOR}", description, identity)
        uttaksplan == mor.uttaksplan -> ja("Mødrekvote tas ${uttaksplan.description}", description, identity)
        else -> nei("Mødrekvote tas ikke ${uttaksplan.description} $soknadstype", description, identity)
    }
})

class SoknadGjelder(
        soknadstype: Soknadstype,
        override val description: String = "Soknad gjelder $soknadstype",
        override val identity: String = "SoknadGjelder"
) : Specification<Soknad>({
    when (soknadstype) {
        it.soknadstype -> ja("Søknad gjelder $soknadstype", description, identity)
        else -> nei("Søknad gjelder ikke $soknadstype", description, identity)
    }
})

class Regelsett {

    private val harBeggeForeldreRettTilForeldrePenger = HarRettTilForeldrePenger(Rolle.MOR).and(
            right = HarRettTilForeldrePenger(Rolle.FAR),
            identity = "FK_VK_10.1",
            description = "Har begge foreldre rett til foreldrepenger?"
    )

    private val gjelderSoknadFødsel = SoknadGjelder(
            soknadstype = Soknadstype.FODSEL,
            identity = "FK_VK 10.2",
            description = "Gjelder søknad fødsel?"
    )
    private val gjelderSoknadAdopsjon = SoknadGjelder(
            soknadstype = Soknadstype.ADOPSJON,
            identity = "FK_VK 10.3",
            description = "Gjelder søknad adopsjon?"
    )
    private val harUttaksplanEtterFodsel = HarUttaksplanForModreKvote(
            soknadstype = Soknadstype.FODSEL,
            uttaksplan = Uttaksplan.SAMMENHENGENDE,
            identity = "FK_VK_10.4",
            description = "Har mor uttaksplan sammenhengende or tre år etter fødsel?"
    )
    private val harUttaksplanEtterAdopsjon = HarUttaksplanForModreKvote(
            soknadstype = Soknadstype.ADOPSJON,
            uttaksplan = Uttaksplan.INNEN_3_AAR,
            identity = "FK_VK_10.5",
            description = "Har mor uttaksplan sammenhengende or tre år etter adopsjon?"
    )
    private val vilkårForFødsel = harBeggeForeldreRettTilForeldrePenger.and(gjelderSoknadFødsel).and(harUttaksplanEtterFodsel, identity = "FK_VK.10.A")
    private val vilkårForAdopsjon = harBeggeForeldreRettTilForeldrePenger
            .and(gjelderSoknadFødsel.not(description = "søknad gjelder ikke fødsel"))
            .and(gjelderSoknadAdopsjon)
            .and(harUttaksplanEtterAdopsjon, identity = "FK_VK.10.B")

    val mødreKvote = vilkårForAdopsjon.or(vilkårForFødsel, identity = "FK_VK.10", description = "Er vilkår for mødrekvote oppfylt for enten fødsel or adopsjon?")
}