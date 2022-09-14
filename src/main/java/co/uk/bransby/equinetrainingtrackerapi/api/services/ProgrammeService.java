package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.ProgrammeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final EquineRepository equineRepository;

    public List<Programme> getAllProgrammes() {
        return programmeRepository.findAll();
    }

    public Programme getProgramme(Long id) {
        return programmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
    }

    public Programme createProgramme(Programme Programme){
        return programmeRepository.saveAndFlush(Programme);
    }

    public Programme updateProgramme(Long id, Programme updatedProgrammeValues) {
        Programme programmeToUpdate = programmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
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
