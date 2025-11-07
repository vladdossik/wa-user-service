package org.wa.user.service.dto.profile;

import lombok.Data;
import org.wa.user.service.model.User;
import org.wa.user.service.model.enumeration.ActivityLevelEnum;
import org.wa.user.service.model.enumeration.HealthGoalEnum;

@Data
public class UserProfileDto {
    private Long id;
    private User user;
    private String firstName;
    private String lastName;
    private ActivityLevelEnum activityLevel;
    private HealthGoalEnum healthGoal;
}
