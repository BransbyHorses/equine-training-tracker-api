package co.uk.bransby.equinetrainingtrackerapi;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.persistence.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class EquineTrainingTrackerApiApplication {


	public static void main(String[] args) {SpringApplication.run(EquineTrainingTrackerApiApplication.class, args);}

}
