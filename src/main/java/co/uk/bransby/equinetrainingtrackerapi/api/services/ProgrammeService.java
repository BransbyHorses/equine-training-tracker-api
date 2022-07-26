package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.ProgrammeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final EquineRepository equineRepository;

    public ProgrammeService(ProgrammeRepository programmeRepository, EquineRepository equineRepository) {
        this.programmeRepository = programmeRepository;
        this.equineRepository = equineRepository;
    }

    public List<Programme> getAllProgrammes() {
        return programmeRepository.findAll();
    }

    public Optional<Programme> getProgramme(Long id) {
        return programmeRepository.findById(id);
    }

    public Programme createProgramme(Programme Programme){
        return programmeRepository.saveAndFlush(Programme);
    }

    public Programme updateProgramme(Long id, Programme updatedProgrammeValues) throws EntityNotFoundException {
        Programme programmeToUpdate = programmeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedProgrammeValues, programmeToUpdate, "id");
        return programmeRepository.saveAndFlush(programmeToUpdate);
    }

    public void deleteProgramme(Long id) {
        Programme programme = programmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
        for(Equine equine : programme.getEquines()) {
            programme.removeEquine(equine);
            equine.setProgramme(null);
            equineRepository.saveAndFlush(equine);
        }
        programmeRepository.saveAndFlush(programme);
        programmeRepository.deleteById(id);
    }
}
