package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.springframework.core.env.Environment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "skill_training_session")
public class SkillTrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "training_programme_id")
    private TrainingProgramme trainingProgramme;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "training_method_id")
    private TrainingMethod trainingMethod;

//    @ManyToOne
//    @JoinColumn(name = "environment_id")
//    private TrainingEnvironment environment;

    private Long trainingTime;

    private String notes;
}
