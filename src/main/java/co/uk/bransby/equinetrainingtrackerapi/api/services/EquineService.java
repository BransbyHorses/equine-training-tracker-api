package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
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
        Optional<Equine> equine = equineRepository.findById(equineId);
        if(equine.isPresent()) {
            Programme programme = programmeRepository.findById(programmeId)
                    .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + programmeId));
            Equine updatedEquine = equine.get();
            updatedEquine.setProgramme(programme);
            return equineRepository.saveAndFlush(updatedEquine);
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }

    public Equine assignEquineToYard(Long equineId, Long yardId) throws EntityNotFoundException {
        Optional<Equine> equineInDb = equineRepository.findById(equineId);
        if(equineInDb.isPresent()) {
            Yard yard = yardRepository.findById(yardId)
                    .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + yardId));
            Equine equine = equineInDb.get();
            equine.setYard(yard);
            return equineRepository.saveAndFlush(equine);
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }

    public Equine assignEquineToCategory(Long equineId, Long categoryId) throws EntityNotFoundException {
        Optional<Equine> equineInDb = equineRepository.findById(equineId);
        if(equineInDb.isPresent()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + categoryId));
            Equine equine = equineInDb.get();
            equine.setCategory(category);
            return equineRepository.saveAndFlush(equine);
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }

    public Equine assignEquineASkill(Long equineId, Long skillId) {
        Optional<Equine> equineInDb = equineRepository.findById(equineId);
        if(equineInDb.isPresent()) {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new EntityNotFoundException("No skill found with id: " + skillId));
            Equine equine = equineInDb.get();
            Set<Skill> equineSkills = equine.getSkills();
            if(equineSkills.contains(skill)) {
                throw new EntityExistsException("Skill with id " + skillId + "already exists on equine " + equineId);
            } else {
                equineSkills.add(skill);
                equine.setSkills(equineSkills);
                return equineRepository.saveAndFlush(equine);
            }
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }
}
