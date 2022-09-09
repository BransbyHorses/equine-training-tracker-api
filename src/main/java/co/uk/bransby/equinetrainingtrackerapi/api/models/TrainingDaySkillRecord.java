package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.springframework.core.env.Environment;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "training_day_skill_record")
public class TrainingDaySkillRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_day_id")
    private TrainingDay trainingDay;

    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "training_method_id")
    private TrainingMethod trainingMethod;

//    @ManyToOne
//    @JoinColumn(name = "environment_id")
//    private TrainingEnvironment environment;

    private Long timeInMinutes;

    private String notes;
}
