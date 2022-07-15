package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public Skill create(Skill skill) {
        Optional<Skill> skillOptional = Optional.ofNullable(skillRepository.findByName(skill.getName()));
        if(skillOptional.isPresent()) {
            throw new EntityExistsException(skill.getName() + " already exists");
        }

        return skillRepository.save(skill);
    }

    public Skill update(Skill updatedSkill, Long id) {
        return skillRepository.findById(id)
                .map(skill -> {
                    skill = updatedSkill;
                    skill.setId(id);
                    return skillRepository.save(skill);
                })
                .orElseGet(() -> {
                    updatedSkill.setId(id);
                    return skillRepository.save(updatedSkill);
                });
    }

    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }

    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }

}
