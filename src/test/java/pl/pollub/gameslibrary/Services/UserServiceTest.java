package pl.pollub.gameslibrary.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pollub.gameslibrary.Models.Utility.DetailedUserResponse;
import pl.pollub.gameslibrary.Exceptions.Exceptions.IncorrectRequestDataException;
import pl.pollub.gameslibrary.Exceptions.Exceptions.UserNotFoundException;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleService roleService;
    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(
                userRepository,
                passwordEncoder,
                roleService);
    }

    @Test
    void canGetAllUsers() {
        underTest.getAll();
        verify(userRepository).findAll();
    }

    @Test
    void canGetUserById() {
        Long id = 1L;

        underTest.getById(id);
        verify(userRepository).findById(id);
    }

    @Test
    void canGetUserByEmail() {
        String email = "admin@gmail.com";

        underTest.getByEmail(email);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void canCreateNewUser() {
        User user = new User(
                null,
                "test",
                "test@gmail.com",
                "test",
                null,
                null
        );

        underTest.add(user);

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(emailCaptor.capture());
        String capturedEmail = emailCaptor.getValue();
        assertThat(capturedEmail).isEqualTo(user.getEmail());

        ArgumentCaptor<String> loginCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByLogin(loginCaptor.capture());
        String capturedLogin = loginCaptor.getValue();
        assertThat(capturedLogin).isEqualTo(user.getLogin());
    }

    @Test
    void canEditUser() {
        Long id = 1L;
        User user = new User(
                1L,
                "old",
                "old@gmail.com",
                null,
                null,
                null
        );
        User newUser = new User(
                1L,
                null,
                "new@gmail.com",
                null,
                null,
                null
        );

        User userInRepo = new User(
                1L,
                "old",
                "new@gmail.com",
                null,
                null,
                null
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        try {
            underTest.edit(newUser,id);
        } catch (UserNotFoundException | IncorrectRequestDataException e) {
            e.printStackTrace();
        }
        verify(userRepository).save(userInRepo);
    }

    @Test
    void canDeleteUser () {
        Long id = 1L;
        User user = new User(id,null,null,null,null,null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<DetailedUserResponse> result = null;
        try {
            result = underTest.delete(id);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        verify(userRepository).deleteById(id);

        assert result != null;
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(Objects.requireNonNull(result.getBody()).getCode()).isEqualTo("USER_DELETED");
        assertThat(result.getBody().getMessage()).isEqualTo("Użytkownik został usunięty.");
        assertThat(result.getBody().getData()).isEqualTo(null);

    }
}