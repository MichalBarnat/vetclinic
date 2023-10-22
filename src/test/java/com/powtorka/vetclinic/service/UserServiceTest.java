package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.UserEntityNotFoundException;
import com.powtorka.vetclinic.model.user.UserEntity;
import com.powtorka.vetclinic.repository.UserRepository;
import com.powtorka.vetclinic.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity user;
    private UserEntity admin;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity(1L, "user", "pass", UserRole.USER);
        admin = new UserEntity(2L, "admin", "adminpass", UserRole.ADMIN);
    }



    @Test
    public void loadUserByUsername_ShouldReturnCorrectUserDetails() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("user");

        assertEquals("user", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void loadUserByUsername_ShouldThrowExceptionWhenUsernameNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());

        assertThrows(UserEntityNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentUser");
        });
    }

    @Test
    public void save_ShouldSaveUserWithEncodedPassword() {
        String encodedPassword = "encodedPass";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        UserEntity savedUser = userService.save(user);

        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    public void saveAll_ShouldSaveMultipleUsers() {
        List<UserEntity> users = Arrays.asList(user, admin);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(passwordEncoder.encode(admin.getPassword())).thenReturn(admin.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(admin)).thenReturn(admin);

        List<UserEntity> savedUsers = userService.saveAll(users);

        assertEquals(2, savedUsers.size());
        assertEquals(user.getUsername(), savedUsers.get(0).getUsername());
        assertEquals(admin.getUsername(), savedUsers.get(1).getUsername());
    }

    @Test
    public void findById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findById(1L);

        assertEquals(user, foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void findById_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(UserEntityNotFoundException.class, () -> {
            userService.findById(3L);
        });
    }


}
