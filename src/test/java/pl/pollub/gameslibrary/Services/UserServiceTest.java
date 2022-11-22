package pl.pollub.gameslibrary.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pollub.gameslibrary.Models.User;
import pl.pollub.gameslibrary.Repositories.RoleRepository;
import pl.pollub.gameslibrary.Repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleService roleService;
    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(
                userRepository,
                roleRepository,
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
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

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

        underTest.edit(newUser,id);
        verify(userRepository).save(userInRepo);
    }

    @Test
    void canDeleteUser () {
        Long id = 1L;
        User user = new User(id,null,null,null,null,null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = underTest.delete(id);
        verify(userRepository).deleteById(id);

        assertThat(result).isEqualTo(user);
    }
}