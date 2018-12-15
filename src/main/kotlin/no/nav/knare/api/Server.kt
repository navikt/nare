package no.nav.knare.api

import com.google.gson.Gson
import no.nav.knare.core.example.*
import spark.Request
import spark.Response
import spark.ResponseTransformer
import spark.Spark.*

fun main(args: Array<String>) {
    val gson: Gson = Gson()
    port(1337)
    staticFiles.location("/public")
    get("/api/vurdering/modrekvote", { _: Request, _: Response -> Regelsett().mødreKvote.evaluate(dummySoknad) }, {gson.toJson(it)})

}

val dummySoknad = Soknad(medsoker = Person(name = "Far", rolle = Rolle.FAR, address = "Oslo", inntekt = 500000, mndArbeid = 80, rettTilFp = true, yrke = "X", uttaksplan = null),
        hovedsoker = Person(name = "Mor", rolle = Rolle.MOR, address = "Oslo", inntekt = 600000, mndArbeid = 24, rettTilFp = true, yrke = "Y", uttaksplan = Uttaksplan.SAMMENHENGENDE),
        soknadstype = Soknadstype.FODSEL)