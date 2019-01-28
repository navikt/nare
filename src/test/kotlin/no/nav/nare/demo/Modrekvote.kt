package no.nav.nare.demo

import no.nav.nare.core.demo.*
import no.nav.nare.core.demo.Rolle.*
import no.nav.nare.core.demo.Soknadstype.*
import no.nav.nare.core.demo.Uttaksplan.*
import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Evaluering.Companion.ja
import no.nav.nare.core.evaluations.Evaluering.Companion.kanskje
import no.nav.nare.core.evaluations.Evaluering.Companion.nei
import no.nav.nare.core.specifications.Spesifikasjon

class Regelsett {

   private val morHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      beskrivelse = "Har søker med rolle mor rett til foreldrepenger?",
      identitet = "FK_VK_10.1",
      implementasjon = { søknad -> søkerHarPåkrevdRolle(MOR, søknad) }
   )

   private val farHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      beskrivelse = "Har søker med rolle fae rett til foreldrepenger?",
      identitet = "FK_VK_10.1",
      implementasjon = { søknad -> søkerHarPåkrevdRolle(FAR, søknad) }
   )

   private val gjelderSoknadFødsel = Spesifikasjon<Soknad>(
      beskrivelse = "Soknad gjelder fødsel?",
      identitet = "FK_VK 10.2",
      implementasjon = { søknad -> soknadGjelder(FODSEL, søknad) }
   )

   private val gjelderSoknadAdopsjon = Spesifikasjon<Soknad>(
      beskrivelse = "Soknad gjelder adopsjon?",
      identitet = "FK_VK 10.23",
      implementasjon = { søknad ->

         soknadGjelder(ADOPSJON, søknad)
      }
   )


   private val harUttaksplanEtterFodsel = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter fødsel?",
      identitet = "FK_VK 10.4/FK_VK 10.5",
      implementasjon = { søknad ->
         harUttaksplanForModreKvote(FODSEL, søknad)
      }
   )

   private val harUttaksplanEtterAdopsjon = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter adopsjon?",
      identitet = "FK_VK 10.6",
      implementasjon = { søknad -> harUttaksplanForModreKvote(ADOPSJON, søknad) }
   )

   private val harBeggeForeldreRettTilForeldrepenger = (farHarRettTilForeldrepenger og morHarRettTilForeldrepenger).med(
      identitet = "FK_VK_1.3",
      beskrivelse = "Har begge foreldre rett til foreldrepenger?"
   )
   private val vilkårForFødsel =(
      harBeggeForeldreRettTilForeldrepenger og
         gjelderSoknadFødsel og harUttaksplanEtterFodsel).med(identitet = "FK_VK 1.11", beskrivelse = "Er vilkår for fødsel oppfylt")


   private val vilkårForAdopsjon =(
      harBeggeForeldreRettTilForeldrepenger og
         gjelderSoknadFødsel.ikke() og
         gjelderSoknadAdopsjon og
         harUttaksplanEtterAdopsjon).med(
      identitet = "FK_VK 1.2",
      beskrivelse = "Er vilkår for adopsjon oppfylt?"
   )

   val mødrekvote = (vilkårForFødsel eller vilkårForAdopsjon).med(
      identitet = "FK_VK_1",
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





