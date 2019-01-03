package no.nav.knare.api

import com.google.gson.Gson
import no.nav.knare.core.example.*
import spark.Request
import spark.Response
import spark.Spark.*

fun main(args: Array<String>) {
    val gson = Gson()
    port(1338)
    staticFiles.location("/public/")
    get("/api/vurdering/modrekvote", { _: Request, _: Response -> Regelsett().mødreKvote.evaluer() }, {gson.toJson(it)})

}