package mejai.mejaigg.app.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.jwt.config.JwtProperties;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

	private final JwtProperties jwtProperties;

	public String generateAccessToken(long id) {
		return Jwts.builder()
			.subject(String.valueOf(id))
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
			.signWith(signKey())
			.compact();
	}

	public String generateRefreshToken(long id) {
		return Jwts.builder()
			.subject(String.valueOf(id))
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
			.signWith(signKey())
			.compact();
	}

	public Long extractId(String token) throws JwtException {
		String id = Jwts.parser()
			.verifyWith(signKey())
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();

		return Long.parseLong(id);
	}

	private SecretKey signKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
