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
    private Long equine_id;
    private String equine_name;
    private Long equine_yard;
    private Long equine_trainerId;
    private String equine_category;
    private String equine_programme;
    private String equine_skills;
    private String equine_training;
    private Boolean equine_on_hold;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Equine equine = (Equine) o;
        return equine_id != null && Objects.equals(equine_id, equine.equine_id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
