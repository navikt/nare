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
      beskrivelse = "Har søker med rolle mor rett til foreldrepenger?",
      identifikator = "FK_VK_10.1",
      implementasjon = { søkerHarPåkrevdRolle(MOR, this) }
   )

   private val farHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      beskrivelse = "Har søker med rolle fae rett til foreldrepenger?",
      identifikator = "FK_VK_10.1",
      implementasjon = { søkerHarPåkrevdRolle(FAR, this) }
   )

   private val gjelderSoknadFødsel = Spesifikasjon<Soknad>(
      beskrivelse = "Soknad gjelder fødsel?",
      identifikator = "FK_VK 10.2",
      implementasjon = { soknadGjelder(FODSEL, this) }
   )

   private val gjelderSoknadAdopsjon = Spesifikasjon<Soknad>(
      beskrivelse = "Soknad gjelder adopsjon?",
      identifikator = "FK_VK 10.23",
      implementasjon = { soknadGjelder(ADOPSJON, this) }
   )


   private val harUttaksplanEtterFodsel = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter fødsel?",
      identifikator = "FK_VK 10.4/FK_VK 10.5",
      implementasjon = { harUttaksplanForModreKvote(FODSEL, this)
      }
   )

   private val harUttaksplanEtterAdopsjon = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter adopsjon?",
      identifikator = "FK_VK 10.6",
      implementasjon = { harUttaksplanForModreKvote(ADOPSJON, this) }
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





