package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.SkillNotFoundException;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.persistence.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("skills")
public class SkillController {

    @Autowired
    private final SkillRepository skillRepository;

    SkillController(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @GetMapping("/cat")
    public String catFact() {

        return "meow";
    }

    @GetMapping
    public List<Skill> all() {

       return skillRepository.findAll();
    }

    @PostMapping
    public Skill addSkill(@RequestBody Skill skill) {

        return skillRepository.save(skill);
    }

    @GetMapping("{id}")
    public Skill getById(@PathVariable Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));
    }

    @PutMapping("{id}")
    public Skill updateSkill(@RequestBody Skill newSkill, @PathVariable Long id) {

        return skillRepository.findById(id)
                .map(skill -> {
                    skill.setName(newSkill.getName());
                    return skillRepository.save(skill);
                })
                .orElseGet(() -> {
                    newSkill.setId(id);
                    return skillRepository.save(newSkill);
                });
    }

    @DeleteMapping("{id}")
    public void deleteSkill(@PathVariable Long id) {
         skillRepository.deleteById(id);

    }

}
