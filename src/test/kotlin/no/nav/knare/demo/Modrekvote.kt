package no.nav.knare.demo

import no.nav.knare.core.demo.Rolle
import no.nav.knare.core.demo.Soknad
import no.nav.knare.core.demo.Soknadstype
import no.nav.knare.core.demo.Soknadstype.ADOPSJON
import no.nav.knare.core.demo.Soknadstype.FODSEL
import no.nav.knare.core.demo.Uttaksplan
import no.nav.knare.core.demo.Uttaksplan.INNEN_3_AAR
import no.nav.knare.core.demo.Uttaksplan.SAMMENHENGENDE
import no.nav.knare.core.evaluations.Evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.ja
import no.nav.knare.core.evaluations.Evaluering.Companion.nei
import no.nav.knare.core.specifications.Spesifikasjon

class Regelsett {

   private val morHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      beskrivelse = "Har søker med rolle mor rett til foreldrepenger?",
      identitet = "FK_VK_10.1",
      implementasjon = { søknad -> søkerHarPåkrevdRolle(Rolle.MOR, søknad) }
   )

   private val farHarRettTilForeldrepenger = Spesifikasjon<Soknad>(
      beskrivelse = "Har søker med rolle fae rett til foreldrepenger?",
      identitet = "FK_VK_10.1",
      implementasjon = { søknad -> søkerHarPåkrevdRolle(Rolle.FAR, søknad) }
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

         soknadGjelder(ADOPSJON, søknad) }
   )


   private val harUttaksplanEtterFodsel = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter fødsel?",
      identitet = "FK_VK 10.4/FK_VK 10.5",
      implementasjon = { søknad ->
         harUttaksplanForModreKvote(FODSEL, SAMMENHENGENDE, søknad) eller
            harUttaksplanForModreKvote(FODSEL, INNEN_3_AAR, søknad)
      }
   )

   private val harUttaksplanEtterAdopsjon = Spesifikasjon<Soknad>(
      beskrivelse = "Foreligger det korrekt uttaksplan etter adopsjon?",
      identitet = "FK_VK 10.6",
      implementasjon = { søknad -> harUttaksplanForModreKvote(ADOPSJON, INNEN_3_AAR, søknad) }
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


fun harUttaksplanForModreKvote(soknadstype: Soknadstype, uttaksplan: Uttaksplan, søknad: Soknad): Evaluering =
   søknad.hentSøkerIRolle(Rolle.MOR)?.let { mor ->
      mor.uttaksplan?.let { morsUttaksplan ->
         if (morsUttaksplan == uttaksplan)
            ja("Mødrekvote tas ${morsUttaksplan.description}")
         else
            nei("Mødrekvote tas ikke ${morsUttaksplan.description} $soknadstype")
      } ?: nei("Det foreligger ingen uttaksplan for mor")
   } ?: nei("Søker er ikke mor")





