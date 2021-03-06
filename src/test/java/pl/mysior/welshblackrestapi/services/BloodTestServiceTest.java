package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
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
public class BloodTestServiceTest {

    private BloodTest bloodTest1;
    private BloodTest bloodTest2;
    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private CowActionService<BloodTest> bloodTestService;

    @Before
    public void before() {
        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
        bloodTest1 = new BloodTest("PL123", true, LocalDate.of(2016, 11, 14));
        bloodTest2 = new BloodTest("PL1234", true, LocalDate.of(2017, 10, 24));
    }

    @Test
    public void save_shouldCreateNewListAndReturnCowWithSavedBloodTest() {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(bloodTest1.getCowNumber());
        Cow result = bloodTestService.save(bloodTest1);
        assertEquals(result.getBloodTests().get(0).getActionDate(), bloodTest1.getActionDate());
    }

    @Test
    public void save_shouldAddBloodTestToListAndReturnCowWithSavedBloodTest() {
        BloodTest bloodTest3 = new BloodTest("PL123", false, LocalDate.of(2019, 8, 8));
        cow1.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest3)));
        doReturn(Optional.of(cow1)).when(cowRepository).findById(bloodTest1.getCowNumber());
        Cow result = bloodTestService.save(bloodTest1);
        assertEquals(result.getBloodTests().size(), 2);
    }

    @Test
    public void save_shouldReturnNullWhenCowDoesNotExist() {
        doReturn(Optional.empty()).when(cowRepository).findById(bloodTest1.getCowNumber());
        Cow result = bloodTestService.save(bloodTest1);
        assertNull(result);
    }

    @Test
    public void findAll_ShouldReturnEmptyListWhenLackOfBloodTests(){
        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<BloodTest> result = bloodTestService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnListOfAllBloodTests() {
        cow1.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest1)));
        cow2.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<BloodTest> result = bloodTestService.findAll();
        assertEquals(result.size(), 2);
    }

    @Test
    public void findAll_ShouldReturnOrderedListOfAllBloodTests() {
        cow1.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest1)));
        cow2.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest2)));

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<BloodTest> result = bloodTestService.findAll();
        assertTrue(result.get(0).getActionDate().isBefore(result.get(1).getActionDate()));
    }

    @Test
    public void findLast_ShouldReturnLastBloodTestOfCow() {
        cow1.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest1)));
        cow2.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest2)));
        BloodTest bloodTest3 = new BloodTest("PL123", false, LocalDate.of(2019, 8, 8));
        cow1.getBloodTests().add(bloodTest3);

        doReturn(new ArrayList<>(Arrays.asList(cow1, cow2))).when(cowRepository).findAll();
        List<BloodTest> result = bloodTestService.findLast();
        assertEquals(result.get(0).getActionDate(), bloodTest3.getActionDate());
        assertEquals(result.get(1).getActionDate(), bloodTest2.getActionDate());
    }

    @Test
    public void findByCow_ShouldReturnEmptyListWhenLackOfBloodTestsForSpecificCow(){
        doReturn(Optional.of(cow1)).when(cowRepository).findById(any(String.class));
        List<BloodTest> result = bloodTestService.findByCow(cow1.getNumber());
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByCow_ShouldReturnOrderedListOfAllBloodTestsOfSpecificCow() {
        cow1.setBloodTests(new ArrayList<>(Collections.singletonList(bloodTest1)));
        BloodTest bloodTest3 = new BloodTest("PL123", false, LocalDate.of(2019, 8, 8));
        cow1.getBloodTests().add(bloodTest3);
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        List<BloodTest> result = bloodTestService.findByCow(cow1.getNumber());
        assertEquals(result.get(0).getActionDate(), bloodTest1.getActionDate());
        assertEquals(result.get(1).getActionDate(), bloodTest3.getActionDate());
    }
}
