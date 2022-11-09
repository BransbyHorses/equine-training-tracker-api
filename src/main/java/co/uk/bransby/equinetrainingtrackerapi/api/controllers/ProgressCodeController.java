package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.ProgressCode;
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
@RequestMapping("/data/progress-codes")
public class ProgressCodeController {


    @GetMapping
    public ResponseEntity<List<ProgressCode>> progressCodes() {
        return new ResponseEntity<>(Arrays.asList(ProgressCode.values()), HttpStatus.OK);
    }

}
