package org.wa.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa.user.service.dto.userProfile.UserProfileRequestDto;
import org.wa.user.service.dto.userProfile.UserProfileResponseDto;
import org.wa.user.service.service.UserProfileService;

@RestController
@RequestMapping("/v1/users/{userId}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDto profile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<UserProfileResponseDto> createUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        UserProfileResponseDto profile = userProfileService.createUserProfile(userId, profileDto);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        UserProfileResponseDto profile = userProfileService.updateUserProfile(userId, profileDto);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
}
