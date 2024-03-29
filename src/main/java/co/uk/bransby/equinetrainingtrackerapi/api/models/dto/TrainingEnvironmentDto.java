package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

public class TrainingEnvironmentDto {
    private Long id;
    private String name;

    public TrainingEnvironmentDto() {}

    public TrainingEnvironmentDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
