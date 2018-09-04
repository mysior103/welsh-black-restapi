package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.model.*;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CowServiceTest {

    private Cow c;
    private BloodTest bloodTest;
    private Comment comment;
    private Deworming deworming;
    private Estrus estrus;
    private Vaccine vaccine;


    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private CowService cowService;

    @Before
    public void before() {

        c = new Cow("PL123", "imie", LocalDate.of(2018, 5, 1), "1324", "13245", "M", "Brazowy", true);
    }

    @Test
    public void save_shouldCreateAndReturnCreatedCowObject() {
        when(cowRepository.save(Mockito.any(Cow.class))).thenReturn(c);
        Cow result = cowService.save(c);
        Assert.assertEquals(c.getNumber(), result.getNumber());
    }

    @Test
    public void findAll_shouldReturnAllCows() {
        Cow c1 = new Cow("PL123", "imie", LocalDate.of(2017, 10, 3), "1324", "13245", "M", "Brazowy", true);
        when(cowRepository.findAll()).thenReturn(Arrays.asList(c, c1));
        List all = cowService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void findByNumber_whenExistShouldReturnObject() {
        doReturn(c).when(cowRepository).findById(c.getNumber());
        Cow result = cowService.findByNumber(c.getNumber());
        assertEquals(c.getNumber(), result.getNumber());
    }

    @Test
    public void findByNumber_whenNotExistShouldReturnNull() throws NullPointerException {
        doReturn(null).when(cowRepository).findById(any(String.class));
        Cow result = cowService.findByNumber(c.getNumber());
        assertNull(result);
    }

    @Test
    public void deleteByNumber_whenNotExistShouldReturnNull() {
        doReturn(null).when(cowRepository).deleteById(any(String.class));
        Cow result = cowService.deleteByNumber(c.getNumber());
        assertNull(result);
    }

    @Test
    public void deleteByNumber_whenExistShouldReturnObject() {
        doReturn(c).when(cowRepository).deleteById(c.getNumber());
        Cow result = cowService.deleteByNumber(c.getNumber());
        assertEquals(c.getNumber(), result.getNumber());
    }

}
