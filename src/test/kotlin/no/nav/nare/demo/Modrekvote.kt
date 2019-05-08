package no.nav.nare.demo

import no.nav.nare.core.demo.Rolle
import no.nav.nare.core.demo.Rolle.FAR
import no.nav.nare.core.demo.Rolle.MOR
import no.nav.nare.core.demo.Soknad
import no.nav.nare.core.demo.Soknadstype
import no.nav.nare.core.demo.Soknadstype.ADOPSJON
import no.nav.nare.core.demo.Soknadstype.FODSEL
import no.nav.nare.core.demo.Uttaksplan.INNEN_3_AAR
import no.nav.nare.core.demo.Uttaksplan.SAMMENHENGENDE
import no.nav.nare.core.demo.Uttaksplan.SENERE
import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Evaluering.Companion.ja
import no.nav.nare.core.evaluations.Evaluering.Companion.kanskje
import no.nav.nare.core.evaluations.Evaluering.Companion.nei
import no.nav.nare.core.specifications.Spesifikasjon

class Regelsett {

   private val morHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      identifikator = "FK_VK_10.1",
      beskrivelse = "Har søker med rolle mor rett til foreldrepenger?",
      implementasjon = { søkerHarPåkrevdRolle(MOR, this) }
   )

   private val farHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      identifikator = "FK_VK_10.1",
      beskrivelse = "Har søker med rolle fae rett til foreldrepenger?",
      implementasjon = { søkerHarPåkrevdRolle(FAR, this) }
   )

   private val gjelderSoknadFødsel = Spesifikasjon<Soknad>(
      identifikator = "FK_VK 10.2",
      beskrivelse = "Soknad gjelder fødsel?",
      implementasjon = { soknadGjelder(FODSEL, this) }
   )

   private val gjelderSoknadAdopsjon = Spesifikasjon<Soknad>(
      identifikator = "FK_VK 10.23",
      beskrivelse = "Soknad gjelder adopsjon?",
      implementasjon = { soknadGjelder(ADOPSJON, this) }
   )


   private val harUttaksplanEtterFodsel = Spesifikasjon<Soknad>(
      identifikator = "FK_VK 10.4/FK_VK 10.5",
      beskrivelse = "Foreligger det korrekt uttaksplan etter fødsel?",
      implementasjon = { harUttaksplanForModreKvote(FODSEL, this)
      }
   )

   private val harUttaksplanEtterAdopsjon = Spesifikasjon<Soknad>(
      identifikator = "FK_VK 10.6",
      beskrivelse = "Foreligger det korrekt uttaksplan etter adopsjon?",
      implementasjon = { harUttaksplanForModreKvote(ADOPSJON, this) }
   )

   private val harBeggeForeldreRettTilForeldrepenger = (farHarRettTilForeldrepenger og morHarRettTilForeldrepenger).med(
      identifikator = "FK_VK_1.3",
      beskrivelse = "Har begge foreldre rett til foreldrepenger?"
   )
   private val vilkårForFødsel =(harBeggeForeldreRettTilForeldrepenger og gjelderSoknadFødsel og harUttaksplanEtterFodsel).med(
      identifikator = "FK_VK 1.11",
      beskrivelse = "Er vilkår for fødsel oppfylt"
   )

   private val vilkårForAdopsjon =(
      harBeggeForeldreRettTilForeldrepenger og
         gjelderSoknadFødsel.ikke() og
         gjelderSoknadAdopsjon og
         harUttaksplanEtterAdopsjon).med(
      identifikator = "FK_VK 1.2",
      beskrivelse = "Er vilkår for adopsjon oppfylt?"
   )

   val mødrekvote = (vilkårForFødsel eller vilkårForAdopsjon).med(
      identifikator = "FK_VK_1",
      beskrivelse =  "Er vilkår for mødrekvote oppfylt?"
   )

}

fun soknadGjelder(søknadstype: Soknadstype, søknad: Soknad): Evaluering =
   if (søknad.søknadstype == søknadstype)
      ja("Søknad gjelder fødsel")
   else
      nei("Søknad gjelder ikke fødsel")

fun søkerHarPåkrevdRolle(rolle: Rolle, søknad: Soknad): Evaluering =
      søknad.hentSøkerIRolle(rolle)?.let { person ->
      if (person.rettTilFp)
         ja("Søker med rolle $rolle har rett til foreldrepenger")
      else
         nei("Søker med rolle $rolle har ikke rett til foreldrepenger")
   } ?: nei("Søker har ikke rolle $rolle")


fun harUttaksplanForModreKvote(soknadstype: Soknadstype, søknad: Soknad): Evaluering =
   søknad.hentSøkerIRolle(MOR)?.let { mor ->
      mor.uttaksplan?.let { morsUttaksplan ->
         when(morsUttaksplan){
            SAMMENHENGENDE -> ja("Mødrekvote tas sammenhengende etter $soknadstype")
            INNEN_3_AAR -> nei("Mødrekvote tas ikke senere enn 3 år etter $soknadstype")
            SENERE -> kanskje("Saksbehandler må se på dette")
         }
      } ?: nei("Det foreligger ingen uttaksplan for mor")
   } ?: nei("Søker er ikke mor")





