
package no.nav.nare.api;

import com.google.gson.Gson;
import no.nav.nare.core.regelsettyper.Ruleset;
import no.nav.nare.input.Person;
import no.nav.nare.input.Rolle;
import no.nav.nare.input.Soknad;
import no.nav.nare.input.Uttaksplan;

import static spark.Spark.*;


public class Server {

    public static void main(String[] args) {
        Gson gson = new Gson();
        port(1337);
        staticFiles.location("/public");
        get("/api/vurdering/modrekvote", (req, res) -> Ruleset.modrekvote().evaluer(dummySoknad()), gson::toJson);
    }


    private static Soknad dummySoknad() {
        Person far = new Person("Far", Rolle.FAR, "X", 500000, 80, "Oslo", true);
        Person mor = new Person("Mor", Rolle.MOR, "Y", 600000, 24, "Oslo", true);

        mor.setUttaksplan(Uttaksplan.SAMMENHENGENDE);
        return Soknad.fodselSøknad(mor).medSøker(far);
    }

}
