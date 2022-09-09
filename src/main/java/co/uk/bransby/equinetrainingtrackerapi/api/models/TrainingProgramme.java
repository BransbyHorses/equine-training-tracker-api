package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "training_programme_skill",
            joinColumns = {@JoinColumn(name = "training_programme_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )

    @ToString.Exclude
    private List<Skill> skills;

    private Date startDate;

    private Date endDate;

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
