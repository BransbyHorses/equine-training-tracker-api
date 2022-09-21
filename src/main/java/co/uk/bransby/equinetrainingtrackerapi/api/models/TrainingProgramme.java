package co.uk.bransby.equinetrainingtrackerapi.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "programmes")
public class TrainingProgramme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "equine_id")
    private Equine equine;

    @OneToMany(mappedBy = "trainingProgramme")
    private List<SkillProgressRecord> skillProgressRecords;

    @OneToMany(mappedBy = "trainingProgramme")
    @ToString.Exclude
    private List<SkillTrainingSession> skillTrainingSessions;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

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
