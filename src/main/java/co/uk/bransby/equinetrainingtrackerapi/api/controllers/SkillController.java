package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.SkillDto;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.api.services.SkillService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/skills")
public class SkillController {

    private final SkillService skillService;
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<SkillDto>> all() {
        HttpHeaders headers = new HttpHeaders();
        List<SkillDto> allSkills = skillService.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();

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