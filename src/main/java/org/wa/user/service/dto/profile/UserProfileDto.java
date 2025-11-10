package org.wa.user.service.dto.profile;

import lombok.Data;
import org.wa.user.service.entity.UserEntity;
import org.wa.user.service.entity.enumeration.ActivityLevel;
import org.wa.user.service.entity.enumeration.HealthGoal;

@Data
public class UserProfileDto {
    private Long id;
    private UserEntity userEntity;
    private String firstName;
    private String lastName;
    private ActivityLevel activityLevel;
    private HealthGoal healthGoal;
}
