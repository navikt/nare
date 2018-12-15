package no.nav.knare.core.example

import no.nav.knare.core.evaluations.Evaluation
import no.nav.knare.core.evaluations.Evaluation.Companion.ja
import no.nav.knare.core.evaluations.Evaluation.Companion.nei
import no.nav.knare.core.specifications.AndSpecification
import no.nav.knare.core.specifications.Specification

class HarArbeidetSisteMnd(val month: Int) : Specification<Soknad>({
    val des = "Har dokumentert sammenhengende arbeid siste $month mnd"
    val id = "HarArbeidetSisteMnd"
    if (it.hovedsoker.mndArbeid >= month) Evaluation.ja("Person har jobbet ${it.hovedsoker.mndArbeid} måneder, som er tilstrekkelig", des, id)
    else Evaluation.nei("Person er oppfort med ${it.hovedsoker.mndArbeid} mnd arbeid. Dekker ikke kravet til ${month} mnd med arbeid", des, id)
})

class HarRettTilForeldrePenger(rolle: Rolle) : Specification<Soknad>({
    val des = "Har rett til foreldrepenger"
    val id = "HarRettTilForeldrePenger"
    val sokerIRolle = it.hentSøkerIRolle(rolle)
    when {
        sokerIRolle == null -> nei("Ingen søker med rolle $rolle", des, id)
        !sokerIRolle.rettTilFp -> nei("Søker med rolle $rolle har ikke rett til foreldrepenger", des, id)
        else -> ja("Søker med rolle $rolle har rett til foreldrepenger", des, id)
    }
})

class HarUttaksplanForModreKvote(soknadstype: Soknadstype, uttaksplan: Uttaksplan) : Specification<Soknad>({
    val des = "Har uttaksplan $uttaksplan for modrekvote"
    val id = "HarUttaksplanForModreKvote"

    val mor = it.hentSøkerIRolle(Rolle.MOR)
    when {
        mor == null -> nei("Ingen søker med rolle ${Rolle.MOR}", des, id)
        mor.uttaksplan == null -> nei("Det foreligger ingen uttaksplan for ${Rolle.MOR}", des, id)
        uttaksplan == mor.uttaksplan -> ja("Mødrekvote tas ${uttaksplan.description}", des, id)
        else -> nei("Mødrekvote tas ikke ${uttaksplan.description} $soknadstype", des, id)
    }
})

class SoknadGjelder(soknadstype: Soknadstype) : Specification<Soknad>({
    val des = "Soknad gjelder $soknadstype"
    val id = "SoknadGjelder"

    when (soknadstype) {
        it.soknadstype -> ja("Søknad gjelder $soknadstype", des, id)
        else -> nei("Søknad gjelder ikke $soknadstype", des, id)
    }
})

class Regelsett {

    private val harBeggeForeldreRettTilForeldrePenger = HarRettTilForeldrePenger(Rolle.MOR).and(HarRettTilForeldrePenger(Rolle.FAR))
    private val gjelderSoknadFødsel = SoknadGjelder(Soknadstype.FODSEL)
    private val gjelderSoknadAdopsjon = SoknadGjelder(Soknadstype.ADOPSJON)
    private val harUttaksplanEtterFodsel = HarUttaksplanForModreKvote(Soknadstype.FODSEL, Uttaksplan.SAMMENHENGENDE)
    private val harUttaksplanEtterAdopsjon = HarUttaksplanForModreKvote(Soknadstype.ADOPSJON, Uttaksplan.INNEN_3_AAR)
    private val vilkårForFødsel = harBeggeForeldreRettTilForeldrePenger.and(gjelderSoknadFødsel).and(harUttaksplanEtterFodsel)
    private val vilkårForAdopsjon = harBeggeForeldreRettTilForeldrePenger
            .and(gjelderSoknadFødsel.not())
            .and(gjelderSoknadAdopsjon)
            .and(harUttaksplanEtterAdopsjon)

    val mødreKvote = vilkårForAdopsjon.or(vilkårForFødsel)
}