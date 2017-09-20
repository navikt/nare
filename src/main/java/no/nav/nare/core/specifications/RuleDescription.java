package no.nav.nare.core.specifications;

import com.google.gson.GsonBuilder;
import no.nav.nare.core.evaluation.Operator;

import java.util.Arrays;
import java.util.List;


public class RuleDescription {

    private  String ruleIdentifcation;
    private  String ruleDescription;
    private Operator operator;
    private List<RuleDescription> children;


    public RuleDescription(Operator operator, String ruleIdentifcation, String ruleDescription, RuleDescription... children) {
        this.operator = operator;
        this.children = Arrays.asList(children);
        this.ruleIdentifcation = ruleIdentifcation;
        this.ruleDescription = ruleDescription;

    }

    public RuleDescription(String ruleIdentifcation, String beskrivelse) {
        this.ruleDescription = beskrivelse;
        this.ruleIdentifcation = ruleIdentifcation;
    }


    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
