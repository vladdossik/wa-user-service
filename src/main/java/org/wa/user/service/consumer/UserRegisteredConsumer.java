package org.wa.user.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.service.UserProfileService;
import org.wa.user.service.service.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {
    private final UserService userService;
    private final UserProfileService userProfileService;

    @KafkaListener(
            topics = "user.registered",
            groupId = "wa-user-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserRegisteredEvent(UserRegisteredDto event) {
        try {
            UserResponseDto userCreateResponse = userService.createUserFromRegisteredEvent(event);
            userProfileService.createUserProfileFromRegisteredEvent(
                    userCreateResponse.getId(), userCreateResponse);
        } catch (AttributeDuplicateException exception) {
            log.warn("user already processed");
        }
    }
}
