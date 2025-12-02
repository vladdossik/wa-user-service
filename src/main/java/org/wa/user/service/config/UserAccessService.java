package org.wa.user.service.config;


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

    public void checkUserAccess(Long targetUserId) {
        if (isAdmin()) {
            log.debug("Admin access granted to user {}", targetUserId);
            return;
        }

        Long userId = getUserId();
        if (!userId.equals(targetUserId)) {
            log.warn("Access denied: user {} tried to access user {}", userId, targetUserId);
            throw new AccessException("Access denied to user data");
        }
        log.debug("User access granted to own data {}", targetUserId);
    }

    private boolean isAdmin() {
        return AuthContextHolder.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private Long getUserId() {
        String email = AuthContextHolder.getEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }
}
