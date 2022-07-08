package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.mockito.ArgumentMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = YardController.class)
class YardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YardService yardService;

    private List<Yard> yardList;

    @BeforeEach
    void setUp() {
        this.yardList = new ArrayList<>();
        yardList.add(new Yard(1L, "Test Yard 1", new HashSet<>()));
        yardList.add(new Yard(2L, "Test Yard 2", new HashSet<>()));
        yardList.add(new Yard(3L, "Test Yard 3", new HashSet<>()));
        yardList.add(new Yard(4L, "Test Yard 4", new HashSet<>()));
        yardList.add(new Yard(5L, "Test Yard 5", new HashSet<>()));
    }

    @Test
    void canFindYardAndReturnOkResponse() throws Exception {
        final Long yardId = 1L;

        BDDMockito.given(yardService.getYard(yardId)).willReturn(Optional.ofNullable(yardList.get(0)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/yards/{id}", yardId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(yardList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(yardList.get(0).getName()));
    }

    @Test
    void willReturnYardNotFoundResponse() throws Exception {
        final Long invalidId = 6L;

        BDDMockito.given(yardService.getYard(invalidId)).willReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/yards/{id}", invalidId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void canFindAllYardsAndReturnOkResponse() throws Exception {
        BDDMockito.given(yardService.getAllYards()).willReturn(yardList);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/yards"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(yardList.size()));
    }

    @Test
    void canCreateYardAndReturnOkResponse() throws Exception {
        BDDMockito.given(yardService.createYard(ArgumentMatchers.any(Yard.class))).willAnswer((invocation -> invocation.getArgument(0)));

        Yard newYard = new Yard(1L, "Test New Yard", new HashSet<>());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/yards")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newYard)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newYard.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newYard.getName()));
    }

    @Test
    void canUpdateYardAndReturnOkResponse() throws Exception {
        Yard updatedYard = new Yard(1L, "Updated Yard", new HashSet<>());

        BDDMockito.given(yardService.updateYard(updatedYard.getId(), updatedYard)).willReturn(updatedYard);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/yards/{id}", updatedYard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedYard)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedYard.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedYard.getName()));
    }

    @Test
    void willReturnNotFoundResponseWhenYardWasNotFoundAndUpdated() throws Exception {
        Yard updatedYard = new Yard(9L, "Invalid Updated Yard", new HashSet<>());

        BDDMockito.given(yardService.updateYard(updatedYard.getId(), updatedYard)).willThrow(new EntityNotFoundException());

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/yards/{id}", updatedYard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedYard)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void canDeleteYardAndReturnOkResponse() throws Exception {
        final Long yardId = 1L;
        Yard yard = yardList.get(0);

        BDDMockito.given(yardService.getYard(yardId)).willReturn(Optional.of(yard));
        Mockito.doNothing().when(yardService).deleteYard(yardId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/yards/{id}", yardId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(yard.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(yard.getName()));
    }

    @Test
    void willReturnNotFoundResponseWhenYardWasNotFoundAndDeleted() throws Exception {
        final Long invalidYardId = 1L;
        Yard yard = yardList.get(0);

        BDDMockito.given(yardService.getYard(invalidYardId)).willReturn(Optional.empty());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/yards/{id}", invalidYardId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}