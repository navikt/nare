package no.nav.knare.core.example

import no.nav.knare.core.evaluations.Evaluation
import no.nav.knare.core.evaluations.Evaluation.Companion.ja
import no.nav.knare.core.evaluations.Evaluation.Companion.nei
import no.nav.knare.core.specifications.Specification
import no.nav.knare.core.specifications.Specification.Companion.not
import no.nav.knare.core.specifications.Specification.Companion.rule

class HarArbeidetSisteMnd(
        val month: Int
) : Specification<Soknad>() {
    override var description: () -> String = { "Har dokumentert sammenhengende arbeid siste $month mnd" }
    override var identity: () -> String = { "FK_VK_10.x" }
    override fun evaluate(søknad: Soknad): Evaluation {
        return if (søknad.hovedsoker.mndArbeid >= month) Evaluation.ja("Person har jobbet ${søknad.hovedsoker.mndArbeid} måneder, som er tilstrekkelig", description(), identity())
        else Evaluation.nei("Person er oppfort med ${søknad.hovedsoker.mndArbeid} mnd arbeid. Dekker ikke kravet til ${month} mnd med arbeid", description(), identity())
    }
}

class HarRettTilForeldrePenger(
        val rolle: Rolle

) : Specification<Soknad>() {
    override var description: () -> String = { "Har søker med rolle $rolle rett til foreldrepenger?" }
    override var identity: () -> String = { "FK_VK_10.1" }
    override fun evaluate(søknad: Soknad): Evaluation {
        val sokerIRolle = søknad.hentSøkerIRolle(rolle)
        return when {
            sokerIRolle == null -> nei("Ingen søker med rolle $rolle", description(), identity())
            !sokerIRolle.rettTilFp -> nei("Søker med rolle $rolle har ikke rett til foreldrepenger", description(), identity())
            else -> ja("Søker med rolle $rolle har rett til foreldrepenger", description(), identity())
        }
    }
}

class HarUttaksplanForModreKvote(
        val soknadstype: Soknadstype,
        val uttaksplan: Uttaksplan
) : Specification<Soknad>() {
    override var description: () -> String = { "" }
    override var identity: () -> String = { "FK_VK 10.4/FK_VK 10.5/FK_VK 10.6" }
    override fun evaluate(søknad: Soknad): Evaluation {
        val mor = søknad.hentSøkerIRolle(Rolle.MOR)
        return when {
            mor == null -> nei("Ingen søker med rolle ${Rolle.MOR}", description(), identity())
            mor.uttaksplan == null -> nei("Det foreligger ingen uttaksplan for ${Rolle.MOR}", description(), identity())
            uttaksplan == mor.uttaksplan -> ja("Mødrekvote tas ${uttaksplan.description}", description(), identity())
            else -> nei("Mødrekvote tas ikke ${uttaksplan.description} $soknadstype", description(), identity())
        }
    }
}

class SoknadGjelder(
        val soknadstype: Soknadstype
) : Specification<Soknad>() {
    override var description: () -> String = { "Soknad gjelder $soknadstype" }
    override var identity: () -> String = { "SoknadGjelder" }
    override fun evaluate(søknad: Soknad): Evaluation {
        return when (soknadstype) {
            søknad.soknadstype -> ja("Søknad gjelder $soknadstype", description(), identity())
            else -> nei("Søknad gjelder ikke $soknadstype", description(), identity())
        }
    }
}

class Regelsett {

    private val harBeggeForeldreRettTilForeldrePenger = rule(
            spec = HarRettTilForeldrePenger(Rolle.MOR).and(HarRettTilForeldrePenger(Rolle.FAR)),
            identity = "FK_VK_10.1",
            description = "Har begge foreldre rett til foreldrepenger?")

    private val gjelderSoknadFødsel = rule(
            spec = SoknadGjelder(Soknadstype.FODSEL),
            identity = "FK_VK 10.2",
            description = "Gjelder søknad fødsel?"
    )
    private val gjelderSoknadAdopsjon = rule(
            spec = SoknadGjelder(Soknadstype.ADOPSJON),
            identity = "FK_VK 10.3",
            description = "Gjelder søknad adopsjon?"
    )
    private val harUttaksplanEtterFodsel = rule(
            spec = HarUttaksplanForModreKvote(
                    soknadstype = Soknadstype.FODSEL,
                    uttaksplan = Uttaksplan.SAMMENHENGENDE),
            identity = "FK_VK_10.4",
            description = "Har mor uttaksplan sammenhengende or tre år etter fødsel?"
    )
    private val harUttaksplanEtterAdopsjon = rule(
            spec = HarUttaksplanForModreKvote(
                    soknadstype = Soknadstype.ADOPSJON,
                    uttaksplan = Uttaksplan.INNEN_3_AAR),
            identity = "FK_VK_10.5",
            description = "Har mor uttaksplan sammenhengende or tre år etter adopsjon?"
    )
    private val vilkårForFødsel = rule(
            spec = harBeggeForeldreRettTilForeldrePenger.and(gjelderSoknadFødsel).and(harUttaksplanEtterFodsel),
            identity = "FK_VK.10.A",
            description = ""
    )
    private val vilkårForAdopsjon = rule(
            spec = harBeggeForeldreRettTilForeldrePenger
                    .and(rule(spec = not(gjelderSoknadFødsel), description = "søknad gjelder ikke fødsel", identity = ""))
                    .and(gjelderSoknadAdopsjon)
                    .and(harUttaksplanEtterAdopsjon),
            identity = "FK_VK.10.B",
            description = "")

    val mødreKvote = rule(
            spec = vilkårForAdopsjon.or(vilkårForFødsel),
            identity = "FK_VK.10",
            description = "Er vilkår for mødrekvote oppfylt for enten fødsel or adopsjon?")
}