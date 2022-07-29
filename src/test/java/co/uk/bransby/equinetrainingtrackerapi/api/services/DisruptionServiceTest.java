package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.DisruptionRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.services.DisruptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DisruptionServiceTest {

    @Mock
    DisruptionRepository disruptionRepository;

    @InjectMocks
    private DisruptionService disruptionServiceUnderTest;

    @BeforeEach
    void setUp() {
        disruptionServiceUnderTest = new DisruptionService(disruptionRepository);
    }

    @Test
    void canGetAllDisruptions() {
        disruptionServiceUnderTest.getDisruptions();
        Mockito.verify(disruptionRepository).findAll();
    }

    @Test
    void canGetDisruptionFromId() {
        given(disruptionRepository.findById(1L)).willReturn(Optional.of(new Disruption()));
        disruptionServiceUnderTest.getDisruption(1L);
        Mockito.verify(disruptionRepository).findById(1L);
    }

    @Test
    void canCreateDisruption() {
        Disruption newDisruption = new Disruption();
        disruptionServiceUnderTest.createDisruption(newDisruption);
        Mockito.verify(disruptionRepository).saveAndFlush(newDisruption);
    }

    @Test
    void canUpdateDisruption() {
        Disruption updatedDisruption = new Disruption();
        given(disruptionRepository.findById(1L)).willReturn(Optional.of(updatedDisruption));
        disruptionServiceUnderTest.updateDisruption(1L, updatedDisruption);
        Mockito.verify(disruptionRepository).saveAndFlush(updatedDisruption);
    }

    @Test
    void willThrowExceptionWhenDisruptionWasNotFoundAndUpdated() {
        Disruption disruption = new Disruption();
        Assertions.assertThrows(EntityNotFoundException.class, () -> disruptionServiceUnderTest.updateDisruption(1L, disruption));
    }

    @Test
    void deleteDisruption() {
        given(disruptionRepository.findById(1L)).willReturn(Optional.of(new Disruption()));
        disruptionServiceUnderTest.deleteDisruption(1L);
        Mockito.verify(disruptionRepository).deleteById(1L);
    }
}