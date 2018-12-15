package no.nav.knare.core.example

data class Soknad(val hovedsoker: Person,
                  val medsoker: Person?,
                  val name: String = "Familien",
                  val soknadstype: Soknadstype) {
    fun hentSøkerIRolle(rolle: Rolle): Person? {
        when (rolle) {
            hovedsoker.rolle -> return hovedsoker
            medsoker?.rolle -> return medsoker
            else -> return null
        }
    }
}

enum class Soknadstype {
    FODSEL, ADOPSJON
}

data class Person(val name: String,
                  val rolle: Rolle,
                  val uttaksplan: Uttaksplan?,
                  val yrke: String,
                  val inntekt: Int,
                  val address: String,
                  val rettTilFp: Boolean,
                  val mndArbeid: Int)

enum class Uttaksplan(val description:String) {
    SAMMENHENGENDE("sammenhengende etter"),
    INNEN_3_AAR("innen 3 år etter"),
    SENERE("senere enn 3 år etter")
}

enum class Rolle {
    MOR, FAR
}