package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.SkillNotFoundException;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/skills")
public class SkillController {

    @Autowired
    private final SkillService skillService;

    SkillController(SkillService skillService) {
        this.skillService = skillService;
    }


    @GetMapping
    public ResponseEntity<List<Skill>> all() {
        HttpHeaders headers = new HttpHeaders();
        List<Skill> allSkills = skillService.findAll();
       return ResponseEntity
               .status(HttpStatus.OK)
               .headers(headers)
               .body(allSkills);
    }

    @GetMapping("{id}")
    public ResponseEntity<Skill> getById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        return skillService.findById(id)
                .map(skill -> ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(skill))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .headers(headers).build());
    }

    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        HttpHeaders headers = new HttpHeaders();
        Skill newSkill = skillService.save(skill);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(newSkill);
    }



    @PutMapping("{id}")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill, @PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        Skill updatedSkill = skillService.update(skill, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(updatedSkill);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Skill> deleteSkillById(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        return skillService.findById(id)
                .map(skill -> {
                    skillService.deleteById(id);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .headers(headers)
                            .body(skill);
                        })
                .orElse(ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .headers(headers)
                            .build());

    }

}
