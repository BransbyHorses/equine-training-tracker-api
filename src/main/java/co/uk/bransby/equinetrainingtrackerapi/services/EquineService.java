package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.repositories.EquineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EquineService {

    private final EquineRepository equineRepository;

    public EquineService(EquineRepository equineRepository){
        this.equineRepository = equineRepository;
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

    public Equine updateEquine(Long id, Equine updatedEquineValues) throws EntityNotFoundException {
        Equine equineToUpdate = equineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedEquineValues, equineToUpdate, "id");
        return equineRepository.saveAndFlush(equineToUpdate);
    }

    public void deleteEquine(Long id){
        equineRepository.deleteById(id);
    }
}
