package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EstrusServiceTest {

    private Estrus estrus1;
    private Estrus estrus2;
    private Estrus estrus3;
    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private EstrusService estrusService;

    @Before
    public void before(){
        estrus1 = new Estrus("PL123", LocalDate.of(2015,4,5));
        estrus2 = new Estrus("PL1234", LocalDate.of(2016,7,6));
        estrus3 = new Estrus("PL123", LocalDate.of(2017,1,1));
        cow1 = new Cow("PL123", "imie", LocalDate.of(2018, 5, 1), "1324", "13245", "M", "Brazowy", true);
        cow2 = new Cow("PL1234", "imie2", LocalDate.of(2014, 5, 4), "1324", "13245", "M", "Brazowy", true);
    }
    @Test
    public void save_ShouldReturnNullIfCowDoesNotExist() {
        doReturn(Optional.empty()).when((cowRepository)).findById(any(String.class));
        Cow result = estrusService.save(estrus1);
        assertNull(result);
    }

    @Test
    public void save_ShouldCreateNewListIfEstrusDoesNotExistAndReturnCow() {
        doReturn(Optional.of(cow1)).when((cowRepository)).findById(any(String.class));
        Cow result = estrusService.save(estrus1);
        assertEquals(result.getEstruses().size(), 1);
    }

    @Test
    public void save_ShouldReturnCowWithSavedEstrusIfExistAtLeastOneEstrusInCow() {
        cow1.setEstruses(new ArrayList<>(Arrays.asList(estrus1)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(any(String.class));
        Cow results = estrusService.save(estrus3);
        assertEquals(results.getEstruses().size(), 2);
    }

    @Test
    public void findAll_ShouldReturnListOfAllEstruses() {
        cow1.setEstruses(new ArrayList<>(Collections.singletonList(estrus1)));
        cow2.setEstruses(new ArrayList<>(Collections.singletonList(estrus2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Estrus> result = estrusService.findAll();
        assertEquals(result.size(), 2);
    }

    @Test
    public void findAll_ShouldReturnOrderedListOfAllEstruses() {
        cow1.setEstruses(new ArrayList<>(Collections.singletonList(estrus1)));
        cow2.setEstruses(new ArrayList<>(Collections.singletonList(estrus2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Estrus> result = estrusService.findAll();
        assertTrue(result.get(0).getEstrusDate().isBefore(result.get(1).getEstrusDate()));
    }

    @Test
    public void findLast_ShouldReturnLastEstrusOfCow() {
        cow1.setEstruses(new ArrayList<>(Collections.singletonList(estrus1)));
        cow2.setEstruses(new ArrayList<>(Collections.singletonList(estrus2)));
        cow1.getEstruses().add(estrus3);

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Estrus> result = estrusService.findLast();
        assertEquals(result.get(0).getEstrusDate(), estrus3.getEstrusDate());
        assertEquals(result.get(1).getEstrusDate(), estrus2.getEstrusDate());
    }

    @Test
    public void findByCow_shouldReturnOrderedListOfAllEstrusesOfSpecificCow(){
        cow1.setEstruses(new ArrayList<>(Collections.singletonList(estrus1)));
        cow1.getEstruses().add(estrus3);
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        List<Estrus> result = estrusService.findByCow(cow1.getNumber());
        assertEquals(result.get(0).getEstrusDate(), estrus1.getEstrusDate());
        assertEquals(result.get(1).getEstrusDate(), estrus3.getEstrusDate());
    }
}
