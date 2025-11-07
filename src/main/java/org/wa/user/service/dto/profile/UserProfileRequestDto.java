package org.wa.user.service.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.wa.user.service.model.enumeration.ActivityLevelEnum;
import org.wa.user.service.model.enumeration.HealthGoalEnum;

@Data
public class UserProfileRequestDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private ActivityLevelEnum activityLevel;
    private HealthGoalEnum healthGoal;
}
