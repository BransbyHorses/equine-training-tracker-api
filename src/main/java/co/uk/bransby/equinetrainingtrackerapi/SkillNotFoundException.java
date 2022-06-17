package co.uk.bransby.equinetrainingtrackerapi;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(Long id) {
        super("Could not find skill " + id);
    }

    public SkillNotFoundException(String name) {
        super("Could not find skill " + name);
    }


}
