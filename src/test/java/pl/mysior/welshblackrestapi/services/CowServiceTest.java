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
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.repository.CowRepository;


import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CowServiceTest {

    private Cow cow1;
    private Cow cow2;
    private static final LocalDate TODAY = LocalDate.of(2018,10,2);
    private Clock fixedClock = Clock.fixed(LocalDateTime.of(TODAY.getYear(),TODAY.getMonth(),TODAY.getDayOfMonth(),0,0).toInstant(OffsetDateTime.now().getOffset()),Clock.systemDefaultZone().getZone());


    @MockBean
    private CowRepository cowRepository;

    @MockBean
    private CowActionService<Estrus> estrusService;

    @Mock
    private Clock clock;

    @Autowired
    private CowService cowService;

    @Before
    public void before() {

        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");

        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
       }

    @Test
    public void save_shouldCreateAndReturnCreatedCowObject() {
        when(cowRepository.save(Mockito.any(Cow.class))).thenReturn(cow1);
        Cow result = cowService.save(cow1);
        Assert.assertEquals(cow1.getNumber(), result.getNumber());
    }

    @Test
    public void findAll_shouldReturnAllCows() {
        when(cowRepository.findAll()).thenReturn(Arrays.asList(cow1, cow2));
        List all = cowService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void findByNumber_whenExistShouldReturnObject() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        Cow result = cowService.findByNumber(cow1.getNumber());
        assertEquals(cow1.getNumber(), result.getNumber());
    }

    @Test
    public void findByNumber_whenNotExistShouldReturnNull(){
        doReturn(Optional.empty()).when(cowRepository).findById(any(String.class));
        Cow result =cowService.findByNumber(this.cow1.getNumber());
        assertNull(result);
    }

    @Test
    public void deleteByNumber_whenNotExistShouldReturnNull() {
        doReturn(Optional.empty()).when(cowRepository).findById(any(String.class));
        Cow result = cowService.deleteByNumber(cow1.getNumber());
        assertNull(result);
    }

    @Test
    public void deleteByNumber_whenExistShouldReturnObject() {
        Optional<Cow> cowOptional = Optional.of(cow1);
        doReturn(cowOptional).when(cowRepository).findById(cow1.getNumber());
        Cow result = cowService.deleteByNumber(cow1.getNumber());
        assertEquals(cow1.getNumber(), result.getNumber());
    }

    @Test
    public void findNearestBirthForAll_shouldReturnListOfNearestBirth(){
        Estrus estrus1 = new Estrus(cow1.getNumber(),LocalDate.of(2018,4,5));
        Estrus estrus2 = new Estrus(cow2.getNumber(),LocalDate.of(2018,5,5));

        when(estrusService.findLast()).thenReturn(Arrays.asList(estrus1,estrus2));
        List<String> results = cowService.findNearestBirthForAll();



        results.forEach(System.out::print);

        assertEquals(results.get(0),estrus1.getCowNumber() + " " + estrus1.getActionDate().toString());
    }

    @Test
    public void findNearestBirthForCow_shouldReturnPredictedBirthDate(){
        LocalDate estrusDate1 = LocalDate.of(2018,4,5);
        LocalDate estrusDate2 = LocalDate.of(2017,4,5);
        Estrus estrus1 = new Estrus(cow1.getNumber(),estrusDate1);
        Estrus estrus2 = new Estrus(cow1.getNumber(),estrusDate2);

        when(estrusService.findByCow(cow1.getNumber())).thenReturn(Arrays.asList(estrus1,estrus2));

        LocalDate predictedBirth = cowService.findNearestBirthForCow(cow1.getNumber());

        assertEquals(predictedBirth,estrusDate1);
    }

}
