package org.wa.user.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.wa.user.service.entity.enumeration.Gender;
import java.time.LocalDateTime;

@Data
public class UserUpdateDto {
    @Email(message = "Email должен быть действительным")
    private String email;
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Телефон должен быть действительным")
    private String phone;
    @Past(message = "Дата рождения должна быть в прошедшем времени")
    private LocalDateTime birthday;
    private Gender gender;
    @Positive(message = "Рост должен иметь положительное значение")
    @Max(value = 250, message = "Рост должен быть не более 250 см")
    private Integer height;
    @Positive(message = "Вес должен иметь положительное значение")
    @Max(value = 300, message = "Вес должен быть не более 300 кг")
    private Integer weight;
}
