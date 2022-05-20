package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.repositories.YardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YardService {

    private final YardRepository yardRepository;

    public YardService(YardRepository yardRepository) {
        this.yardRepository = yardRepository;
    }

    // read all yards from the database
    public List<Yard> readAllYards() {
        return yardRepository.findAll();
    }
    // read a specified yard from the database
    public Optional<Yard> readYard(Long id) {
        return yardRepository.findById(id);
    }
    // create a new yard in the database
    public Yard createYard(Yard yard){
        return yardRepository.saveAndFlush(yard);
    }
    // update a yard in the database
    public Yard updateYard(Long id, Yard updatedYard) {
        Optional<Yard> yardFromDb = yardRepository.findById(id);
        if(yardFromDb.isPresent()) {
            BeanUtils.copyProperties(updatedYard, yardFromDb.get(), "id");
            return yardRepository.saveAndFlush(yardFromDb.get());
        }
        return null;
    }
    // delete a yard from the database
    public boolean deleteYard(Long id) {
        try {
            yardRepository.deleteById(id);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}
