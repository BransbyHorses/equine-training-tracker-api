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
@RequiredArgsConstructor
@Entity(name = "Yard")
@Table(name="yards")
public class Yard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    @OneToMany
//    @JoinColumn(name = "cognito_id")
//    private List<Trainer> trainers;

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
}