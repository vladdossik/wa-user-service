package org.wa.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wa.user.service.model.ConnectedDevice;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectedDeviceRepository extends JpaRepository<ConnectedDevice, Long> {
    List<ConnectedDevice> findByUserId(Long userId);

    boolean existsByDeviceId(String deviceId);

    @Query("SELECT cd FROM ConnectedDevice cd WHERE cd.deviceId = :deviceId AND cd.user.id = :userId")
    Optional<ConnectedDevice> findByUserIdAndDeviceId(@Param("deviceId") Long deviceId, @Param("userId") Long userId);

}
