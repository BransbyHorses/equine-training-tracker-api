package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_category_id")
    private TrainingCategory trainingCategory;

    @ManyToOne
    @JoinColumn(name = "equine_id")
    private Equine equine;

    @OneToMany(mappedBy = "trainingProgramme", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SkillProgressRecord> skillProgressRecords;

    @OneToMany(mappedBy = "trainingProgramme", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SkillTrainingSession> skillTrainingSessions;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private final LocalDateTime createdOn;

    public TrainingProgramme(Long id, TrainingCategory trainingCategory, Equine equine, List<SkillProgressRecord> skillProgressRecords, List<SkillTrainingSession> skillTrainingSessions, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.trainingCategory = trainingCategory;
        this.equine = equine;
        this.skillProgressRecords = skillProgressRecords;
        this.skillTrainingSessions = skillTrainingSessions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdOn = LocalDateTime.now();
    }


    public TrainingProgramme(Long id, TrainingCategory trainingCategory, Equine equine, List<SkillProgressRecord> skillProgressRecords, List<SkillTrainingSession> skillTrainingSessions, LocalDateTime endDate) {
        this.id = id;
        this.trainingCategory = trainingCategory;
        this.equine = equine;
        this.skillProgressRecords = skillProgressRecords;
        this.skillTrainingSessions = skillTrainingSessions;
        this.startDate = LocalDateTime.now();
        this.endDate = endDate;
        this.createdOn = LocalDateTime.now();
    }

    public TrainingProgramme() {
        this.startDate = LocalDateTime.now();
        this.createdOn = LocalDateTime.now();
    }

    public void addSkillTrainingSession(SkillTrainingSession newSkillTrainingSession) {
        skillTrainingSessions.add(newSkillTrainingSession);
    }

    public void addSkillProgressRecord(SkillProgressRecord skillProgressRecord) {
        skillProgressRecords.add(skillProgressRecord);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrainingProgramme trainingProgramme = (TrainingProgramme) o;
        return id != null && Objects.equals(id, trainingProgramme.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
