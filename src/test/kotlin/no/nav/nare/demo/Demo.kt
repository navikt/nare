package no.nav.nare.demo

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import no.nav.nare.core.demo.Person
import no.nav.nare.core.demo.Rolle
import no.nav.nare.core.demo.Soknad
import no.nav.nare.core.demo.Soknadstype
import no.nav.nare.core.demo.Uttaksplan
import no.nav.nare.core.evaluations.Resultat
import spark.Spark.get
import spark.Spark.port
import spark.Spark.staticFiles

fun main() {
   val builder = GsonBuilder()
   val gson = builder.create()
   port(1339)
   staticFiles.location("/public")
   get("/api", { _, _ -> Regelsett().mødrekvote.evaluer(søknad) }, { gson.toJson(it) })

}

private val søknad = Soknad(
   hovedsoker = Person(name = "Mor", rolle = Rolle.MOR, address = "Oslo", inntekt = 600000, mndArbeid = 24, rettTilFp = true, yrke = "A", uttaksplan = Uttaksplan.SENERE),
   medsoker = Person(name = "Far", rolle = Rolle.FAR, address = "Oslo", inntekt = 500000, mndArbeid = 80, rettTilFp = true, yrke = "B", uttaksplan = null),
   søknadstype = Soknadstype.FODSEL)

