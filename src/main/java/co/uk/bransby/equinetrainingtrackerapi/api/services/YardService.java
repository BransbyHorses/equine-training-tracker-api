package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.YardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class YardService {

    private final YardRepository yardRepository;
    private final EquineRepository equineRepository;

    public YardService(YardRepository yardRepository, EquineRepository equineRepository) {
        this.yardRepository = yardRepository;
        this.equineRepository = equineRepository;
    }

    public List<Yard> getAllYards() {
        return yardRepository.findAll();
    }

    public Yard getYard(Long id) {
        return yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + id));
    }

    public Yard createYard(Yard yard){
        return yardRepository.saveAndFlush(yard);
    }

    public Yard updateYard(Long id, Yard updatedYardValues) {
        Yard yardToUpdate = yardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + id));
        BeanUtils.copyProperties(updatedYardValues, yardToUpdate, "id");
        return yardRepository.saveAndFlush(yardToUpdate);
    }

    public void deleteYard(Long id) throws EntityNotFoundException {
        Yard yard = yardRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("No yard found with id: 1"));
        for(Equine equine : yard.getEquines()) {
            yard.removeEquine(equine);
            equine.setYard(null);
            equineRepository.saveAndFlush(equine);
        }
        yardRepository.saveAndFlush(yard);
        yardRepository.deleteById(id);
    }
}