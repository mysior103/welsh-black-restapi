package pl.mysior.welshblackrestapi.IntegrationTests;

import com.mongodb.Mongo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;

import java.util.GregorianCalendar;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class CowControllerIT {

    private Cow cow;
    @Autowired
    private CowService cowService;

    @Test
    public void save() {
        cow = new Cow("PL123", "imie", new GregorianCalendar(2017, 10, 3), "1324", "13245", "M", "Brazowy", true);
        System.out.println(cow.toString());
        Cow c = cowService.save(cow);
        System.out.println(c.toString());
        Assert.assertEquals(c.getNumber(),cow.getNumber());
    }
}
