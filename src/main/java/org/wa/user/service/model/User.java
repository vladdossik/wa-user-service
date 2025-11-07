package org.wa.user.service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.wa.user.service.model.enumeration.GenderEnum;
import org.wa.user.service.model.enumeration.StatusEnum;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Phone should be valid")
    @Column(name = "phone", unique = true)
    private String phone;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    @Column(name = "birthday")
    private OffsetDateTime birthday;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required (MALE, FEMALE)")
    @Column(name = "gender")
    private GenderEnum gender;

    @Positive(message = "Height must be positive value")
    @Max(value = 250, message = "Height must be at most 250 cm")
    @Column(name = "height")
    private Integer height;

    @Positive(message = "Weight must be positive value")
    @Max(value = 300, message = "Weight must be at most 300 kg")
    @Column(name = "weight")
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    @Column(name = "status")
    private StatusEnum status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConnectedDevice> connectedDevices = new ArrayList<>();

    public User(String email, String phone, OffsetDateTime birthday, GenderEnum gender) {
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
    }
}
