//package pl.pollub.gameslibrary.Services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import pl.pollub.gameslibrary.Models.User;
//import pl.pollub.gameslibrary.Repositories.UserRepository;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    private UserServiceImpl underTest;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new UserServiceImpl(userRepository);
//    }
//
//    @Test
//    @Disabled
//    void canGetAllUsers() {
//        underTest.findAll();
//        verify(userRepository).findAll();
//    }
//
//    @Test
//    @Disabled
//    void canGetUserById() {
//        Long id = 1L;
//
//        underTest.findById(id);
//        verify(userRepository).findById(id);
//    }
//
//    @Test
//    @Disabled
//    void edit() {
//        Long id = 1L;
//        User user = new User(
//                null,
//                "james123",
//                "james.smith@gmail.com",
//                "james123",
//                null
//        );
//
//        underTest.edit(user,id);
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    @Disabled
//    void canCreateNewUser() {
//        User user = new User(
//                null,
//                "james123",
//                "james.smith@gmail.com",
//                "james123",
//                null
//        );
//
//        underTest.add(user);
//        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepository).save(argumentCaptor.capture());
//
//        User capturedUser = argumentCaptor.getValue();
//        assertThat(capturedUser).isEqualTo(user);
//    }
//
//    @Test
//    @Disabled
//    void del() {
//        Long id = 1L;
//        underTest.del(id);
//    }
//}