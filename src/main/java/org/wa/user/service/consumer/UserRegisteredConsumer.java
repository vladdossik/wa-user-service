package org.wa.user.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.wa.user.service.dto.user.UserRegisteredDto;
import org.wa.user.service.dto.user.UserResponseDto;
import org.wa.user.service.exception.AttributeDuplicateException;
import org.wa.user.service.mapper.UserMapper;
import org.wa.user.service.mapper.UserProfileMapper;
import org.wa.user.service.service.DecryptService;
import org.wa.user.service.service.UserProfileService;
import org.wa.user.service.service.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final DecryptService decryptService;

    @KafkaListener(
            topics = "user.registered",
            groupId = "wa-user-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserRegisteredEvent(UserRegisteredDto event) {
        try {
            UserResponseDto userCreateResponse = userService.createUser(userMapper.toCreateDtoFromTopic(event, decryptService));
            userProfileService.createUserProfile(
                    userCreateResponse.getId(), userProfileMapper.toRequestDto(userCreateResponse));
        } catch (AttributeDuplicateException exception) {
            log.warn("user already processed");
        }
    }
}
