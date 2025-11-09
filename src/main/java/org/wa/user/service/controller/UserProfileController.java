package org.wa.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wa.user.service.dto.profile.UserProfileRequestDto;
import org.wa.user.service.dto.profile.UserProfileResponseDto;
import org.wa.user.service.service.UserProfileService;

@RestController
@RequestMapping("/v1/users/{userId}/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping
    public UserProfileResponseDto getUserProfile(@PathVariable Long userId) {
        return userProfileService.getUserProfile(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponseDto createUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        return userProfileService.createUserProfile(userId, profileDto);
    }

    @PutMapping
    public UserProfileResponseDto updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        return userProfileService.updateUserProfile(userId, profileDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteUserProfile(userId);
    }
}
