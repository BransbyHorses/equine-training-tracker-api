package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.dtos.SkillDto;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.services.SkillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/skills")
public class SkillController {

    @Autowired
    private final SkillService skillService;

    @Autowired
    private ModelMapper modelMapper;


    SkillController(SkillService skillService) {
        this.skillService = skillService;
    }


    @GetMapping
    public ResponseEntity<List<SkillDto>> all() {
        HttpHeaders headers = new HttpHeaders();
        List<SkillDto> allSkills = skillService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

       return ResponseEntity
               .status(HttpStatus.OK)
               .headers(headers)
               .body(allSkills);
    }

    @GetMapping("{id}")
    public ResponseEntity<SkillDto> getById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        return skillService.findById(id)
                .map(skill -> ResponseEntity
                            .status(HttpStatus.OK)
                            .headers(headers)
                            .body(convertToDto(skill)))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .headers(headers).build());
    }

    @PostMapping
    public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto) {
        HttpHeaders headers = new HttpHeaders();
        Skill skillEntity = convertToEntity(skillDto);
        Skill newSkill = skillService.create(skillEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(convertToDto(newSkill));
    }

    @PutMapping("{id}")
    public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skillDto, @PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        Skill skill = convertToEntity(skillDto);
        Skill updatedSkill = skillService.update(skill, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(convertToDto(updatedSkill));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SkillDto> deleteSkillById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        return skillService.findById(id)
                .map(skill -> {
                    skillService.deleteById(id);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .headers(headers)
                            .body(convertToDto(skill));
                        })
                .orElse(ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .headers(headers)
                            .build());
    }

    private Skill convertToEntity(SkillDto skillDto) {
        return modelMapper.map(skillDto, Skill.class);
    }

    private SkillDto convertToDto(Skill skill) {
        return modelMapper.map(skill, SkillDto.class);
    }

}
