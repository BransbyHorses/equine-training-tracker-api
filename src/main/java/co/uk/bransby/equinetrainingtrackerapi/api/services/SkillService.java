package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final EquineRepository equineRepository;

    public SkillService(SkillRepository skillRepository, EquineRepository equineRepository) {
        this.skillRepository = skillRepository;
        this.equineRepository = equineRepository;
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
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
        for(Equine equine : skill.getEquines()) {
            skill.removeEquine(equine);
            Equine equineDb = equineRepository.getById(equine.getId());
            Set<Skill> skills = equineDb.getSkills();
            skills.remove(skill);
            equineDb.setSkills(skills);
            equineRepository.saveAndFlush(equineDb);
        }
        skillRepository.saveAndFlush(skill);
        skillRepository.deleteById(id);
    }

}
