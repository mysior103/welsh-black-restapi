package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DewormingServiceTest {

    private Deworming deworming1;
    private Deworming deworming2;
    private Deworming deworming3;
    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private DewormingService dewormingService;

    @Before
    public void before() {
        cow1 = new Cow("PL123", "imie", LocalDate.of(2018, 5, 1), "1324", "13245", "M", "Brazowy", true);
        cow2 = new Cow("PL1234", "imie2", LocalDate.of(2014, 5, 4), "1324", "13245", "M", "Brazowy", true);
        deworming1 = new Deworming("PL123", 100, LocalDate.of(2013, 6, 7));
        deworming2 = new Deworming("PL1234", 200, LocalDate.of(2014, 6, 7));
        deworming3 = new Deworming("PL123", 200, LocalDate.of(2015, 6, 7));
    }

    @Test
    public void save_ShouldReturnNullIfCowDoesNotExist() {
        doReturn(Optional.empty()).when((cowRepository)).findById(any(String.class));
        Cow result = dewormingService.save(deworming1);
        assertNull(result);
    }

    @Test
    public void save_ShouldCreateNewListIfNoDewormingsDoesNotExistAndReturnCowWithDeworming() {
        doReturn(Optional.of(cow1)).when((cowRepository)).findById(any(String.class));
        Cow result = dewormingService.save(deworming1);
        assertEquals(result.getDewormings().size(), 1);
    }

    @Test
    public void save_ShouldReturnCowWithSavedDewormingIfExistAtLeastOneDewormingInCow() {
        cow1.setDewormings(new ArrayList<>(Arrays.asList(deworming1)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(deworming1.getCowNumber());
        Cow results = dewormingService.save(deworming3);
        assertEquals(results.getDewormings().size(), 2);
    }

    @Test
    public void findAll_ShouldReturnEmptyListIfNoDewormingsInCows() {
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Deworming> result = dewormingService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnListOfAllDewormings() {
        cow1.setDewormings(new ArrayList<>(Arrays.asList(deworming1)));
        cow2.setDewormings(new ArrayList<>(Arrays.asList(deworming2)));
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Deworming> result = dewormingService.findAll();
        assertEquals(result.size(), 2);
    }

    @Test
    public void findAll_ShouldReturnOrderedListOfAllDewormings() {
        cow1.setDewormings(new ArrayList<>(Arrays.asList(deworming1)));
        cow2.setDewormings(new ArrayList<>(Arrays.asList(deworming2)));
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<Deworming> result = dewormingService.findAll();
        assertTrue(result.get(0).getDewormingDate().isAfter(result.get(1).getDewormingDate()));
    }

    @Test
    public void findByCow_ShouldReturnNullIfCowDoesNotExist() {
        doReturn(Optional.empty()).when(cowRepository).findById(any(String.class));
        List<Deworming> result = dewormingService.findByCow("PL123");
        assertNull(result);
    }

    @Test
    public void findByCow_ShouldReturnEmptyListIfDewormingDoesNotExist() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(any(String.class));
        List<Deworming> result = dewormingService.findByCow(cow1.getNumber());
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByCow_ShouldReturnListOfDewormings() {
        cow1.setDewormings(new ArrayList<>(Arrays.asList(deworming1, deworming3)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(any(String.class));
        List<Deworming> result = dewormingService.findByCow(cow1.getNumber());
        assertEquals(result.size(),2);
    }


}
