
package no.nav.nare.api;

import com.google.gson.Gson;
import no.nav.nare.core.evaluation.Evaluation;
import no.nav.nare.input.Person;
import no.nav.nare.input.Rolle;
import no.nav.nare.input.Uttaksplan;
import spark.Response;

import static no.nav.nare.core.regelsettyper.Ruleset.kanskjekvote;
import static no.nav.nare.core.regelsettyper.Ruleset.modrekvote;
import static no.nav.nare.input.Soknad.adopsjonSøknad;
import static no.nav.nare.input.Soknad.fodselSøknad;
import static no.nav.nare.input.Uttaksplan.*;
import static spark.Spark.*;


public class Server {

    public static void main(String[] args) {
        Gson gson = new Gson();
        port(1337);
        staticFiles.location("/public");

        get("/api/vurdering/modrekvote", (req, res) ->
                asJson(modrekvote().evaluer(fodselSøknad(mor()).medSøker(far())), res), gson::toJson);

        get("/api/vurdering/kanskjekvote", (req, res) ->
                asJson(kanskjekvote().evaluer(adopsjonSøknad(mor()).medSøker(far())), res), gson::toJson);
    }

    private static Evaluation asJson(Evaluation ruleset, Response res) {
        res.type("application/json");
        return ruleset;
    }

    private static Person far() {
        return new Person("Far", Rolle.FAR, "X", 500000, 80, "Oslo", true);
    }

    private static Person mor() {
        return new Person("Mor", Rolle.MOR, "Y", 600000, 24, "Oslo", true).withtUttaksplan(INNEN_3_AAR);
    }

}
