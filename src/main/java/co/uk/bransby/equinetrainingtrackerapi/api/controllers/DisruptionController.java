package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.DisruptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/disruptions")
public class DisruptionController {


    @GetMapping
    public ResponseEntity<List<DisruptionCode>> disruptions() {
        return new ResponseEntity<List<DisruptionCode>>(Arrays.asList(DisruptionCode.values()), HttpStatus.OK);
    }

}
