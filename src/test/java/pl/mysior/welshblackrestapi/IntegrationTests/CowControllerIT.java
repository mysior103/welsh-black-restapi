package pl.mysior.welshblackrestapi.IntegrationTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.repository.CowRepository;
import pl.mysior.welshblackrestapi.services.CowService;
import pl.mysior.welshblackrestapi.services.Impl.CowServiceImpl;


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
