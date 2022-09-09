package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.YardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class YardServiceTest {

    @Mock YardRepository yardRepository;
    @Mock
    EquineRepository equineRepository;
    @InjectMocks
    private YardService yardServiceUnderTest;
    private Yard yardInstance;

    @BeforeEach
    void setUp() {
        yardServiceUnderTest = new YardService(yardRepository, equineRepository);
        yardInstance = new Yard(1L, "Test Yard Instance", new HashSet<>());
    }

    @Test
    void canGetAllYards() {
        yardServiceUnderTest.getAllYards();
        Mockito.verify(yardRepository).findAll();
    }

    @Test
    void canGetYard() {
        given(yardRepository.findById(1L)).willReturn(Optional.ofNullable(yardInstance));
        yardServiceUnderTest.getYard(1L);
        Mockito.verify(yardRepository).findById(yardInstance.getId());
    }

    @Test
    void canCreateYard() {
        yardServiceUnderTest.createYard(yardInstance);
        Mockito.verify(yardRepository).saveAndFlush(yardInstance);
    }

    @Test
    void canUpdateYard() {
        given(yardRepository.findById(1L)).willReturn(Optional.ofNullable(yardInstance));
        yardInstance.setName("New Yard Name");
        yardServiceUnderTest.updateYard(1L, yardInstance);
        Mockito.verify(yardRepository).saveAndFlush(yardInstance);
    }

    @Test
    void throwsExceptionWhenYardWasNotFoundAndUpdated() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> yardServiceUnderTest.updateYard(yardInstance.getId(), yardInstance));
    }

    @Test
    void canDeleteYard() {
        Equine equine = new Equine(1L, "First Horse", yardInstance, new Category(), List.of(new TrainingProgramme()));
        yardInstance.setEquines(new HashSet<>(List.of(equine)));
        // given
        given(yardRepository.findById(1L)).willReturn(Optional.of(yardInstance));
        // when
        yardServiceUnderTest.deleteYard(yardInstance.getId());
        // then
        Assertions.assertNull(equine.getYard());
        Mockito.verify(yardRepository).deleteById(yardInstance.getId());
    }
}