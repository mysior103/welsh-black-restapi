package pl.mysior.welshblackrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.TestObjectFactory;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

}
