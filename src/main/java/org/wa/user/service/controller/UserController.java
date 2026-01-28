package org.wa.user.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wa.user.service.dto.common.PageResponse;
import org.wa.user.service.dto.user.UserCreateDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.dto.user.UserShortInfoDto;
import org.wa.user.service.dto.user.UserStatusUpdateDto;
import org.wa.user.service.dto.user.UserUpdateDto;
import org.wa.user.service.service.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API для управления пользователями")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить всех пользователей",
            description = "Только для администраторов")
    public PageResponse<UserShortInfoDto> getAllUsers(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sort") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Получить активных пользователей")
    public PageResponse<UserShortInfoDto> getActiveUsers(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sort") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userService.getNonDeletedUsers(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userAccessService.admin or @userAccessService.userId == #id")
    @Operation(summary = "Получить пользователя по ID")
    public UserResponseDto getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового пользователя",
            description = "Доступно без авторизации")
    public UserResponseDto createUser(@Valid @RequestBody UserCreateDto createDto) {
        return userService.createUser(createDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userAccessService.admin or @userAccessService.userId == #id")
    @Operation(summary = "Обновить пользователя")
    public UserResponseDto updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        return userService.updateUser(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userAccessService.admin or @userAccessService.userId == #id")
    @Operation(summary = "Удалить пользователя")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/permanent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить пользователя с БД",
            description = "Только для администраторов")
    public void hardDeleteUser(@PathVariable("id") Long id) {
        userService.hardDeleteUser(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("@userAccessService.admin or @userAccessService.userId == #id")
    @Operation(summary = "Обновить статус пользователя")
    public UserResponseDto updateUserStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserStatusUpdateDto statusDto) {
        return userService.updateUserStatus(id, statusDto);
    }
}
