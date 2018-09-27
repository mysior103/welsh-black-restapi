package pl.mysior.welshblackrestapi.services;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.mysior.welshblackrestapi.security.user.ApplicationUser;
import pl.mysior.welshblackrestapi.repository.ApplicationUserRepository;
import pl.mysior.welshblackrestapi.security.user.UserServiceImpl;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private ApplicationUserRepository repository;

    @Autowired
    private UserServiceImpl userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameShouldThrowExceptionIfNotExist(){
        Mockito.when(repository.findByUsername(any(String.class))).thenReturn(null);
        userService.loadUserByUsername("user");
    }
    @Test
    public void loadUserByUsernameShouldRetrnUserDetails(){
        ApplicationUser user = new ApplicationUser();
        user.setUsername("user");
        user.setPassword("password");
        Mockito.when(repository.findByUsername(user.getUsername())).thenReturn(user);
        User userDetails = new User(user.getUsername(),user.getPassword(), Collections.emptyList());
        Assert.assertEquals(userDetails.getUsername(),userService.loadUserByUsername(user.getUsername()).getUsername());
    }
}
