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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa.user.service.model.enums.ConnectedDevicesTypeEnum;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "connected_devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connected_devices_seq")
    @SequenceGenerator(name = "connected_devices_seq", sequenceName = "connected_devices_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Device type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "deviceType", nullable = false)
    private ConnectedDevicesTypeEnum deviceType;

    @NotBlank(message = "Device ID is required")
    @Column(name = "deviceId", nullable = false)
    private String deviceId;

    @NotBlank(message = "Device model is required")
    @Column(name = "deviceModel")
    private String deviceModel;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "lastSyncAt")
    private OffsetDateTime lastSyncAt;

    protected void onCreate() {
        if (lastSyncAt == null) {
            lastSyncAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
    }

    public ConnectedDevices(User user, ConnectedDevicesTypeEnum deviceType, String deviceId) {
        this.user = user;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
    }
}
