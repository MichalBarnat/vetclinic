package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.user.MyUserDetails;
import com.powtorka.vetclinic.model.user.UserEntity;
import com.powtorka.vetclinic.repository.UserRepository;
import com.powtorka.vetclinic.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(MyUserDetails::new)
                .orElseThrow();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<UserRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    public UserEntity save(UserEntity user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<UserEntity> saveAll(List<UserEntity> users) {
        return users.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

}
