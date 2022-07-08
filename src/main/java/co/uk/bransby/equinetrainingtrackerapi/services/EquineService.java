package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.*;
import co.uk.bransby.equinetrainingtrackerapi.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class EquineService {

    private final EquineRepository equineRepository;
    private final ProgrammeRepository programmeRepository;
    private final YardRepository yardRepository;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;

    public List<Equine> getAllEquines(){
        return equineRepository.findAll();
    }

    public Optional<Equine> getEquine(Long id) {
        return equineRepository.findById(id);
    }

    public Equine createEquine(Equine equine){
        return equineRepository.saveAndFlush(equine);
    }

    public Equine updateEquine(Long id, Equine updatedEquineValues) throws EntityNotFoundException {
        Equine equineToUpdate = equineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedEquineValues, equineToUpdate, "id");
        return equineRepository.saveAndFlush(equineToUpdate);
    }

    public void deleteEquine(Long id){
        equineRepository.deleteById(id);
    }

    public Equine assignEquineToProgramme(Long equineId, Long programmeId) throws EntityNotFoundException {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Programme programme = programmeRepository.findById(programmeId)
                            .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + programmeId));
                    equine.setProgramme(programme);
                    return equineRepository.saveAndFlush(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }

    public Equine assignEquineToYard(Long equineId, Long yardId) throws EntityNotFoundException {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Yard yard = yardRepository.findById(yardId)
                            .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + yardId));
                    equine.setYard(yard);
                    return equineRepository.saveAndFlush(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }

    public Equine assignEquineToCategory(Long equineId, Long categoryId) throws EntityNotFoundException {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + categoryId));
                    equine.setCategory(category);
                    return equineRepository.saveAndFlush(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }

    public Equine assignEquineASkill(Long equineId, Long skillId) {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Skill skill = skillRepository.findById(skillId)
                            .orElseThrow(() -> new EntityNotFoundException("No skill found with id: " + skillId));
                    Set<Skill> skills = equine.getSkills();
                    skills.add(skill);
                    equine.setSkills(skills);
                    return equineRepository.save(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }
}
