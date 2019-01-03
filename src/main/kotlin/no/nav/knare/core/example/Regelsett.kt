package no.nav.knare.core.example

import no.nav.knare.core.evaluations.*
import no.nav.knare.core.evaluations.Evaluering.Companion.ja
import no.nav.knare.core.evaluations.Evaluering.Companion.nei
import no.nav.knare.core.specifications.*

fun harArbeidetSisteXMnd(mnd: Int, søknad: Soknad) = Spesifikasjon(
        beskrivelse = "Har dokumentert sammenhengende arbeid siste $mnd antall måneder",
        identitet = "FK_VK_10.x",
        implementasjon = {
            if (søknad.hovedsoker.mndArbeid >= mnd)
                Evaluering.ja("Person har jobbet ${søknad.hovedsoker.mndArbeid} måneder, som er tilstrekkelig")
            else
                Evaluering.nei("Person er oppfort med ${søknad.hovedsoker.mndArbeid} mnd arbeid. Dekker ikke kravet til ${mnd} mnd med arbeid")
        }
)

fun søkerHarPåkrevdRolle(rolle: Rolle, søknad: Soknad) = Spesifikasjon(
        beskrivelse = "Har søker med rolle $rolle rett til foreldrepenger?",
        identitet = "FK_VK_10.1",
        implementasjon = {
            søknad.hentSøkerIRolle(rolle)?.let { person ->
                if (person.rettTilFp)
                    ja("Søker med rolle $rolle har rett til foreldrepenger")
                else
                    nei("Søker med rolle $rolle har ikke rett til foreldrepenger")
            } ?: nei("Søker har ikke rolle $rolle")
        }
)

fun harUttaksplanForModreKvote(soknadstype: Soknadstype, uttaksplan: Uttaksplan, søknad: Soknad) = Spesifikasjon(
        beskrivelse = "jau hau",
        identitet = "FK_VK 10.4/FK_VK 10.5/FK_VK 10.6",
        implementasjon = {
            søknad.hentSøkerIRolle(Rolle.MOR)?.let { mor ->
                mor.uttaksplan?.let { uttaksplan ->
                    if (uttaksplan == mor.uttaksplan)
                        ja("Mødrekvote tas ${uttaksplan.description}")
                    else
                        nei("Mødrekvote tas ikke ${uttaksplan.description} $soknadstype")
                } ?: nei("Det foreligger ingen uttaksplan for mor")
            } ?: nei("Søker er ikke mor")
        }
)

fun soknadGjelder(søknadstype: Soknadstype, søknad: Soknad) = Spesifikasjon(
        beskrivelse = "Soknad gjelder $søknadstype?",
        identitet = "FK_VK 10.2",
        implementasjon = {
            if (søknad.soknadstype == søknadstype)
                ja("Søknad gjelder $søknadstype")
            else
                nei("Søknad gjelder ikke $søknadstype")
        }
)

class Regelsett {

    val søknad = Soknad(medsoker = Person(name = "Far", rolle = Rolle.FAR, address = "Oslo", inntekt = 500000, mndArbeid = 80, rettTilFp = true, yrke = "X", uttaksplan = null),
            hovedsoker = Person(name = "Mor", rolle = Rolle.MOR, address = "Oslo", inntekt = 600000, mndArbeid = 24, rettTilFp = true, yrke = "Y", uttaksplan = Uttaksplan.SAMMENHENGENDE),
            soknadstype = Soknadstype.FODSEL)

    private val harBeggeForeldreRettTilForeldrePenger =
            søkerHarPåkrevdRolle(Rolle.MOR, søknad) og søkerHarPåkrevdRolle(Rolle.FAR, søknad)

    private val gjelderSoknadFødsel = soknadGjelder(Soknadstype.FODSEL, søknad)

    private val gjelderSoknadAdopsjon = soknadGjelder(Soknadstype.ADOPSJON, søknad)

    private val harUttaksplanEtterFodsel =
            harUttaksplanForModreKvote(Soknadstype.FODSEL, Uttaksplan.SAMMENHENGENDE, søknad) eller
            harUttaksplanForModreKvote(Soknadstype.FODSEL, Uttaksplan.INNEN_3_AAR, søknad)

    private val harUttaksplanEtterAdopsjon = harUttaksplanForModreKvote(Soknadstype.ADOPSJON, Uttaksplan.INNEN_3_AAR, søknad)

    private val vilkårForFødsel = harBeggeForeldreRettTilForeldrePenger og gjelderSoknadFødsel og harUttaksplanEtterFodsel

    private val vilkårForAdopsjon = harBeggeForeldreRettTilForeldrePenger og
            gjelderSoknadFødsel.ikke() og
            gjelderSoknadAdopsjon og
            harUttaksplanEtterAdopsjon

    val mødreKvote = vilkårForFødsel eller vilkårForAdopsjon
}