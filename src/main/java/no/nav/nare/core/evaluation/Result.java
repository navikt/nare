package no.nav.nare.core.evaluation;


public enum Result {

    YES(-1, "NO"),
    NO(1, "YES"),
    MAYBE(0, "MAYBE");

    private final int weight;
    private String inverse;


    Result(int weight, String inverse) {
        this.weight = weight;
        this.inverse = inverse;
    }

    public Result and(Result result) {
        return (this.weight > result.weight) ? this : result;
    }

    public Result or(Result result) {
        return (this.weight < result.weight) ? this : result;
    }

    public Result not(){
        return Result.valueOf(this.inverse);

    }
}
