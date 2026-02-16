package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.PasswordReset;
import com.flight.util.OtpType;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

	Optional<PasswordReset> findTopByUserIdAndUsedAndTypeOrderByExpiryTimeDesc(Long userId, char used, OtpType type);

}
