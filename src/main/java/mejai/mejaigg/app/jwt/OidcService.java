package mejai.mejaigg.app.jwt;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.jwt.client.AppleClient;
import mejai.mejaigg.app.jwt.client.JwksClient;
import mejai.mejaigg.app.jwt.client.KakaoClient;
import mejai.mejaigg.app.jwt.config.AppleProperties;
import mejai.mejaigg.app.jwt.config.KakaoProperties;
import mejai.mejaigg.app.jwt.config.SocialProperties;
import mejai.mejaigg.app.jwt.dto.JsonWebKey;
import mejai.mejaigg.app.jwt.dto.OidcHeader;
import mejai.mejaigg.app.jwt.dto.OidcPayload;
import mejai.mejaigg.app.user.domain.SocialType;

@Service
@Slf4j
public class OidcService {

	private final Map<SocialType, JwksClient> jwksClients = new EnumMap<>(SocialType.class);
	private final Map<SocialType, SocialProperties> socialProperties = new EnumMap<>(SocialType.class);
	private final ObjectMapper objectMapper;

	@Autowired
	public OidcService(
		KakaoClient kakaoClient,
		AppleClient appleClient,
		KakaoProperties kakaoProperties,
		AppleProperties appleProperties,
		ObjectMapper objectMapper
	) {
		this.jwksClients.put(SocialType.KAKAO, kakaoClient);
		this.jwksClients.put(SocialType.APPLE, appleClient);
		this.socialProperties.put(SocialType.KAKAO, kakaoProperties);
		this.socialProperties.put(SocialType.APPLE, appleProperties);
		this.objectMapper = objectMapper;
	}

	public String extractSocialId(SocialType socialType, String idToken) {
		SocialProperties properties = socialProperties.get(socialType);
		JwksClient client = jwksClients.get(socialType);
		if (properties == null) {
			throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다.");
		}

		return extract(idToken, client, properties);
	}

	private String extract(String idToken, JwksClient client, SocialProperties properties) {
		var token = idToken.split("\\.");
		if (token.length != 3) {
			throw new IllegalArgumentException("Invalid id token");
		}
		JsonWebKey publicKey = findPublicKey(token[0], token[1], client, properties);

		var decodedIdToken = verifyIdToken(publicKey, idToken);

		return decodedIdToken.getPayload().getSubject();
	}

	private <T> T parseJwtToken(String token, Class<T> clazz) {
		try {
			byte[] decodedToken = Decoders.BASE64.decode(token);
			return objectMapper.readValue(decodedToken, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException("잘못된 토큰입니다.");
		}
	}

	private JsonWebKey findPublicKey(String header, String payload, JwksClient client, SocialProperties properties) {
		OidcPayload decodedPayload = parseJwtToken(payload, OidcPayload.class);
		decodedPayload.validate(properties.getAppKey(), properties.getIssuer());

		OidcHeader decodedHeader = parseJwtToken(header, OidcHeader.class);

		return client.getJwks().getKeys().stream()
			.filter(key -> key.getKid().equals(decodedHeader.getKid()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("공개키와 일치하는 키가 없습니다."));
	}

	private Jws<Claims> verifyIdToken(JsonWebKey publicKey, String token) {
		try {
			return Jwts.parser()
				.verifyWith(generatePublicKey(publicKey.getN(), publicKey.getE()))
				.build()
				.parseSignedClaims(token);
		} catch (JwtException e) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalArgumentException("서명이 잘못되었습니다.");
		}
	}

	private PublicKey generatePublicKey(
		String modulus,
		String exponent
	) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
		byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

		BigInteger n = new BigInteger(1, modulusBytes);
		BigInteger e = new BigInteger(1, exponentBytes);

		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(publicKeySpec);
	}
}
