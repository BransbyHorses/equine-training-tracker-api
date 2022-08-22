package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Method;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.MethodDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.MethodService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/methods")
public class MethodController {

    private final MethodService methodService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<MethodDto>> getMethods() {
        List<MethodDto> allMethods = methodService.listMethods()
                .stream()
                .map(method -> modelMapper.map(method, MethodDto.class))
                .toList();
        return ResponseEntity
                .ok()
                .body(allMethods);
    }

    @GetMapping("{id}")
    public ResponseEntity<MethodDto> getMethod(@PathVariable Long id) {
        Method method = methodService.listMethod(id);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(method, MethodDto.class));
    }

    @PostMapping
    public ResponseEntity<MethodDto> createMethod(@RequestBody Method method) {
        Method createdMethod = methodService.createMethod(method);
        return ResponseEntity
                .created(URI.create("/data/methods/" + createdMethod.getId()))
                .body(modelMapper.map(createdMethod, MethodDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<MethodDto> updateMethod(@PathVariable Long id, @RequestBody Method updatedMethodValues) {
        Method updatedMethod = methodService.updateMethod(id, updatedMethodValues);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedMethod, MethodDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteMethod(@PathVariable Long id) {
        methodService.deleteMethod(id);
        return ResponseEntity
                .ok()
                .body(id);
    }

}
