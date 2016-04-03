package ex.services.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.simflex.ex.dao.interfaces.UserDao;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.exceptions.UserReadingException;
import ru.simflex.ex.services.implementation.UserServiceImplementation;
import ru.simflex.ex.util.Utility;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImplementation
 */
public class UserServiceImplementationTest {

    private UserServiceImplementation userService;

    @Mock
    private UserDao userDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImplementation();
        userService.setUserDao(userDao);
    }

    @After
    public void destroy() {
        userService = null;
    }

    /**
     * Special utility method for creating users.
     */
    private User getUser(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Test
    public void authUser_Correct() {
        String email = "email@email.com";
        String password = "password";

        User expectedUser = getUser(0);
        expectedUser.setEmail(email);
        expectedUser.setPassword(Utility.getHashedPassword(password));

        when(userDao.authUser(email, Utility.getHashedPassword(password))).thenReturn(expectedUser);
        User resultUser = userService.authUser("eMaIL@email.com", password);
        assertEquals(expectedUser.getEmail(), resultUser.getEmail());
    }

    @Test
    public void authUser_Must_Return_Null_If_Incorrect_Entries() {
        String email = "email@email.com";
        String password = "incorrect_password";
        User expectedUser = getUser(0);
        expectedUser.setEmail(email);
        expectedUser.setPassword(Utility.getHashedPassword(password));

        when(userDao.authUser(email, Utility.getHashedPassword(password))).thenReturn(null);
        User resultUser = userService.authUser("eMaIL@email.com", password);
        assertEquals(null, resultUser);
    }

    @Test(expected = UserReadingException.class)
    @SuppressWarnings("unchecked")
    public void authUser_Must_Throw_Exception() {
        String email = "email@email.com";
        String password = "incorrect_password";
        when(userDao.authUser(email, Utility.getHashedPassword(password))).thenThrow(Exception.class);
        userService.authUser(email, password);
    }

}