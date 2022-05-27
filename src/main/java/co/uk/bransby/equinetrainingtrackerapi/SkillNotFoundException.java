package co.uk.bransby.equinetrainingtrackerapi;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(Long id) {
        super("Could not find skill " + id);
    }

}
