package co.myboost.po.yasirmaulana.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.myboost.po.yasirmaulana.domain.User;
import co.myboost.po.yasirmaulana.dto.UserCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.UserListResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserCreateUpdateRequestDTO dto = new UserCreateUpdateRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhone("123456789");

        userService.createUser(dto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Jane");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        UserCreateUpdateRequestDTO dto = new UserCreateUpdateRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("johndoe@example.com");
        dto.setPhone("123456789");

        userService.updateUser(userId, dto);

        verify(userRepository, times(1)).save(existingUser);
        assertEquals("John", existingUser.getFirstName());
        assertEquals("Doe", existingUser.getLastName());
    }

    @Test
    void testUpdateUser_NotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserCreateUpdateRequestDTO dto = new UserCreateUpdateRequestDTO();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.updateUser(userId, dto));
        assertEquals("invalid.user.id", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void testDeleteUser_NotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
        assertEquals("invalid.user.id", exception.getMessage());
    }

    @Test
    void testGetUserById_Success() {
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void testGetUserById_NotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
        assertEquals("invalid.user.id", exception.getMessage());
    }

    @Test
    void testGetUserList_Success() {
        int pages = 0;
        int limit = 10;
        String sortBy = "firstName";
        String direction = "asc";
        String firstName = "John";

        User user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");
        user.setPhone("123456789");

        Page<User> pageResult = new PageImpl<>(List.of(user));
        when(userRepository.findByFirstNameLikeIgnoreCase(anyString(), any(Pageable.class))).thenReturn(pageResult);

        ResultPageResponseDTO<UserListResponseDTO> result = userService.getUserList(pages, limit, sortBy, direction, firstName);

        assertNotNull(result);
        assertEquals(1, result.getResult().size());
        assertEquals(user.getFirstName(), result.getResult().getFirst().getFirstName());
    }
}