package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EquineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = EquineController.class)
class EquineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquineService equineService;

    private List<Equine> equineList;

    @BeforeEach
    void setUp() {
        this.equineList = new ArrayList<>();
        equineList.add(new Equine(1L, "First Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>()));
        equineList.add(new Equine(2L, "Second Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>()));
        equineList.add(new Equine(3L, "Third Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>()));
        equineList.add(new Equine(4L, "Fourth Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>()));
        equineList.add(new Equine(5L, "Fifth Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void findEquineById() throws Exception {
        final Long equineId = 1L;
        BDDMockito.given(equineService.getEquine(equineId)).willReturn(equineList.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/{id}", equineId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(equineList.get(0).getId()));
    }

    @Test
    void findAllEquines() throws Exception{
        BDDMockito.given(equineService.getAllEquines()).willReturn(equineList);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(equineList.size()));
    }

    @Test
    void createEquine() throws Exception {
        BDDMockito.given(equineService.createEquine(ArgumentMatchers.any(Equine.class))).willAnswer((invocation -> invocation.getArgument(0)));

        Equine newEquine = new Equine(6L, "Sixth Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newEquine)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newEquine.getId()));
    }

    @Test
    void updateEquine() throws Exception {
        Equine EquineToUpdate = new Equine(1L, "Updated Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>());
        BDDMockito.given(equineService.updateEquine(EquineToUpdate.getId(), EquineToUpdate)).willReturn(EquineToUpdate);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equines/{id}", EquineToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(EquineToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(EquineToUpdate.getId()));
    }

    @Test
    void canFindEquineTrainingProgrammesAndReturnOkResponse() throws Exception {
        TrainingProgramme trainingProgramme1 = new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), null, null);

        BDDMockito.given(equineService.getEquineTrainingProgrammes(1L)).willReturn(new ArrayList<>(
                List.of(trainingProgramme1)
        ));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/training-programmes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect((MockMvcResultMatchers.jsonPath("$.[0].id").value(1L)));
    }

    @Test
    void deleteEquine() throws Exception {
        Long equineId = 1L;
        Equine equine = equineList.get(0);

        BDDMockito.given(equineService.getEquine(equineId)).willReturn(equine);
        doNothing().when(equineService).deleteEquine(equineId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/equines/{id}", equineId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void returnsNotFoundIfEquineAbsentWhenUpdated() throws Exception {
        Equine invalidEquine = new Equine(9L, "Invalid Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>());

        BDDMockito.given(equineService.updateEquine(invalidEquine.getId(), invalidEquine)).willThrow((new EntityNotFoundException()));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equines/{id}", invalidEquine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidEquine)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willAssignEquineToYardAndReturnOkResponse() throws Exception {
        BDDMockito.given(equineService.assignEquineToYard(1L, 1L))
                .willReturn(new Equine());
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/data/equines/1/yards/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void willReturnNotFoundResponseAndNotFoundMessageWhenEquineOrYardEntityWasNotFound() throws Exception {
        BDDMockito.given(equineService.assignEquineToYard(1L, 1L))
                .willThrow(new EntityNotFoundException("No yard found with id: 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/data/equines/1/yards/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No yard found with id: 1"));
    }

    @Test
    void willGetEquineHealthAndSafetyFlagsAndReturnOkResponse() throws Exception {
        HealthAndSafetyFlag healthAndSafetyFlag = new HealthAndSafetyFlag(1L, "", new Equine());
        BDDMockito.given(equineService.getEquineHealthAndSafetyFlags(1L)).willReturn(new ArrayList<>(List.of(healthAndSafetyFlag)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/health-and-safety-flags"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
    }

    @Test
    void willReturnActiveTrainingProgrammeAndOkResponse() throws Exception {
        TrainingProgramme activeTrainingProgramme = new TrainingProgramme();
        activeTrainingProgramme.setId(1L);
        BDDMockito.given(equineService.getActiveTrainingProgramme(1L)).willReturn(activeTrainingProgramme);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/training-programmes/latest"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void willReturnNoActiveTrainingProgrammeAndNoContentResponse() throws Exception {
        BDDMockito.given(equineService.getActiveTrainingProgramme(1L)).willReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/training-programmes/latest"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    void willReturnAllSkillTrainingSessionsAndOkResponse() throws Exception {
        SkillTrainingSession skillTrainingSession1 = new SkillTrainingSession();
        skillTrainingSession1.setId(1L);
        SkillTrainingSession skillTrainingSession2 = new SkillTrainingSession();
        skillTrainingSession2.setId(2L);
        List<SkillTrainingSession> skillTrainingSessions = new ArrayList<>(List.of(skillTrainingSession1, skillTrainingSession2));
        BDDMockito.given(equineService.getEquineSkillTrainingSessions(1L))
                .willReturn(skillTrainingSessions);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/skill-training-sessions"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect((MockMvcResultMatchers.jsonPath("$.[0].id").value(1L)))
                .andExpect((MockMvcResultMatchers.jsonPath("$.[1].id").value(2L)));
    }

    @Test
    void willLogNewDisruptionAndReturnOkResponse() throws Exception {
        Disruption newDisruption = new Disruption();
        newDisruption.setId(1L);
        newDisruption.setReason(DisruptionCode.YARD_BUSY);
        BDDMockito.given(equineService.logNewDisruption(1, 1L)).willReturn(newDisruption);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines/1/disruptions/1/start"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason.string").value("Yard Busy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").isString());
    }
}