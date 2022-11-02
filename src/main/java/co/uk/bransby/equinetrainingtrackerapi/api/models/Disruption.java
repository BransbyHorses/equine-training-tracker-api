package co.uk.bransby.equinetrainingtrackerapi.api.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name="disruptions")
public class Disruption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DisruptionCode reason;
    @ManyToOne
    @JoinColumn(name = "equine_id")
    private Equine equine;
    private final LocalDateTime startDate;
    private LocalDateTime endDate;

    public Disruption() {
        this.startDate = LocalDateTime.now();
    }

    public Disruption(Long id, DisruptionCode reason, Equine equine, LocalDateTime endDate) {
        this.id = id;
        this.reason = reason;
        this.equine = equine;
        this.startDate = LocalDateTime.now();
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Disruption category = (Disruption) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
