package com.alberto.user_service.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alberto.user_service.dto.UserDTO;
import com.alberto.user_service.dto.UserMinDTO;
import com.alberto.user_service.dto.UserNewDTO;
import com.alberto.user_service.entities.Role;
import com.alberto.user_service.entities.User;
import com.alberto.user_service.projections.UserDetailsProjection;
import com.alberto.user_service.repositories.RoleRepository;
import com.alberto.user_service.repositories.UserRepository;
import com.alberto.user_service.tests.UserDetailsFactory;
import com.alberto.user_service.tests.UserFactory;
import com.alberto.user_service.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CustomUserUtil userUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String existingUser, nonExistingUser;
    private User user;
    private UserNewDTO userNewDto;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    public void setUp() {
        existingUser = "existing@example.com";
        nonExistingUser = "nonexisting@example.com";

        user = UserFactory.createClientUser(1L, existingUser);
        userNewDto = new UserNewDTO("John Doe", nonExistingUser, "password", "password");

        userDetails = UserDetailsFactory.createClientUser(existingUser);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail(nonExistingUser);
        Mockito.when(userRepository.save(any())).thenReturn(savedUser);

        Role userRole = new Role(1L, "ROLE_USER");
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));

        Mockito.when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUser)).thenReturn(userDetails);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistingUser)).thenReturn(new ArrayList<>());

        Mockito.when(userRepository.findByEmail(existingUser)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail(nonExistingUser)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {

        UserDetails result = userService.loadUserByUsername(existingUser);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUser);
    }

    @Test
    public void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingUser);
        });
    }

    @Test
    public void insert_ShouldCreateUser_WhenValidData() {

        UserMinDTO result = userService.insert(userNewDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userNewDto.getName(), result.getName());
    }

    @Test
    public void authenticated_ShouldReturnUser_WhenUserExists() {

        Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUser);

        User result = userService.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUser);
    }

    @Test
    public void authenticated_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        Mockito.when(userUtil.getLoggedUsername()).thenReturn(nonExistingUser);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticated();
        });
    }

    @Test
    public void getMe_ShouldReturnUserDTO_WhenUserIsAuthenticated() {

        Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUser);

        UserDTO result = userService.getMe();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getEmail(), existingUser);
    }
}
