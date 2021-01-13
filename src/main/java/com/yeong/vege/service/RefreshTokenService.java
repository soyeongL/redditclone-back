package com.yeong.vege.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeong.vege.exceptions.SpringRedditException;
import com.yeong.vege.model.RefreshToken;
import com.yeong.vege.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRespository;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		
		return refreshTokenRespository.save(refreshToken);
	}
	
	void validateRefreshToken(String token) {
		refreshTokenRespository.findByToken(token)
		.orElseThrow(()-> new SpringRedditException("Invalid refresh token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRespository.deleteByToken(token);
	}
}
