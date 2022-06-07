package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.SkillNotFoundException;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.persistence.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Skill> all() {

       return skillService.findAll();
    }

    @PostMapping
    public Skill addSkill(@RequestBody Skill skill) {

        return skillService.save(skill);
    }

    @GetMapping("{id}")
    public Skill getById(@PathVariable Long id) {
        return skillService.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));
    }

    @PutMapping("{id}")
    public Skill updateSkill(@RequestBody Skill newSkill, @PathVariable Long id) {

        return skillService.findById(id)
                .map(skill -> {
                    skill.setName(newSkill.getName());
                    return skillService.save(skill);
                })
                .orElseGet(() -> {
                    newSkill.setId(id);
                    return skillService.save(newSkill);
                });
    }

    @DeleteMapping("{id}")
    public void deleteSkill(@PathVariable Long id) {
         skillService.deleteById(id);

    }



}
