package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.repositories.YardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class YardServiceTest {

    @Mock YardRepository yardRepository;
    private YardService yardServiceUnderTest;

    @BeforeEach
    void setUp() {
        yardServiceUnderTest = new YardService(yardRepository);
    }

    @Test
    void canGetAllYards() {
        yardServiceUnderTest.getAllYards();
        Mockito.verify(yardRepository).findAll();
    }

    @Test
    void canGetYard() {
        Long id = 1L;
        yardServiceUnderTest.getYard(id);
        Mockito.verify(yardRepository).findById(id);
    }

    @Test
    void canCreateYard() {
        Yard newYard = new Yard();
        yardServiceUnderTest.createYard(newYard);
        Mockito.verify(yardRepository).saveAndFlush(newYard);
    }

    @Test
    void canUpdateYard() {
    }

    @Test
    void canDeleteYard() {
        Long id = 1L;
        yardServiceUnderTest.deleteYard(id);
        Mockito.verify(yardRepository).deleteById(id);
    }
}