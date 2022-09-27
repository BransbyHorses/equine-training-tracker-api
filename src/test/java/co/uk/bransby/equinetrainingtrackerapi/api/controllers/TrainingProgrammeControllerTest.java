package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.SkillTrainingSessionDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingProgrammeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = TrainingProgrammeController.class)
class TrainingProgrammeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainingProgrammeService trainingProgrammeService;

    @MockBean
    ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    private List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.trainingProgrammes = new ArrayList<>();
        trainingProgrammes.add(new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        trainingProgrammes.add(new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        trainingProgrammes.add(new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        trainingProgrammes.add(new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        trainingProgrammes.add(new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void canFindAllProgrammesAndReturnOkResponse() throws Exception {
        given(trainingProgrammeService.getAllProgrammes()).willReturn(trainingProgrammes);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/programmes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(trainingProgrammes.size()));
    }

    @Test
    void canFindProgrammeAndReturnOkResponse() throws Exception {
        given(trainingProgrammeService.getProgramme(1L)).willReturn(trainingProgrammes.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/training-programmes/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(trainingProgrammes.get(0).getId()));
    }

    @Test
    void canCreateProgrammeAndReturnOkResponse() throws Exception {
        TrainingProgramme newProgramme = new TrainingProgramme(6L,  new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null);
        given(trainingProgrammeService.createProgramme(newProgramme)).willReturn(newProgramme);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/training-programmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newProgramme)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L));
    }

    @Test
    void canUpdateProgrammeAndReturnOkResponse() throws Exception {
        TrainingProgramme updatedProgramme = new TrainingProgramme(1L,  new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null);
        given(trainingProgrammeService.updateProgramme(1L, updatedProgramme)).willReturn(updatedProgramme);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/training-programmes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedProgramme)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void canDeleteProgramme() throws Exception {
        given(trainingProgrammeService.getProgramme(1L)).willReturn(trainingProgrammes.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/training-programmes/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void canAddSkillTrainingSessionToTrainingProgrammeAndReturnOkResponse() throws Exception {
        TrainingProgramme trainingProgramme = new TrainingProgramme(1L,  new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        SkillTrainingSession skillTrainingSession = new SkillTrainingSession();
        skillTrainingSession.setId(1L);
        skillTrainingSession.setSkill(new Skill());
        skillTrainingSession.setTrainingMethod(new TrainingMethod());
        skillTrainingSession.setDate(LocalDateTime.now());
        skillTrainingSession.setProgressCode(ProgressCode.NOT_ABLE);
        skillTrainingSession.setEnvironment(new TrainingEnvironment());
        skillTrainingSession.setTrainingTime(10);
        skillTrainingSession.setNotes("");

        given(trainingProgrammeService.addSkillTrainingSessionToTrainingProgramme(
                1L,
                skillTrainingSession)
        ).willReturn(trainingProgramme);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/training-programmes/{id}/skill-training-session", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(skillTrainingSession)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // TODO - test json body
    }

}