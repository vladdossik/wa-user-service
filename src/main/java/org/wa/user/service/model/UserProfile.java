package org.wa.user.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa.user.service.model.enums.ActivityLevelEnum;
import org.wa.user.service.model.enums.HealthGoalEnum;

@Entity
@Table(name = "user_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_seq")
    @SequenceGenerator(name = "user_profile_seq", sequenceName = "user_profile_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "First name is required")
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "activityLevel")
    private ActivityLevelEnum activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "healthGoal")
    private HealthGoalEnum healthGoal;

    public UserProfile(User user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
