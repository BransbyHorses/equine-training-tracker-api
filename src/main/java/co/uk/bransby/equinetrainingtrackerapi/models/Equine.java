package co.uk.bransby.equinetrainingtrackerapi.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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
    private Category category;
    @ManyToOne
    @JoinColumn(name = "programme_id", referencedColumnName = "id")
    private Programme programme;
    @ManyToMany
    @JoinTable(
            name = "equines_skills",
            joinColumns = @JoinColumn(name = "equine_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<Skill> skills;
//    private String training;
//    private Boolean onHold;
//    private Long trainerId;

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
