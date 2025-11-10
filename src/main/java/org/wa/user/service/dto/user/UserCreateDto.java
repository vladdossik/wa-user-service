package org.wa.user.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.wa.user.service.entity.enumeration.Gender;
import java.time.LocalDateTime;

@Data
public class UserCreateDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Phone should be valid")
    private String phone;
    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private LocalDateTime birthday;
    @NotNull(message = "Gender is required (MALE, FEMALE)")
    private Gender gender;
    @Positive(message = "Height must be positive value")
    @Max(value = 250, message = "Height must be at most 250 cm")
    private Integer height;
    @Positive(message = "Weight must be positive value")
    @Max(value = 300, message = "Weight must be at most 300 kg")
    private Integer weight;
}
