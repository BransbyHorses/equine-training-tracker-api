package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.YardDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.YardService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@RestController
@RequestMapping("/data/yards")
public class YardController {

    private final YardService yardService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<YardDto> findYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        Yard yard = yardService.getYard(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(modelMapper.map(yard, YardDto.class));
    }

    @GetMapping
    public ResponseEntity<List<YardDto>> findAllYards() {
        List<YardDto> allYards = yardService.getAllYards().stream()
                .map(yard -> modelMapper.map(yard, YardDto.class)).toList();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allYards, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<YardDto> createYard(@RequestBody YardDto newYard) {
        Yard createdYard = yardService.createYard(modelMapper.map(newYard, Yard.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(createdYard, YardDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateYard(@PathVariable Long id, @RequestBody YardDto updatedYardValues) {
        HttpHeaders resHeaders = new HttpHeaders();
            Yard updatedYard =  yardService.updateYard(id, modelMapper.map(updatedYardValues, Yard.class));
            return ResponseEntity
                    .ok()
                    .headers(resHeaders)
                    .body(modelMapper.map(updatedYard, YardDto.class));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        yardService.deleteYard(id);
        return ResponseEntity.ok().headers(resHeaders).build();
    }
}
