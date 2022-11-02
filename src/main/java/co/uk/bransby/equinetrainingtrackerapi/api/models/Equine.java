package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="equines")
public class Equine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "yard_id", referencedColumnName = "id")
    private Yard yard;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private EquineStatus equineStatus;

    @OneToMany(mappedBy = "equine", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TrainingProgramme> trainingProgrammes;

    @ManyToOne
    @JoinColumn(name = "learner_type_id")
    private LearnerType learnerType;

    @OneToMany(mappedBy = "equine", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<HealthAndSafetyFlag> healthAndSafetyFlags;

    @OneToMany(mappedBy = "equine", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Disruption> disruptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Equine equine = (Equine) o;
        return id != null && Objects.equals(id, equine.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
