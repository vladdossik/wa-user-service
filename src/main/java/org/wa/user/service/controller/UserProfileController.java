package org.wa.user.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Profiles", description = "API для управления профилями пользователей")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping
    @Operation(summary = "Получить профиль")
    public UserProfileResponseDto getUserProfile(@PathVariable Long userId) {
        return userProfileService.getUserProfile(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать профиль")
    public UserProfileResponseDto createUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        return userProfileService.validateUserAndCreate(userId, profileDto);
    }

    @PutMapping
    @Operation(summary = "Обновить профиль")
    public UserProfileResponseDto updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequestDto profileDto) {
        return userProfileService.updateUserProfile(userId, profileDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить профиль")
    public void deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteUserProfile(userId);
    }
}
