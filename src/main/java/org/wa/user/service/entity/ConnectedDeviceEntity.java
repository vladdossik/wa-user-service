package org.wa.user.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.wa.user.service.entity.enumeration.ConnectedDeviceType;
import java.time.OffsetDateTime;

@Entity
@Table(name = "connected_device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedDeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connected_device_seq")
    @SequenceGenerator(name = "connected_device_seq", sequenceName = "connected_device_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private ConnectedDeviceType deviceType;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "is_active")
    private Boolean isActive;

    @UpdateTimestamp
    @Column(name = "last_sync_at")
    private OffsetDateTime lastSyncAt;

    public ConnectedDeviceEntity(UserEntity user, ConnectedDeviceType deviceType, String deviceId) {
        this.user = user;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
    }
}
