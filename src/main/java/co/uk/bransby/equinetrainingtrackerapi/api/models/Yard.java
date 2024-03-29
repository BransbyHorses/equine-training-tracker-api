package co.uk.bransby.equinetrainingtrackerapi.api.models;

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
@Table(name="yards")
public class Yard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "id")
    @ToString.Exclude
    private Set<Equine> equines;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Yard yard = (Yard) o;
        return id != null && Objects.equals(id, yard.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void removeEquine(Equine equine) {
        this.equines.remove(equine);
    }
}