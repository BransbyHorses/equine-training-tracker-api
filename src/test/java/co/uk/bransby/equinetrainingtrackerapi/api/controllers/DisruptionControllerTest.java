package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.controllers.DisruptionController;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.api.services.DisruptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = DisruptionController.class)
class DisruptionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DisruptionService disruptionService;

    private List<Disruption> disruptions;

    @BeforeEach
    void setUp() {
        this.disruptions = new ArrayList<>();
        disruptions.add(new Disruption(1L, "Test Disruption 1"));
        disruptions.add(new Disruption(2L, "Test Disruption 2"));
        disruptions.add(new Disruption(3L, "Test Disruption 3"));
        disruptions.add(new Disruption(4L, "Test Disruption 4"));
        disruptions.add(new Disruption(5L, "Test Disruption 5"));
    }

    @Test
    void willGetDisruptionsAndReturnOkResponse() throws Exception {
        given(disruptionService.getDisruptions()).willReturn(disruptions);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/disruptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(disruptions.size()));
    }

    @Test
    void willGetDisruptionAndReturnOkResponse() throws Exception {
        given(disruptionService.getDisruption(1L)).willReturn(Optional.of(disruptions.get(0)));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/disruptions/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Disruption 1"));
    }

    @Test
    void willReturnNotFoundResponseWhenDisruptionWasNotFound() throws Exception {
        given(disruptionService.getDisruption(6L)).willReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/disruptions/{id}", 6))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willCreateDisruptionAndReturnCreatedResponse() throws Exception {
        Disruption newDisruption =  new Disruption(6L, "New Disruption");
        given(disruptionService.createDisruption(newDisruption)).willReturn(newDisruption);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/disruptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newDisruption)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Disruption"));
    }

    @Test
    void willUpdateDisruptionAndReturnOkResponse() throws Exception {
        Disruption updatedDisruption = new Disruption(7L, "Updated Disruption");
        given(disruptionService.updateDisruption(7L, updatedDisruption)).willReturn(updatedDisruption);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/disruptions/{id}", updatedDisruption.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDisruption)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Disruption"));
    }

    @Test
    void willReturnNotFoundResponseWhenDisruptionWasNotFoundAndUpdated() throws Exception {
        Disruption updatedDisruption = new Disruption(7L, "Updated Disruption");
        given(disruptionService.updateDisruption(7L, updatedDisruption))
                .willThrow(new EntityNotFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/disruptions/{id}", updatedDisruption.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDisruption)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willDeleteDisruptionAndReturnOkResponse() throws Exception {
        given(disruptionService.getDisruption(1L)).willReturn(Optional.of(disruptions.get(0)));
        Mockito.doNothing().when(disruptionService).deleteDisruption(1L);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/disruptions/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Disruption 1"));
    }

    @Test
    void willReturnNotFoundResponseWhenDisruptionWasNotFoundAndDeleted() throws Exception {
        given(disruptionService.getDisruption(1L)).willReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/disruptions/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}