package co.uk.bransby.equinetrainingtrackerapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "equines")
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


}
