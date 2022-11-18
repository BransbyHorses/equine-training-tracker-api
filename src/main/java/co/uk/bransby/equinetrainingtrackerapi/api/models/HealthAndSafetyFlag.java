package co.uk.bransby.equinetrainingtrackerapi.api.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_and_safety_flags")
public class HealthAndSafetyFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "equine_id")
    private Equine equine;
    private final LocalDateTime dateCreated;

    public HealthAndSafetyFlag(Long id, String content, Equine equine) {
        this.id = id;
        this.content = content;
        this.equine = equine;
        this.dateCreated = LocalDateTime.now();
    }

    public HealthAndSafetyFlag() {
        this.dateCreated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Equine getEquine() {
        return equine;
    }

    public void setEquine(Equine equine) {
        this.equine = equine;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }


}
