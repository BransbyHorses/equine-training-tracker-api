package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.api.services.CategoryService;
import co.uk.bransby.equinetrainingtrackerapi.api.services.ProgrammeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = ProgrammeController.class)
class ProgrammeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProgrammeService programmeService;

    private List<Programme> programmes;

    @BeforeEach
    void setUp() {
        this.programmes = new ArrayList<>();
        programmes.add(new Programme(1L, "Test Programme 1", new HashSet<>()));
        programmes.add(new Programme(2L, "Test Programme 2", new HashSet<>()));
        programmes.add(new Programme(3L, "Test Programme 3", new HashSet<>()));
        programmes.add(new Programme(4L, "Test Programme 4", new HashSet<>()));
        programmes.add(new Programme(5L, "Test Programme 5", new HashSet<>()));
    }

    @Test
    void canFindAllProgrammesAndReturnOkResponse() throws Exception {
        given(programmeService.getAllProgrammes()).willReturn(programmes);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/programmes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(programmes.size()));
    }

    @Test
    void canFindProgrammeAndReturnOkResponse() throws Exception {
        given(programmeService.getProgramme(1L)).willReturn(programmes.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/programmes/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(programmes.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(programmes.get(0).getName()));
    }

    @Test
    void canCreateProgrammeAndReturnOkResponse() throws Exception {
        Programme newProgramme = new Programme(6L, "Test Programme 6", new HashSet<>());
        given(programmeService.createProgramme(newProgramme)).willReturn(newProgramme);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/programmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newProgramme)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Programme 6"));
    }

    @Test
    void canUpdateProgrammeAndReturnOkResponse() throws Exception {
        Programme updatedProgramme = new Programme(1L, "Test Programme 1 Updated", new HashSet<>());
        given(programmeService.updateProgramme(1L, updatedProgramme)).willReturn(updatedProgramme);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/programmes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProgramme)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Programme 1 Updated"));
    }

    @Test
    void canDeleteProgramme() throws Exception {
        given(programmeService.getProgramme(1L)).willReturn(programmes.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/programmes/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}