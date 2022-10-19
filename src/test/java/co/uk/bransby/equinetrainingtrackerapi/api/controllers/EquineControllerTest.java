package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.HealthAndSafetyFlagDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EquineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.BDDAssumptions.given;
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
        equineList.add(new Equine(1L, "First Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>()));
        equineList.add(new Equine(2L, "Second Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>()));
        equineList.add(new Equine(3L, "Third Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>()));
        equineList.add(new Equine(4L, "Fourth Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>()));
        equineList.add(new Equine(5L, "Fifth Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>()));
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

        Equine newEquine = new Equine(6L, "Sixth Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newEquine)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newEquine.getId()));
    }

    @Test
    void updateEquine() throws Exception {
        Equine EquineToUpdate = new Equine(1L, "Updated Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>());

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
        Equine invalidEquine = new Equine(9L, "Invalid Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>());

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
    void willAssignEquineToCategoryAndReturnOkResponse() throws Exception {
        BDDMockito.given(equineService.assignEquineToCategory(1L, 1L))
                .willReturn(new Equine());
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/data/equines/1/categories/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void willReturnNotFoundResponseAndNotFoundMessageWhenEquineOrCategoryEntityWasNotFound() throws Exception {
        BDDMockito.given(equineService.assignEquineToCategory(1L, 1L))
                .willThrow(new EntityNotFoundException("No category found with id: 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/data/equines/1/categories/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No category found with id: 1"));
    }

    @Test
    void willCreateHealthAndSafetyFlagAndReturnCreatedResponse() throws Exception {
       HealthAndSafetyFlag healthAndSafetyFlag = new HealthAndSafetyFlag();
       healthAndSafetyFlag.setId(1L);
       healthAndSafetyFlag.setContent("Content");
        BDDMockito.given(equineService.createEquineHealthAndSafetyFlag(1L, healthAndSafetyFlag)).willReturn(healthAndSafetyFlag);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines/1/health-and-safety-flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(healthAndSafetyFlag)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void willGetEquineHealthAndSafetyFlagsAndReturnOkResponse() throws Exception {
        HealthAndSafetyFlag healthAndSafetyFlag = new HealthAndSafetyFlag(1L, "", new Equine());
        BDDMockito.given(equineService.getEquineHealthAndSafetyFlags(1L)).willReturn(new ArrayList<>(List.of(healthAndSafetyFlag)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/1/health-and-safety-flags"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));
    }
}