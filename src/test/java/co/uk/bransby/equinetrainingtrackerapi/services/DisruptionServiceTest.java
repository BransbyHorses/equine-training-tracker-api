package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.repositories.DisruptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        BDDMockito.given(disruptionRepository.findById(1L)).willReturn(Optional.of(updatedDisruption));
        disruptionServiceUnderTest.updateDisruption(1L, updatedDisruption);
        Mockito.verify(disruptionRepository).saveAndFlush(updatedDisruption);
    }

    @Test
    void willThrowExceptionWhenDisruptionWasNotFoundAndUpdated() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> disruptionServiceUnderTest.updateDisruption(1L, new Disruption()));
    }

    @Test
    void deleteDisruption() {
        disruptionServiceUnderTest.deleteDisruption(1L);
        Mockito.verify(disruptionRepository).deleteById(1L);
    }
}