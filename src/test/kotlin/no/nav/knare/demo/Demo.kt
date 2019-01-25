package no.nav.knare.demo

import com.google.gson.Gson
import no.nav.knare.core.demo.*
import spark.Request
import spark.Response
import spark.Spark.*


fun main(args: Array<String>) {
    val gson = Gson()
    port(1337)
    staticFiles.location("/public")
    get("/api", { _: Request, _: Response -> Regelsett().mødrekvote.evaluer(søknad) }, { gson.toJson(it) })

}

private val søknad = Soknad(
        hovedsoker = Person(name = "Mor", rolle = Rolle.MOR, address = "Oslo", inntekt = 600000, mndArbeid = 24, rettTilFp = true, yrke = "A", uttaksplan = Uttaksplan.SAMMENHENGENDE),
        medsoker = Person(name = "Far", rolle = Rolle.FAR, address = "Oslo", inntekt = 500000, mndArbeid = 80, rettTilFp = true, yrke = "B", uttaksplan = null),
        søknadstype = Soknadstype.FODSEL)

