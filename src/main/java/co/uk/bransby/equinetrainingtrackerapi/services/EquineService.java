package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.repositories.ProgrammeRepository;
import co.uk.bransby.equinetrainingtrackerapi.repositories.YardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EquineService {

    private final EquineRepository equineRepository;
    private final YardRepository yardRepository;
    private final ProgrammeRepository programmeRepository;

    public EquineService(EquineRepository equineRepository, YardRepository yardRepository, ProgrammeRepository programmeRepository){
        this.equineRepository = equineRepository;
        this.yardRepository = yardRepository;
        this.programmeRepository = programmeRepository;
    }

    public List<Equine> getAllEquines(){
        return equineRepository.findAll();
    }

    public Optional<Equine> getEquine(Long id) {
        return equineRepository.findById(id);
    }

    public Equine createEquine(Equine equine){
        return equineRepository.saveAndFlush(equine);
    }

    public Equine updateEquine(Long id, Equine updatedEquineValues) {
        Equine equineToUpdate = equineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedEquineValues, equineToUpdate, "id");
        return equineRepository.save(equineToUpdate);
    }

    public void deleteEquine(Long id){
        equineRepository.deleteById(id);
    }

    public Equine assignEquineToYard(Long equineId, Long yardId) {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Yard yard = yardRepository.findById(yardId)
                            .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + yardId));
                    equine.setYard(yard);
                    return equineRepository.saveAndFlush(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }

    public Equine assignEquineToProgramme(Long equineId, Long programmeId) {
        return equineRepository.findById(equineId)
                .map(equine -> {
                    Programme programme = programmeRepository.findById(programmeId)
                            .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + programmeId));
                    equine.setProgramme(programme);
                    return equineRepository.saveAndFlush(equine);
                })
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));
    }

    // TODO - assign equine to category logic
    // TODO - add a skill to an equine logic
}
