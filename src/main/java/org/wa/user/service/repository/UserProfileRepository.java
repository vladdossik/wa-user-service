package org.wa.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wa.user.service.model.UserProfile;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
