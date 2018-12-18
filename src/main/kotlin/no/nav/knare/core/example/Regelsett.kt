package no.nav.knare.core.example

import no.nav.knare.core.evaluations.Evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.ja
import no.nav.knare.core.evaluations.Evaluering.Companion.nei
import no.nav.knare.core.specifications.Spesifikasjon
import no.nav.knare.core.specifications.Spesifikasjon.Companion.ikke
import no.nav.knare.core.specifications.Spesifikasjon.Companion.regel

class HarArbeidetSisteMnd(
        val month: Int
) : Spesifikasjon<Soknad>() {
    override var beskrivelse: () -> String = { "Har dokumentert sammenhengende arbeid siste $month mnd" }
    override var identitet: () -> String = { "FK_VK_10.x" }
    override fun evaluer(søknad: Soknad): Evaluering {
        return if (søknad.hovedsoker.mndArbeid >= month) Evaluering.ja("Person har jobbet ${søknad.hovedsoker.mndArbeid} måneder, som er tilstrekkelig", beskrivelse(), identitet())
        else Evaluering.nei("Person er oppfort med ${søknad.hovedsoker.mndArbeid} mnd arbeid. Dekker ikke kravet til ${month} mnd med arbeid", beskrivelse(), identitet())
    }
}

class HarRettTilForeldrePenger(
        val rolle: Rolle

) : Spesifikasjon<Soknad>() {
    override var beskrivelse: () -> String = { "Har søker med rolle $rolle rett til foreldrepenger?" }
    override var identitet: () -> String = { "FK_VK_10.1" }
    override fun evaluer(søknad: Soknad): Evaluering {
        val sokerIRolle = søknad.hentSøkerIRolle(rolle)
        return when {
            sokerIRolle == null -> nei("Ingen søker med rolle $rolle", beskrivelse(), identitet())
            !sokerIRolle.rettTilFp -> nei("Søker med rolle $rolle har ikke rett til foreldrepenger", beskrivelse(), identitet())
            else -> ja("Søker med rolle $rolle har rett til foreldrepenger", beskrivelse(), identitet())
        }
    }
}

class HarUttaksplanForModreKvote(
        val soknadstype: Soknadstype,
        val uttaksplan: Uttaksplan
) : Spesifikasjon<Soknad>() {
    override var beskrivelse: () -> String = { "" }
    override var identitet: () -> String = { "FK_VK 10.4/FK_VK 10.5/FK_VK 10.6" }
    override fun evaluer(søknad: Soknad): Evaluering {
        val mor = søknad.hentSøkerIRolle(Rolle.MOR)
        return when {
            mor == null -> nei("Ingen søker med rolle ${Rolle.MOR}", beskrivelse(), identitet())
            mor.uttaksplan == null -> nei("Det foreligger ingen uttaksplan for ${Rolle.MOR}", beskrivelse(), identitet())
            uttaksplan == mor.uttaksplan -> ja("Mødrekvote tas ${uttaksplan.description}", beskrivelse(), identitet())
            else -> nei("Mødrekvote tas ikke ${uttaksplan.description} $soknadstype", beskrivelse(), identitet())
        }
    }
}

class SoknadGjelder(
        val soknadstype: Soknadstype
) : Spesifikasjon<Soknad>() {
    override var beskrivelse: () -> String = { "Soknad gjelder $soknadstype" }
    override var identitet: () -> String = { "SoknadGjelder" }
    override fun evaluer(søknad: Soknad): Evaluering {
        return when (soknadstype) {
            søknad.soknadstype -> ja("Søknad gjelder $soknadstype", beskrivelse(), identitet())
            else -> nei("Søknad gjelder ikke $soknadstype", beskrivelse(), identitet())
        }
    }
}

class Regelsett {

    private val harBeggeForeldreRettTilForeldrePenger = regel(
            spec = HarRettTilForeldrePenger(Rolle.MOR) og HarRettTilForeldrePenger(Rolle.FAR),
            identitet = "FK_VK_10.1",
            beskrivelse = "Har begge foreldre rett til foreldrepenger?")

    private val gjelderSoknadFødsel = regel(
            spec = SoknadGjelder(Soknadstype.FODSEL),
            identitet = "FK_VK 10.2",
            beskrivelse = "Gjelder søknad fødsel?")
    private val gjelderSoknadAdopsjon = regel(
            spec = SoknadGjelder(Soknadstype.ADOPSJON),
            identitet = "FK_VK 10.3",
            beskrivelse = "Gjelder søknad adopsjon?")
    private val harUttaksplanEtterFodsel = regel(
            spec = HarUttaksplanForModreKvote(
                    soknadstype = Soknadstype.FODSEL,
                    uttaksplan = Uttaksplan.SAMMENHENGENDE),
            identitet = "FK_VK_10.4",
            beskrivelse = "Har mor uttaksplan sammenhengende eller tre år etter fødsel?")
    private val harUttaksplanEtterAdopsjon = regel(
            spec = HarUttaksplanForModreKvote(
                    soknadstype = Soknadstype.ADOPSJON,
                    uttaksplan = Uttaksplan.INNEN_3_AAR),
            identitet = "FK_VK_10.5",
            beskrivelse = "Har mor uttaksplan sammenhengende eller tre år etter adopsjon?")
    private val vilkårForFødsel = regel(
            spec = harBeggeForeldreRettTilForeldrePenger og gjelderSoknadFødsel og harUttaksplanEtterFodsel,
            identitet = "FK_VK.10.A",
            beskrivelse = "Vilkår for foreldrepenger ved fødsel"
    )

    private val gjelderIkkeFødsel = regel(spec = ikke(gjelderSoknadFødsel), beskrivelse = "søknad gjelder ikke fødsel", identitet = "")
    private val vilkårForAdopsjon = regel(
            spec = harBeggeForeldreRettTilForeldrePenger og gjelderIkkeFødsel og gjelderSoknadAdopsjon og harUttaksplanEtterAdopsjon,
            identitet = "FK_VK.10.B",
            beskrivelse = "Vilkår for foreldrepenger ved adopsjon")

    val mødreKvote = regel(
            spec = vilkårForAdopsjon eller vilkårForFødsel,
            identitet = "FK_VK.10",
            beskrivelse = "Er vilkår for mødrekvote oppfylt for enten fødsel eller adopsjon?")
}