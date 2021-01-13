package com.yeong.vege.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yeong.vege.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
	Optional<VerificationToken> findByToken(String token);
}
