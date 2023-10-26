package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.exceptions.RoleNotFoundException;
import com.powtorka.vetclinic.model.security.permission.EPermission;
import com.powtorka.vetclinic.model.security.permission.Permission;
import com.powtorka.vetclinic.model.security.role.ERole;
import com.powtorka.vetclinic.model.security.role.Role;
import com.powtorka.vetclinic.model.security.user.User;
import com.powtorka.vetclinic.payload.request.LoginRequest;
import com.powtorka.vetclinic.payload.request.SignupRequest;
import com.powtorka.vetclinic.payload.response.JwtResponse;
import com.powtorka.vetclinic.payload.response.MessageResponse;
import com.powtorka.vetclinic.repository.PermissionRepository;
import com.powtorka.vetclinic.repository.RoleRepository;
import com.powtorka.vetclinic.repository.UserRepository;
import com.powtorka.vetclinic.security.jwt.JwtUtils;
import com.powtorka.vetclinic.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        userDetails.getAuthorities().forEach(authority -> {
            if (authority.getAuthority().startsWith("ROLE_")) {
                roles.add(authority.getAuthority());
            } else {
                permissions.add(authority.getAuthority());
            }
        });

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles, permissions));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(RoleNotFoundException::new);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(RoleNotFoundException::new);
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(RoleNotFoundException::new);
                        roles.add(modRole);
                        break;
                    case "doctor_admin":
                        Role doctorAdminRole = roleRepository.findByName(ERole.ROLE_DOCTOR_ADMIN)
                                .orElseThrow();
                        roles.add(doctorAdminRole);
                        break;
                    case "patient_admin":
                        Role patientAdminRole = roleRepository.findByName(ERole.ROLE_PATIENT_ADMIN)
                                .orElseThrow();
                        roles.add(patientAdminRole);
                        break;
                    case "appointment_admin":
                        Role appointmentAdminRole = roleRepository.findByName(ERole.ROLE_APPOINTMENT_ADMIN)
                                .orElseThrow();
                        roles.add(appointmentAdminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        Set<String> strPermissions = signUpRequest.getPermissions();
        Set<Permission> permissions = new HashSet<>();

        if (strPermissions != null) {
            strPermissions.forEach(permissionName -> {
                Permission permission = permissionRepository.findByName(EPermission.valueOf(permissionName))
                        .orElseThrow(() -> new RuntimeException("Error: Permission is not found."));
                permissions.add(permission);
            });
        }

        user.setPermissions(permissions);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}