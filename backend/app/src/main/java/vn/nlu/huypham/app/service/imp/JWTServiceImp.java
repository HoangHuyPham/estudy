package vn.nlu.huypham.app.service.imp;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.dto.response.TokenPairResponse;
import vn.nlu.huypham.app.entity.RefreshToken;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.RefreshTokenRepo;
import vn.nlu.huypham.app.service.JWTService;
import vn.nlu.huypham.app.service.RedisService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JWTServiceImp implements JWTService
{
	final JWTVerifier jwtVerifier;
	final Algorithm algorithm;
	final AppConfig appConfig;

	final RefreshTokenRepo refreshTokenRepo;
	final RedisService redisService;

	@Override
	public JWTInfo extractFrom(
		String token) throws JWTVerificationException
	{
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		if (redisService.checkATBlackList(UUID.fromString(decodedJWT.getId())))
		{
			log.warn("Redis cache hit for blacklisted token: {}", decodedJWT.getId());
			throw new JWTVerificationException("Token is blacklisted");
		}

		return JWTInfo.builder().issuer(decodedJWT.getIssuer())
				.id(UUID.fromString(decodedJWT.getId()))
				.avatar(decodedJWT.getClaim("avatar").asString())
				.roles(decodedJWT.getClaim("roles").asList(String.class))
				.isDarkMode(decodedJWT.getClaim("isDarkMode").asBoolean())
				.displayName(decodedJWT.getClaim("displayName").asString())
				.username(decodedJWT.getSubject()).expiredAt(decodedJWT.getExpiresAtAsInstant())
				.build();
	}

	@Override
	public String generateAT(
		User user)
	{
		Instant now = Instant.now();
		Instant expireAt = now.plus(appConfig.getJwt().getAccessExp(), ChronoUnit.SECONDS);

		Builder tokenBuilder = JWT.create().withIssuer(appConfig.getJwt().getIssuer())
				.withSubject(user.getUsername()).withJWTId(UUID.randomUUID().toString())
				.withIssuedAt(Date.from(now)).withExpiresAt(Date.from(expireAt));

		if (user.getDisplayName() != null)
		{
			tokenBuilder.withClaim("displayName", user.getDisplayName());
		}
		if (user.getAvatar() != null)
		{
			tokenBuilder.withClaim("avatar", user.getAvatar());
		}
		if (user.getRoles() != null && !user.getRoles().isEmpty())
		{
			tokenBuilder.withClaim("roles",
					user.getRoles().stream().map(role -> role.getName().name()).toList());
		}

		tokenBuilder.withClaim("isDarkMode", user.isDarkMode());

		return tokenBuilder.sign(algorithm);
	}

	@Override
	public UUID generateRT(
		User user)
	{
		RefreshToken refreshToken = refreshTokenRepo
				.save(RefreshToken
						.builder().user(user).expiredAt(Instant.now()
								.plusSeconds(appConfig.getJwt().getRefreshExp()).getEpochSecond())
						.build());
		return refreshToken.getId();
	}

	@Override
	@Transactional
	public TokenPairResponse rotateRT(
		String refreshToken) throws AppException
	{
		long result = refreshTokenRepo.useRefreshTokenIfValid(UUID.fromString(refreshToken),
				Instant.now().getEpochSecond());
		if (result == 0)
		{
			throw Errors.REFRESH_TOKEN_INVALID;
		}

		RefreshToken rt = refreshTokenRepo.findById(UUID.fromString(refreshToken))
				.orElseThrow(() -> Errors.REFRESH_TOKEN_INVALID);
		User user = rt.getUser();

		if (user == null)
			throw Errors.REFRESH_TOKEN_INVALID;

		return TokenPairResponse.builder().accessToken(generateAT(user))
				.refreshToken(generateRT(user)).build();
	}

	@Override
	@Transactional
	public void invokeRT(
		String refreshToken) throws AppException
	{
		long result = refreshTokenRepo.useRefreshTokenIfValid(UUID.fromString(refreshToken),
				Instant.now().getEpochSecond());
		if (result == 0)
		{
			throw Errors.REFRESH_TOKEN_INVALID;
		}
	}
}