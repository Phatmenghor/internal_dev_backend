package com.internal.feature.auth.repository;

import com.internal.feature.auth.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    /**
     * Find the latest active token for a given email
     */
    Optional<PasswordResetToken> findByEmailAndUsedFalseOrderByCreatedAtDesc(String email);
    
    /**
     * Find token by email and OTP
     */
    Optional<PasswordResetToken> findByEmailAndOtpAndUsedFalse(String email, String otp);
    
    /**
     * Delete all expired tokens
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    /**
     * Delete all tokens for a specific email
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.email = :email")
    void deleteByEmail(@Param("email") String email);
    
    /**
     * Check if email has an active token
     */
    boolean existsByEmailAndUsedFalseAndExpiryDateAfter(String email, LocalDateTime now);
}