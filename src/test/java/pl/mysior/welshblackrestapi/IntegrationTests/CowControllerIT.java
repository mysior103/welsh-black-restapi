package pl.mysior.welshblackrestapi.IntegrationTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ActiveProfiles("test")
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class CowControllerIT {

    @Test
    public void save() {
        Assert.assertTrue(true);
    }
}
