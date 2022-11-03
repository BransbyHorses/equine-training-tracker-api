package co.uk.bransby.equinetrainingtrackerapi.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EquineStatus {
    AWAITING_TRAINING("Awaiting Training", 1L, true),
    IN_TRAINING("In Training", 2L, true),
    RETURNED_TO_OWNER("Returned To Owner",3L, false),
    REHOMED("Rehomed", 4L, false),
    EUTHANISED("Euthanised",5L, false),
    OTHER("Other",6L, false);

    private final String string;
    private final Long id;
    private final boolean categorisedAsTraining;

    EquineStatus(String string, Long id, boolean categorisedAsTraining) {
        this.string = string;
        this.id = id;
        this.categorisedAsTraining = categorisedAsTraining;
    }

    public String getString() {
        return string;
    }

    public Long getId() {
        return id;
    }

    public boolean isCategorisedAsTraining() {
        return categorisedAsTraining;
    }

    public static EquineStatus getEquineStatusFromId(Long id) {
        for(EquineStatus equineStatus : EquineStatus.values()) {
            if(equineStatus.getId().equals(id)){
                return equineStatus;
            }
        }
        return null;
    }

    @JsonCreator
    public static EquineStatus getEquineStatusFromString(String string) {
        for(EquineStatus equineStatus : EquineStatus.values()) {
            if(equineStatus.getString().equals(string)){
                return equineStatus;
            }
        }
        return null;
    }
}
