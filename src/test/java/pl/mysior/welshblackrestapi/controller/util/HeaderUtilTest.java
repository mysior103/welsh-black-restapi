package pl.mysior.welshblackrestapi.controller.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeaderUtilTest {

    @Test
    public void createAlert_ShouldReturnHeader(){
        HttpHeaders header = HeaderUtil.createAlert("message","param");
        Assert.assertEquals(header.get("X-welsh-black-restapi-alert").toString(),"[message]");
        Assert.assertEquals(header.get("X-welsh-black-restapi-params").toString(),"[param]");

    }

    @Test
    public void createEntityCreationAlert_ShouldReturnHeader(){
        HttpHeaders header = HeaderUtil.createEntityCreationAlert("message","param");

        Assert.assertTrue(header.get("X-welsh-black-restapi-alert").toString().contains(".created"));
        Assert.assertEquals(header.get("X-welsh-black-restapi-params").toString(),"[param]");
    }
    @Test
    public void createEntityUpdateAlert_ShouldReturnHeader(){
        HttpHeaders header = HeaderUtil.createEntityUpdateAlert("message","param");
        Assert.assertTrue(header.get("X-welsh-black-restapi-alert").toString().contains(".updated"));
        Assert.assertEquals(header.get("X-welsh-black-restapi-params").toString(),"[param]");
    }

    @Test
    public void createEntityDeletionAlert_ShouldReturnHeader(){
        HttpHeaders header = HeaderUtil.createEntityDeletionAlert("message","param");
        Assert.assertTrue(header.get("X-welsh-black-restapi-alert").toString().contains(".deleted"));
        Assert.assertEquals(header.get("X-welsh-black-restapi-params").toString(),"[param]");
    }

    @Test
    public void createFailureAlert_ShouldReturnHeader(){
        HttpHeaders header = HeaderUtil.createFailureAlert("name","errorKey","message");
        Assert.assertTrue(header.get("X-welsh-black-restapi-error").toString().contains("error." + "errorKey"));
        Assert.assertEquals(header.get("X-welsh-black-restapi-params").toString(),"[name]");
    }

}
