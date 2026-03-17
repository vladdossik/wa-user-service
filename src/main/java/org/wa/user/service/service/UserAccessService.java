package org.wa.user.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wa.auth.lib.util.AuthContextHolder;
import org.wa.user.service.exception.AccessException;
import org.wa.user.service.exception.ResourceNotFoundException;
import org.wa.user.service.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAccessService {

    private final UserRepository userRepository;

    public void checkUser(Object targetId) {
        if (isAdmin()) return;

        Object currentId = targetId instanceof Long ? getCurrentUserId() : AuthContextHolder.getId();

        if (!currentId.equals(targetId)) {
            log.warn("Access denied: user {} tried to access user {}", currentId, targetId);
            throw new AccessException("Access denied");
        }
    }

    public boolean checkAccess(Object targetId) {
        try {
            checkUser(targetId);
            return true;
        } catch (AccessException e) {
            return false;
        }
    }

    public boolean isAdmin() {
        return AuthContextHolder.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private Long getCurrentUserId() {
        return userRepository.findByEmail(AuthContextHolder.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }
}
