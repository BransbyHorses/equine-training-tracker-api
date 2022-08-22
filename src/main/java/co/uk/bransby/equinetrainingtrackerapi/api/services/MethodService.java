package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Method;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.MethodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class MethodService {

    private final MethodRepository methodRepository;

    public List<Method> listMethods() {
        return methodRepository.findAll();
    }

    public Method listMethod(Long id) {
        return methodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No method found with id: " + id));
    }

    public Method createMethod(Method newMethod) {
        return methodRepository.saveAndFlush(newMethod);
    }

    public Method updateMethod(Long id, Method updatedMethod) {
        Method method = methodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No method found with id: " + id));
        BeanUtils.copyProperties(updatedMethod, method, "id");
        return methodRepository.saveAndFlush(method);
    }

    public void deleteMethod(Long id) {
        if(methodRepository.existsById(id)) {
            methodRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("No method found with id: " + id);
        }
    }

}
