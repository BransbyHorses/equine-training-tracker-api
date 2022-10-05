package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingProgrammeServiceTest {

    @Mock
    TrainingProgrammeRepository trainingProgrammeRepository;
    @Mock
    SkillRepository skillRepository;
    @Mock
    SkillTrainingSessionRepository skillTrainingSessionRepository;
    @Mock
    SkillProgressRecordRepository skillProgressRecordRepository;
    @Mock
    EquineRepository equineRepository;
    @InjectMocks
    TrainingProgrammeService trainingProgrammeService;

    List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.trainingProgrammeService = new TrainingProgrammeService(trainingProgrammeRepository, skillRepository, skillTrainingSessionRepository, skillProgressRecordRepository, equineRepository);
        this.trainingProgrammes = new ArrayList<>(List.of(
                new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(2L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(3L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(4L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(5L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now())
        ));
    }

    @Test
    void canGetAllProgrammes() {
        given(trainingProgrammeRepository.findAll()).willReturn(trainingProgrammes);
        List<TrainingProgramme> returnedTrainingProgrammes = trainingProgrammeService.getAllProgrammes();
        assertEquals(trainingProgrammes, returnedTrainingProgrammes);
    }

    @Test
    void canGetProgrammeById() {
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = trainingProgrammeService.getProgramme(1L);
        assertNotNull(trainingProgramme);
        assertEquals(trainingProgrammes.get(0).getId(), trainingProgramme.getId());
    }

    @Test
    void canCreateFirstTrainingProgramme() {
        Equine testEquine = new Equine();
        testEquine.setId(1L);
        testEquine.setTrainingProgrammes(new ArrayList<>());

        Skill skill1 = new Skill(1L, "Skill 1");
        Skill skill2 = new Skill(1L, "Skill 2");

        TrainingProgramme newTrainingProgramme = new TrainingProgramme();
        newTrainingProgramme.setId(1L);
        newTrainingProgramme.setTrainingCategory(new TrainingCategory(1L, "", ""));
        newTrainingProgramme.setEquine(testEquine);
        newTrainingProgramme.setSkillTrainingSessions(new ArrayList<>());
        newTrainingProgramme.setSkillProgressRecords(new ArrayList<>());
        newTrainingProgramme.setStartDate(null);
        newTrainingProgramme.setEndDate(null);

        given(equineRepository.getById(1L)).willReturn(testEquine);
        given(trainingProgrammeRepository.saveAndFlush(newTrainingProgramme)).willReturn(newTrainingProgramme);
        given(skillRepository.findAll()).willReturn(new ArrayList<>(List.of(skill1, skill2)));

        TrainingProgramme savedTrainingProgramme = trainingProgrammeService.createProgramme(newTrainingProgramme);

        assertEquals(1L, savedTrainingProgramme.getId());
        assertEquals(2, savedTrainingProgramme.getSkillProgressRecords().size());
        assertEquals("Skill 1", savedTrainingProgramme.getSkillProgressRecords().get(0).getSkill().getName());
        assertEquals("Skill 2", savedTrainingProgramme.getSkillProgressRecords().get(1).getSkill().getName());
        assertEquals(ProgressCode.NOT_ABLE, savedTrainingProgramme.getSkillProgressRecords().get(0).getProgressCode());
        assertEquals(ProgressCode.NOT_ABLE, savedTrainingProgramme.getSkillProgressRecords().get(1).getProgressCode());
        assertEquals(0, savedTrainingProgramme.getSkillProgressRecords().get(0).getTime());
        assertEquals(0, savedTrainingProgramme.getSkillProgressRecords().get(1).getTime());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(0).getStartDate());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(0).getEndDate());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(1).getStartDate());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(1).getEndDate());
    }

    @Test
    void canCreateNewTrainingProgrammeAndWillTransferSkillProgressRecords() {
        // setup
        Skill testSkill = new Skill(1L, "Skill 1");

        SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
        Equine testEquine = new Equine();
        TrainingProgramme oldTrainingProgramme = new TrainingProgramme();
        TrainingProgramme newTrainingProgramme = new TrainingProgramme();

        skillProgressRecord.setId(1L);
        skillProgressRecord.setTrainingProgramme(oldTrainingProgramme);
        skillProgressRecord.setSkill(testSkill);
        skillProgressRecord.setProgressCode(ProgressCode.OK_WITH_LIMITS);
        skillProgressRecord.setStartDate(LocalDateTime.of(2022, 9, 20, 10, 30));
        skillProgressRecord.setEndDate(null);
        skillProgressRecord.setTime(35);

        oldTrainingProgramme.setId(2L);
        oldTrainingProgramme.setEquine(testEquine);
        oldTrainingProgramme.setSkillTrainingSessions(new ArrayList<>());
        oldTrainingProgramme.setSkillProgressRecords(new ArrayList<>(List.of(skillProgressRecord)));
        oldTrainingProgramme.setStartDate(LocalDateTime.of(2022, 9, 12, 21, 30));
        oldTrainingProgramme.setEndDate(null);

        testEquine.setId(1L);
        testEquine.setTrainingProgrammes(new ArrayList<>(List.of(oldTrainingProgramme)));

        newTrainingProgramme.setId(2L);
        newTrainingProgramme.setEquine(testEquine);
        newTrainingProgramme.setSkillTrainingSessions(new ArrayList<>());
        newTrainingProgramme.setSkillProgressRecords(new ArrayList<>());
        newTrainingProgramme.setStartDate(null);
        newTrainingProgramme.setEndDate(null);

        given(equineRepository.getById(1L)).willReturn(testEquine);
        given(trainingProgrammeRepository.saveAndFlush(newTrainingProgramme)).willReturn(newTrainingProgramme);

        TrainingProgramme savedTrainingProgramme = trainingProgrammeService.createProgramme(newTrainingProgramme);

        assertEquals(2L, savedTrainingProgramme.getId());
        assertEquals(1, savedTrainingProgramme.getSkillProgressRecords().size());
        assertNotNull(oldTrainingProgramme.getEndDate());
        assertEquals("Skill 1", savedTrainingProgramme.getSkillProgressRecords().get(0).getSkill().getName());
        assertEquals(ProgressCode.OK_WITH_LIMITS, savedTrainingProgramme.getSkillProgressRecords().get(0).getProgressCode());
        assertEquals(0, savedTrainingProgramme.getSkillProgressRecords().get(0).getTime());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(0).getStartDate());
    }


    @Test
    void canAddSkillTrainingSessionToTrainingProgramme() {
        Skill skill = new Skill(1L, "Test Skill");
        TrainingProgramme trainingProgramme = new TrainingProgramme();
        trainingProgramme.setId(1L);
        trainingProgramme.setSkillTrainingSessions(new ArrayList<>());
        trainingProgramme.setSkillProgressRecords(new ArrayList<>());
        trainingProgramme.setStartDate(null);

        SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
        skillProgressRecord.setTrainingProgramme(trainingProgramme);
        skillProgressRecord.setSkill(skill);
        skillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
        skillProgressRecord.setStartDate(null);
        skillProgressRecord.setTime(0);

        SkillTrainingSession skillTrainingSession = new SkillTrainingSession();
        skillTrainingSession.setDate(LocalDateTime.now());
        skillTrainingSession.setSkill(skill);
        skillTrainingSession.setTrainingMethod(new TrainingMethod());
        skillTrainingSession.setEnvironment(new TrainingEnvironment());
        skillTrainingSession.setProgressCode(ProgressCode.CONFIDENT);
        skillTrainingSession.setTrainingTime(10);

        trainingProgramme.addSkillProgressRecord(skillProgressRecord);

        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.of(trainingProgramme));
        given(skillTrainingSessionRepository.saveAndFlush(skillTrainingSession)).willReturn(skillTrainingSession);

        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService
                .addSkillTrainingSessionToTrainingProgramme(1L, skillTrainingSession);

        assertEquals(skillTrainingSession, updatedTrainingProgramme.getSkillTrainingSessions().get(0));
        assertEquals(10, updatedTrainingProgramme.getSkillProgressRecords().get(0).getTime());
        assertEquals(ProgressCode.CONFIDENT, updatedTrainingProgramme.getSkillProgressRecords().get(0).getProgressCode());
        assertNotNull(updatedTrainingProgramme.getStartDate());
    }

}