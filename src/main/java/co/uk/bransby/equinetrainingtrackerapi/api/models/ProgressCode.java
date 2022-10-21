package co.uk.bransby.equinetrainingtrackerapi.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProgressCode {
    NOT_ABLE("Not able"),
    JUST_STARTED("Limited"),
    OK_WITH_LIMITS("Ok"),
    CONFIDENT("Confident");

    private final String string;

    ProgressCode(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @JsonCreator
    public static ProgressCode getProgressCode(String string) {
        for(ProgressCode pc : ProgressCode.values()) {
            if(pc.getString().equals(string)){
                return pc;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return string;
    }
}
