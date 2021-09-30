package kr.co.datarse.auth.config;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.datarse.auth.dto.TokenDto;
import kr.co.datarse.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
	
	public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_TYPE = "bearer ";
    
	// private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 1;	// 1일
	// private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 30;	// 30일
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 3;	// 3분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;	// 1일
	
	private final Key key;
	
//	@Autowired
//	CustomUserDetailsService customUserDetailsService;
	
	/**
	 * secret key 호출
	 * @param secretKey
	 */
	public TokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	/**
	 * TokenDto 생성
	 * @param authentication
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public TokenDto generateTokenDto(Authentication authentication) {
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		
		// 권한 가져오기
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		Long now = (new Date()).getTime();
		
		// AccessToken 생성
		Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName())		// payload "sub": "name"
				.claim(AUTHORITIES_KEY, authorities)		// payload "auth": "ROLE_USER"
				.setExpiration(accessTokenExpiresIn)		// payload "exp": 1516239022
				.signWith(signatureAlgorithm, key)			// header "alg": "HS512"
				.compact();
		
		// RefreshToken 생성
		String refreshToken = Jwts.builder()
				.setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
				.signWith(signatureAlgorithm, key)
				.compact();
		
		return TokenDto.builder()
				.grantType(BEARER_TYPE)
				.accessToken(accessToken)
				.accessTokenExpires(accessTokenExpiresIn.getTime())
				.refreshToken(refreshToken)
				.build();
	}
	
	public UsernamePasswordAuthenticationToken getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);
		
		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}
		
		// Claims에서 권한 호출
		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		
//		UserDetails principal = this.customUserDetailsService.loadUserByUsername(claims.getSubject());
//		
//		return new UsernamePasswordAuthenticationToken(principal, null, authorities);
		
		// UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}
	
	/**
	 * parse Claims
	 * @param accessToken
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	
	/**
	 * token validation
	 * @param token
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
		return false;
	}
}
