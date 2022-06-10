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
@Table(name = "PROGRAMMES")

public class Programme {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Programme programme = (Programme) o;
        return id != null && Objects.equals(id, programme.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
