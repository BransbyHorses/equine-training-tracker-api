package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingEnvironment;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EnvironmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingEnvironmentServiceTest {

    @Mock
    EnvironmentRepository environmentRepository;

    @InjectMocks
    EnvironmentService environmentService;

    TrainingEnvironment environmentInstance;

    @BeforeEach
    void setUp() {
        environmentService = new EnvironmentService(environmentRepository);
        environmentInstance = new TrainingEnvironment(1L, "Test Environment");
    }

    @Test
    void willGetEnvironments() {
        given(environmentRepository.findAll()).willReturn(
                new ArrayList<>(List.of(environmentInstance))
        );
        List<TrainingEnvironment> environments = environmentService.getEnvironments();
        assertEquals(1, environments.size());
        assertEquals(new ArrayList<>(List.of(environmentInstance)), environments);
    }

    @Test
    void willGetEnvironment() {
        given(environmentRepository.findById(1L)).willReturn(Optional.of(environmentInstance));
        TrainingEnvironment environmentFromId = environmentService.getEnvironment(1L);
        assertEquals(environmentInstance, environmentFromId);
    }

    @Test
    void willCreateEnvironment() {
        TrainingEnvironment newEnvironment = new TrainingEnvironment(2L, "New Environment");
        given(environmentRepository.saveAndFlush(environmentInstance)).willReturn(newEnvironment);
        TrainingEnvironment environment = environmentService.createEnvironment(environmentInstance);
        assertEquals("New Environment", environment.getName());
    }

    @Test
    void updateEnvironment() {
        TrainingEnvironment updatedEnvironmentValues = new TrainingEnvironment(1L, "Updated Environment");
        given(environmentRepository.findById(1L)).willReturn(Optional.ofNullable(environmentInstance));
        TrainingEnvironment updatedEnvironment = environmentService.updateEnvironment(1L, updatedEnvironmentValues);
        assertEquals("Updated Environment", updatedEnvironment.getName());
    }

    @Test
    void deleteEnvironment() {
        environmentService.deleteEnvironment(1L);
        Mockito.verify(environmentRepository).deleteById(1L);
    }
}