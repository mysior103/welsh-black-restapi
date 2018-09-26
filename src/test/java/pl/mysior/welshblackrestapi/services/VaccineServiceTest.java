package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Vaccine;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class VaccineServiceTest {

    private Vaccine vaccine1;
    private Vaccine vaccine2;
    private Vaccine vaccine3;
    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private VaccineService vaccineService;

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        vaccine1 = new Vaccine("PL123", LocalDate.of(2015, 4, 5));
        vaccine2 = new Vaccine("PL1234", LocalDate.of(2016, 7, 6));
        vaccine3 = new Vaccine("PL123", LocalDate.of(2017, 1, 1));
    }

    @Test
    public void save_shouldReturnNullWhenCowDoesNotExist() {
        doReturn(Optional.empty()).when(cowRepository).findById(vaccine1.getCowNumber());
        Cow result = vaccineService.save(vaccine1);
        assertNull(result);
    }

    @Test
    public void save_shouldCreateNewListAndReturnCowWithSavedVaccines() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(vaccine1.getCowNumber());
        Cow result = vaccineService.save(vaccine1);
        assertEquals(result.getVaccines().get(0).getVaccineDate(), vaccine1.getVaccineDate());
    }

    @Test
    public void save_shouldAddVaccineToListAndReturnCowWithSavedVaccine() {

        cow1.setVaccines(new ArrayList<>(Arrays.asList(vaccine3)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(vaccine1.getCowNumber());
        Cow result = vaccineService.save(vaccine1);
        assertEquals(result.getVaccines().size(), 2);
    }

    @Test
    public void findAll_ShouldReturnEmptyListIfNoVaccinesInCows() {
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Vaccine> result = vaccineService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnListOfAllVaccines() {
        cow1.setVaccines(new ArrayList<>(Collections.singletonList(vaccine1)));
        cow2.setVaccines(new ArrayList<>(Collections.singletonList(vaccine2)));
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Vaccine> result = vaccineService.findAll();
        assertEquals(result.size(), 2);
    }

    @Test
    public void findAll_ShouldReturnOrderedListOfAllVaccines() {
        cow1.setVaccines(new ArrayList<>(Collections.singletonList(vaccine1)));
        cow2.setVaccines(new ArrayList<>(Collections.singletonList(vaccine2)));
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Vaccine> result = vaccineService.findAll();
        assertTrue(result.get(0).getVaccineDate().isBefore(result.get(1).getVaccineDate()));
    }

    @Test
    public void findLast_ShouldReturnLastVaccineOfCow() {
        cow1.setVaccines(new ArrayList<>(Collections.singletonList(vaccine1)));
        cow2.setVaccines(new ArrayList<>(Collections.singletonList(vaccine2)));
        cow1.getVaccines().add(vaccine3);
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Vaccine> result = vaccineService.findLast();
        assertEquals(result.get(0).getVaccineDate(), vaccine3.getVaccineDate());
        assertEquals(result.get(1).getVaccineDate(), vaccine2.getVaccineDate());
    }

    @Test
    public void findByCow_ShouldReturnEmptyListIfVaccinesDoesNotExistForSpecificCow() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(any(String.class));
        List<Vaccine> result = vaccineService.findByCow(cow1.getNumber());
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByCow_shouldReturnOrderedListOfVaccinesOfSpecificCow() {
        cow1.setVaccines(new ArrayList<>(Collections.singletonList(vaccine1)));
        cow1.getVaccines().add(vaccine3);
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        List<Vaccine> result = vaccineService.findByCow(cow1.getNumber());
        assertEquals(result.get(0).getVaccineDate(), vaccine1.getVaccineDate());
        assertEquals(result.get(1).getVaccineDate(), vaccine3.getVaccineDate());
    }
}
