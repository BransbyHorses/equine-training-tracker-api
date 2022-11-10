package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.ProgressCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ProgressCodeController.class)
class ProgressCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void progressCodesAndReturnOkResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(
                "/data/progress-codes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(ProgressCode.values().length));
    }
}