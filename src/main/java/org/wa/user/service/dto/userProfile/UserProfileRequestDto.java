package org.wa.user.service.dto.userProfile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.wa.user.service.model.enums.ActivityLevelEnum;
import org.wa.user.service.model.enums.HealthGoalEnum;

@Data
public class UserProfileRequestDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private ActivityLevelEnum activityLevel;
    private HealthGoalEnum healthGoal;
}
