package mejai.mejaigg.app.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey; //

	@Value("${jwt.expiration}")
	private long expiration; // 예: 36000000 (10시간)

	public String generateToken(long id) {
		return Jwts.builder()
			.subject(String.valueOf(id))
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(signKey())
			.compact();
	}

	public boolean isValidateToken(String token) {
		return Jwts.parser().decryptWith(signKey()).build().isSigned(token);
	}

	private SecretKey signKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public Long extractId(String token) {
		String id = Jwts.parser().verifyWith(signKey()).build().parseSignedClaims(token).getPayload().getSubject();

		return Long.parseLong(id);
	}
}
