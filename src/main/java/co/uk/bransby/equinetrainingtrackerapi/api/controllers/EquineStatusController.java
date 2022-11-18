package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/equine-statuses")
public class EquineStatusController {

    @GetMapping
    public ResponseEntity<List<EquineStatus>> getEquineStatuses() {
        return new ResponseEntity<>(Arrays.asList(EquineStatus.values()), HttpStatus.OK);
    }

}
