package org.wa.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wa.user.service.model.ConnectedDevices;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectedDevicesRepository extends JpaRepository<ConnectedDevices, Long> {
    List<ConnectedDevices> findByUserId(Long userId);

    boolean existsByDeviceId(String deviceId);

    @Query("SELECT cd FROM ConnectedDevices cd WHERE cd.id = :deviceId AND cd.user.id = :userId")
    Optional<ConnectedDevices> findByUserIdAndDeviceId(@Param("deviceId") Long deviceId, @Param("userId") Long userId);

}
