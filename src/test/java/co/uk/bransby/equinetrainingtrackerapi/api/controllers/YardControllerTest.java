package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.api.services.YardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

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
        given(yardService.getYard(yardId)).willReturn(yardList.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/yards/{id}", yardId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(yardList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(yardList.get(0).getName()));
    }

    @Test
    void canFindAllYardsAndReturnOkResponse() throws Exception {
        given(yardService.getAllYards()).willReturn(yardList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/yards"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(yardList.size()));
    }

    @Test
    void canCreateYardAndReturnOkResponse() throws Exception {
        given(yardService.createYard(ArgumentMatchers.any(Yard.class))).willAnswer((invocation -> invocation.getArgument(0)));
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

        given(yardService.updateYard(updatedYard.getId(), updatedYard)).willReturn(updatedYard);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/yards/{id}", updatedYard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedYard)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedYard.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedYard.getName()));
    }

    @Test
    void canDeleteYardAndReturnOkResponse() throws Exception {
        final Long yardId = 1L;
        Yard yard = yardList.get(0);
        given(yardService.getYard(yardId)).willReturn(yard);
        doNothing().when(yardService).deleteYard(yardId);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/yards/{id}", yardId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}