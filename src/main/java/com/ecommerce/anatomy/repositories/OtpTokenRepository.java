package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.OtpToken;
import jakarta.persistence.Table;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findByMobile(String mobile);

    @Modifying
    @Transactional
    @Query("DELETE FROM OtpToken o WHERE o.mobile = :mobile")  // Explicit query
    void deleteByMobile(@Param("mobile") String mobile);

    @Modifying
    @Transactional
    @Query("DELETE FROM OtpToken o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);

    List<OtpToken> findByExpiresAtBefore(LocalDateTime now);
}