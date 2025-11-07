package org.wa.user.service.dto.profile;

import lombok.Data;
import org.wa.user.service.model.enumeration.ActivityLevelEnum;
import org.wa.user.service.model.enumeration.HealthGoalEnum;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private ActivityLevelEnum activityLevel;
    private HealthGoalEnum healthGoal;
}
