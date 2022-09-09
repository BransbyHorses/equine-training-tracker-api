package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingDayService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/data/training-days")
public class TrainingDayController {

    private final TrainingDayService trainingDayService;
    private final ModelMapper modelMapper;

//    @PostMapping("{trainingProgrammeId}")


}
