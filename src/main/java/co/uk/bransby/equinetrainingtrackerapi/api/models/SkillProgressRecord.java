package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="skill_progress_records")
public class SkillProgressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "training_programme_id")
    private TrainingProgramme trainingProgramme;
    @OneToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
    private ProgressCode progressCode; // TODO - create ProgressCode enum (confirm with charity)
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer time; // in minutes
}
