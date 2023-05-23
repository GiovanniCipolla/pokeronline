package it.prova.pokeronline.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {

	@Value("${jwt-secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long jwtExpirationMs;

	// Method to sign and create a JWT using the injected secret
	public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
		return JWT.create()
				.withSubject("User Details")
				.withClaim("username", username)
				.withIssuedAt(new Date())
				.withIssuer("RACCOLTAFILMSPRINGREST")
				.withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
				.sign(Algorithm.HMAC256(secret));
	}

	// Method to verify the JWT and then decode and extract the username stored in
	// the payload of the token
	public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject("User Details")
				.withIssuer("RACCOLTAFILMSPRINGREST")
				.build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt.getClaim("username").asString();
	}

}

