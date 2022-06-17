package co.uk.bransby.equinetrainingtrackerapi.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="EQUINES")
public class Equine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String yard;
    private Long trainerId;
    private String category;
    private String programme;
    private String skills;
    private String training;
    private Boolean onHold;

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
