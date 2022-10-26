package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    @Mock
    TrainingCategoryRepository trainingCategoryRepository;
    @InjectMocks
    TrainingProgrammeService trainingProgrammeService;

    List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.trainingProgrammeService = new TrainingProgrammeService(trainingProgrammeRepository, skillRepository, skillTrainingSessionRepository, skillProgressRecordRepository, equineRepository, trainingCategoryRepository);
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
        Equine equine = new Equine();
        equine.setId(1L);
        TrainingCategory trainingCategory = new TrainingCategory(1L, "Training Category");
        TrainingProgramme newTrainingProgramme = new TrainingProgramme();

        List<SkillProgressRecord> skillProgressRecords = new ArrayList<>();

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        given(trainingCategoryRepository.findById(1L)).willReturn(Optional.of(trainingCategory));
        when(trainingProgrammeService.createNewSkillProgressRecords(newTrainingProgramme)).thenReturn(skillProgressRecords);

        TrainingProgramme savedTrainingProgramme = trainingProgrammeService.createProgramme(1L, 1L);

        assertEquals(trainingCategory, savedTrainingProgramme.getTrainingCategory());
        assertEquals(skillProgressRecords, savedTrainingProgramme.getSkillProgressRecords());
        assertEquals(equine, savedTrainingProgramme.getEquine());
        assertNull(savedTrainingProgramme.getStartDate());
        assertNotNull(savedTrainingProgramme.getCreatedOn());
        assertNull(savedTrainingProgramme.getEndDate());
    }

    @Test
    void canCreateNewTrainingProgramme() {
        TrainingProgramme oldTrainingProgramme = new TrainingProgramme();
        Skill skill = new Skill(1L, "Skill");
        SkillProgressRecord skillProgressRecord = new SkillProgressRecord(
                1L,
                oldTrainingProgramme,
                skill,
                ProgressCode.CONFIDENT,
                LocalDateTime.of(2022,9,9, 7, 30),
                null,
                15
        );
        oldTrainingProgramme.setSkillProgressRecords(new ArrayList<>(List.of(skillProgressRecord)));

        Equine equine = new Equine();
        equine.setId(1L);
        equine.setTrainingProgrammes(new ArrayList<>(List.of(oldTrainingProgramme)));

        TrainingCategory trainingCategory = new TrainingCategory(1L, "Training Category");

        TrainingProgramme newTrainingProgramme = new TrainingProgramme();

        List<SkillProgressRecord> skillProgressRecords = new ArrayList<>();

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        given(trainingCategoryRepository.findById(1L)).willReturn(Optional.of(trainingCategory));
        lenient().when(trainingProgrammeService.transferSkillProgressRecords(oldTrainingProgramme, newTrainingProgramme)).thenReturn(skillProgressRecords);

        TrainingProgramme savedTrainingProgramme = trainingProgrammeService.createProgramme(1L, 1L);

        assertNotNull(oldTrainingProgramme.getEndDate());

        assertEquals(1, savedTrainingProgramme.getSkillProgressRecords().size());
        assertEquals(ProgressCode.CONFIDENT, savedTrainingProgramme.getSkillProgressRecords().get(0).getProgressCode());
        assertEquals(0, savedTrainingProgramme.getSkillProgressRecords().get(0).getTime());
        assertEquals(skill, savedTrainingProgramme.getSkillProgressRecords().get(0).getSkill());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(0).getEndDate());
        assertNull(savedTrainingProgramme.getSkillProgressRecords().get(0).getStartDate());
        assertNull(savedTrainingProgramme.getEndDate());
        assertNull(savedTrainingProgramme.getStartDate());
        assertNotNull(savedTrainingProgramme.getCreatedOn());
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

    @Test
    void willCreatNewSkillProgressRecords() {
        TrainingProgramme newTrainingProgramme = new TrainingProgramme();
        newTrainingProgramme.setId(1L);
        Skill skill1 = new Skill(1L, "");

        given(skillRepository.findAll()).willReturn(List.of(skill1));

        List<SkillProgressRecord> newSkillProgressRecords = trainingProgrammeService.createNewSkillProgressRecords(newTrainingProgramme);

        assertEquals(1, newSkillProgressRecords.size());
        assertEquals(skill1, newSkillProgressRecords.get(0).getSkill());
        assertEquals(newTrainingProgramme, newSkillProgressRecords.get(0).getTrainingProgramme());
        assertEquals(0, newSkillProgressRecords.get(0).getTime());
        assertEquals(ProgressCode.NOT_ABLE, newSkillProgressRecords.get(0).getProgressCode());
        assertNull(newSkillProgressRecords.get(0).getStartDate());
        assertNull(newSkillProgressRecords.get(0).getEndDate());
    }

    @Test
    void willTransferSkillProgressRecords() {
        TrainingProgramme oldTrainingProgramme = new TrainingProgramme();
        oldTrainingProgramme.setId(1L);
        TrainingProgramme newTrainingProgramme = new TrainingProgramme();
        newTrainingProgramme.setId(2L);
        Skill skill1 = new Skill(1L, "");

        SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
        skillProgressRecord.setSkill(skill1);
        skillProgressRecord.setProgressCode(ProgressCode.OK);
        skillProgressRecord.setTrainingProgramme(oldTrainingProgramme);

        oldTrainingProgramme.setSkillProgressRecords(List.of(skillProgressRecord));

        List<SkillProgressRecord> newSkillProgressRecords = trainingProgrammeService.transferSkillProgressRecords(
                oldTrainingProgramme, newTrainingProgramme
        );

        assertEquals(1, newSkillProgressRecords.size());
        assertEquals(skill1, newSkillProgressRecords.get(0).getSkill());
        assertEquals(newTrainingProgramme, newSkillProgressRecords.get(0).getTrainingProgramme());
        assertEquals(ProgressCode.OK, newSkillProgressRecords.get(0).getProgressCode());
        assertNull(newSkillProgressRecords.get(0).getStartDate());
        assertNull(newSkillProgressRecords.get(0).getEndDate());

    }

}