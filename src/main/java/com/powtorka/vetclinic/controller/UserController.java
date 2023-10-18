package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.user.UserEntity;
import com.powtorka.vetclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findById(@PathVariable("id") Long id) {
        UserEntity user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, CREATED);
    }
}
