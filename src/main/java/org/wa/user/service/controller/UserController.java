package org.wa.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserShortInfoDto>> getAllUsers(){
        List<UserShortInfoDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto createDto){
        UserResponseDto createdUser = userService.createUser(createDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserUpdateDto updateDto){
        UserResponseDto updatedUser = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateDto statusDto) {
        UserResponseDto updatedUser = userService.updateUserStatus(id, statusDto);
        return ResponseEntity.ok(updatedUser);
    }
}
