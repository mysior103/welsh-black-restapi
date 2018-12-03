package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.exception.CowNotFoundException;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CowServiceTest {

    private Cow cow1;
    private Cow cow2;

    @MockBean
    private CowRepository cowRepository;

    @Autowired
    private CowService cowService;

    @Before
    public void before() {

        cow1 = TestObjectFactory.Cow("PL123");
        cow2 = TestObjectFactory.Cow("PL1234");
       }

    @Test
    public void save_shouldCreateAndReturnCreatedCowObject() {
//        when(cowRepository.save(Mockito.any(Cow.class))).thenReturn(cow1);
//        given(cowService.save(CowMapper.toDto(cow1))).will;
//        Assert.assertEquals(cow1.getNumber(), result.getNumber());
    }

    @Test
    public void findAll_shouldReturnAllCows() {
        when(cowRepository.findAll()).thenReturn(Arrays.asList(cow1, cow2));
        List all = cowService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void findByNumber_whenExistShouldReturnObject() throws CowNotFoundException {
        doReturn(Optional.of(cow1)).when(cowRepository).findById(cow1.getNumber());
        CowDTO result = cowService.findByNumber(cow1.getNumber());
        assertEquals(cow1.getNumber(), result.getNumber());
    }

    @Test(expected = CowNotFoundException.class)
    public void findByNumber_whenNotExistShouldReturnException() throws CowNotFoundException {
        doReturn(Optional.empty()).when(cowRepository).findById(any(String.class));
        CowDTO result = cowService.findByNumber(this.cow1.getNumber());
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

}
