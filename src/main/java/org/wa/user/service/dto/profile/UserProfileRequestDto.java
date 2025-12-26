package org.wa.user.service.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.wa.user.service.entity.enumeration.ActivityLevel;
import org.wa.user.service.entity.enumeration.HealthGoal;

@Data
public class UserProfileRequestDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    private String lastName;
    private ActivityLevel activityLevel;
    private HealthGoal healthGoal;
}
