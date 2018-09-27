package pl.mysior.welshblackrestapi.controller;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.mysior.welshblackrestapi.security.user.ApplicationUser;
import pl.mysior.welshblackrestapi.repository.ApplicationUserRepository;
import pl.mysior.welshblackrestapi.security.user.UserController;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ApplicationUserRepository repository;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    private String createBodyWithUsernameAndPassword(String username, String password) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        return json.toJSONString();
    }

    private String createBodyWithUsernameAndPassword() {
        JSONObject json = new JSONObject();
        json.put("username", "username");
        json.put("password", "password");
        return json.toJSONString();
    }

    @Test
    public void signUpShouldBeNotAuthorizedAndReturnOk() throws Exception {
        ApplicationUser user = new ApplicationUser();
        user.setUsername("username");
        user.setPassword("username");
        Mockito.when(repository.save(any(ApplicationUser.class))).thenReturn(user);
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createBodyWithUsernameAndPassword()))
                .andExpect(status().isOk());
    }
}
