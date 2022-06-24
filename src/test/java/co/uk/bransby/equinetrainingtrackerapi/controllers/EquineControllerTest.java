package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.services.EquineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

@WebMvcTest(controllers = EquineController.class)
@ExtendWith(MockitoExtension.class)
class EquineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquineService equineService;

    private List<Equine> equineList;

    @BeforeEach
    void setUp() {
        this.equineList = new ArrayList<>();
        equineList.add(new Equine(1L, "First Horse", "First Yard", 1L, "RED", "First programme", "First skill", "First training", true));
        equineList.add(new Equine(2L, "Second Horse", "Second Yard", 2L, "GREEN", "Second programme", "Second skill", "Second training", false));
        equineList.add(new Equine(3L, "Third Horse", "Third Yard", 3L, "AMBER", "Third programme", "Third skill", "Third training", true));
        equineList.add(new Equine(4L, "Fourth Horse", "Fourth Yard", 4L, "GREEN", "Fourth programme", "Fourth skill", "Fourth training", false));
        equineList.add(new Equine(5L, "Fifth Horse", "Fifth Yard", 5L, "AMBER", "Fifth programme", "Fifth skill", "Fifth training", true));
    }


    @Test
    void findEquineById() throws Exception {
        final Long equineId = 1L;

        BDDMockito.given(equineService.getEquine(equineId)).willReturn(Optional.ofNullable(equineList.get(0)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/{id}", equineId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(equineList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(equineList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yard").value(equineList.get(0).getYard()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trainerId").value(equineList.get(0).getTrainerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(equineList.get(0).getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programme").value(equineList.get(0).getProgramme()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skills").value(equineList.get(0).getSkills()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.training").value(equineList.get(0).getTraining()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.onHold").value(equineList.get(0).getOnHold()));

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

        Equine newEquine = new Equine(6L, "Sixth Horse", "Sixth Yard", 6L, "RED", "Sixth programme", "Sixth skill", "Sixth training", false);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newEquine)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newEquine.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newEquine.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yard").value(newEquine.getYard()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trainerId").value(newEquine.getTrainerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(newEquine.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programme").value(newEquine.getProgramme()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skills").value(newEquine.getSkills()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.training").value(newEquine.getTraining()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.onHold").value(newEquine.getOnHold()));
    }

    @Test
    void updateEquine() throws Exception {
        Equine EquineToUpdate = new Equine(1L, "Updated Horse", "First Yard", 1L, "BLUE", "First programme", "First skill", "First training", true);

        BDDMockito.given(equineService.updateEquine(EquineToUpdate.getId(), EquineToUpdate)).willReturn(EquineToUpdate);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equines/{id}", EquineToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(EquineToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(EquineToUpdate.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(EquineToUpdate.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yard").value(EquineToUpdate.getYard()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trainerId").value(EquineToUpdate.getTrainerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(EquineToUpdate.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programme").value(EquineToUpdate.getProgramme()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skills").value(EquineToUpdate.getSkills()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.training").value(EquineToUpdate.getTraining()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.onHold").value(EquineToUpdate.getOnHold()));

    }


    @Test
    void deleteEquine() throws Exception {
        Long equineId = 1L;
        Equine equine = equineList.get(0);

        BDDMockito.given(equineService.getEquine(equineId)).willReturn(Optional.of(equine));
        Mockito.doNothing().when(equineService).deleteEquine(equineId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/equines/{id}", equineId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(equine.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(equine.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yard").value(equine.getYard()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trainerId").value(equine.getTrainerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(equine.getCategory()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.programme").value(equine.getProgramme()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skills").value(equine.getSkills()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.training").value(equine.getTraining()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.onHold").value(equine.getOnHold()));

    }

    @Test
    void returnsNotFoundIfEquineAbsent() throws  Exception {
        final Long invalidEquineId = 7L;

        BDDMockito.given(equineService.getEquine(invalidEquineId)).willReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equines/{id}", invalidEquineId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void returnsNotFoundIfEquineAbsentWhenUpdated() throws Exception {
        Equine invalidEquine = new Equine(9L, "Invalid Horse", "First Yard", 1L, "BLUE", "First programme", "First skill", "First training", true);

        BDDMockito.given(equineService.updateEquine(invalidEquine.getId(), invalidEquine)).willThrow((new EntityNotFoundException()));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equines/{id}", invalidEquine.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(invalidEquine)))
                .andExpect((MockMvcResultMatchers.status().isNotFound()));
    }

    @Test
    void returnsNotFoundIfEquineAbsentWhenDeleted() throws Exception {
        final Long invalidEquineId = 7L;

        BDDMockito.given(equineService.getEquine(invalidEquineId)).willReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/equines/{id}", invalidEquineId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}