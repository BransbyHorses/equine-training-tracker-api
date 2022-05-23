package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.repositories.YardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class YardService {

    private final YardRepository yardRepository;

    public YardService(YardRepository yardRepository) {
        this.yardRepository = yardRepository;
    }

    public List<Yard> getAllYards() {
        return yardRepository.findAll();
    }
    public Optional<Yard> getYard(Long id) {
        return yardRepository.findById(id);
    }
    public Yard createYard(Yard yard){
        return yardRepository.saveAndFlush(yard);
    }
    public Yard updateYard(Long id, Yard updatedYardValues) throws EntityNotFoundException {
        Yard yardToUpdate = yardRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedYardValues, yardToUpdate, "id");
        return yardRepository.saveAndFlush(yardToUpdate);
    }
    public void deleteYard(Long id) {
        yardRepository.deleteById(id);
    }
}