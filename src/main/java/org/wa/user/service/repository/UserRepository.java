package org.wa.user.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.wa.user.service.model.User;
import org.wa.user.service.model.enumeration.StatusEnum;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    Page<User> findByStatusNot(StatusEnum statusEnum, Pageable pageable);
}
