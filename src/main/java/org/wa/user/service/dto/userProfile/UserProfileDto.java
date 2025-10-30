package org.wa.user.service.dto.userProfile;

import lombok.Data;
import org.wa.user.service.model.User;
import org.wa.user.service.model.enums.ActivityLevelEnum;
import org.wa.user.service.model.enums.HealthGoalEnum;

@Data
public class UserProfileDto {
    private Long id;
    private User user;
    private String firstName;
    private String lastName;
    private ActivityLevelEnum activityLevel;
    private HealthGoalEnum healthGoal;
}
