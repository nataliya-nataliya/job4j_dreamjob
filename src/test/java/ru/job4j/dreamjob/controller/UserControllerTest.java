package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegisterPageThenGetRegistrationPage() {
        var view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostNewUserThenRedirectIndexPage() {
        var user = new User(0, "email@nonexistentmail.com",
                "name", "password");
        when(userService.save(any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);

        assertThat(view).isEqualTo("redirect:/index");
    }

    @Test
    public void whenSomeExceptionThrownUponExistEmailThenGetErrorPageWithMessage() {
        var user = new User(0, "email@nonexistentmail.com",
                "name", "password");
        userService.save(user);
        var expectedException = new RuntimeException("User with this email already exists");
        when(userService.save(user)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(model, new User());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestLoginPageThenGetLoginPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenPostLoginThenRedirectVacanciesPage() {
        var user = new User(0, "email@nonexistentmail.com",
                "name", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var request = new MockHttpServletRequest();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenSomeExceptionThrownUponWrongEmailAndPasswordThenGetErrorPageWithMessage() {
        var user = new User(0, "email@nonexistentmail.com",
                "name", "password");
        var expectedException = new RuntimeException("Wrong email or password");
        when(userService.findByEmailAndPassword("email@nonexistentmail.com", "wrongPass"))
                .thenThrow(expectedException);

        var model = new ConcurrentModel();
        var request = new MockHttpServletRequest();

        var view = userController.loginUser(user, model, request);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestLogOutThenRedirectLoginPage() {
        var request = new MockHttpSession();
        var view = userController.logout(request);

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}
