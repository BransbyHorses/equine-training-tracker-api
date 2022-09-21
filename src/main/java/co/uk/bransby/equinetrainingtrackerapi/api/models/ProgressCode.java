package co.uk.bransby.equinetrainingtrackerapi.api.models;

public enum ProgressCode {
    NOT_ABLE("Not Able"),
    JUST_STARTED("Just Started"),
    OK_WITH_LIMITS("Ok With Limits"),
    CONFIDENT("Confident");

    private final String string;

    ProgressCode(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
