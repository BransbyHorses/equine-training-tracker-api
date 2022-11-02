package co.uk.bransby.equinetrainingtrackerapi.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EquineStatus {
    AWAITING_TRAINING("Awaiting Training", true),
    IN_TRAINING("In Training", true),
    RETURNED_TO_OWNER("Returned To Owner", false),
    REHOMED("Rehomed", false),
    EUTHANISED("Euthanised", false),
    OTHER("Other", false);

    private final String string;
    private final boolean categorisedAsTraining;

    EquineStatus(String string, boolean categorisedAsTraining) {
        this.string = string;
        this.categorisedAsTraining = categorisedAsTraining;
    }

    public String getString() {
        return string;
    }

    public boolean isCategorisedAsTraining() {
        return categorisedAsTraining;
    }

    @JsonCreator
    public static EquineStatus getEquineStatus(String string) {
        for(EquineStatus equineStatus : EquineStatus.values()) {
            if(equineStatus.getString().equals(string)){
                return equineStatus;
            }
        }
        return null;
    }
}
