package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.*;
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
import java.util.HashSet;
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
        equineList.add(new Equine(1L, "First Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>()));
        equineList.add(new Equine(2L, "Second Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>()));
        equineList.add(new Equine(3L, "Third Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>()));
        equineList.add(new Equine(4L, "Fourth Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>()));
        equineList.add(new Equine(5L, "Fifth Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>()));
    }

    @Test
    void findEquineById() throws Exception {
        final Long equineId = 1L;

        BDDMockito.given(equineService.getEquine(equineId)).willReturn(Optional.ofNullable(equineList.get(0)));

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

        Equine newEquine = new Equine(6L, "Sixth Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equines")
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newEquine)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newEquine.getId()));
    }

    @Test
    void updateEquine() throws Exception {
        Equine EquineToUpdate = new Equine(1L, "Updated Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>());

        BDDMockito.given(equineService.updateEquine(EquineToUpdate.getId(), EquineToUpdate)).willReturn(EquineToUpdate);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equines/{id}", EquineToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(EquineToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(EquineToUpdate.getId()));
    }


    @Test
    void deleteEquine() throws Exception {
        Long equineId = 1L;
        Equine equine = equineList.get(0);

        BDDMockito.given(equineService.getEquine(equineId)).willReturn(Optional.of(equine));
        Mockito.doNothing().when(equineService).deleteEquine(equineId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/equines/{id}", equineId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(equine.getId()));

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
        Equine invalidEquine = new Equine(9L, "Invalid Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>());

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