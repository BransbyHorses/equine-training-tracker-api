package co.uk.bransby.equinetrainingtrackerapi;

public class SkillCreationException extends RuntimeException {
    public SkillCreationException(String name) {
        super("Skill " + name +" already exists");
    }

}
