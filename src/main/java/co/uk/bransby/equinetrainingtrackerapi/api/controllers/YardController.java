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
        return yardService.getYard(id)
                .map(yard -> new ResponseEntity<>(modelMapper.map(yard, YardDto.class), resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<YardDto>> findAllYards() {
        List<YardDto> allYards = yardService.getAllYards().stream()
                .map(yard -> modelMapper.map(yard, YardDto.class)).toList();
        System.out.println(allYards);
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
        try {
            Yard updatedYard =  yardService.updateYard(id, modelMapper.map(updatedYardValues, Yard.class));
            return new ResponseEntity<YardDto>(modelMapper.map(updatedYard, YardDto.class), resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return yardService.getYard(id)
                .map(yard -> {
                    yardService.deleteYard(id);
                    return new ResponseEntity<YardDto>(modelMapper.map(yard, YardDto.class), resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
}
