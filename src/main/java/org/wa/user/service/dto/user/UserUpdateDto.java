package org.wa.user.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Phone should be valid")
    private String phone;

    @Positive(message = "Height must be positive value")
    @Max(value = 250, message = "Height must be at most 250 cm")
    private Integer height;

    @Positive(message = "Weight must be positive value")
    @Max(value = 300, message = "Weight must be at most 300 cm")
    private Integer weight;
}
