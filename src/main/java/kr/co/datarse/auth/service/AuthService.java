package kr.co.datarse.auth.service;

import javax.transaction.Transactional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.datarse.auth.config.TokenProvider;
import kr.co.datarse.auth.dto.MemberRequestDto;
import kr.co.datarse.auth.dto.MemberResponseDto;
import kr.co.datarse.auth.dto.TokenDto;
import kr.co.datarse.auth.dto.TokenRequestDto;
import kr.co.datarse.auth.entity.Member;
import kr.co.datarse.auth.entity.RefreshToken;
import kr.co.datarse.auth.repository.MemberRepository;
import kr.co.datarse.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//public class AuthService implements UserDetailsService {
public class AuthService {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	
	/**
	 * 회원가입
	 * @param memberRequestDto
	 * @return
	 */
	@Transactional
	public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
		if (memberRepository.existsByUsrId(memberRequestDto.getUsrId())) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		}
		Member member = memberRequestDto.toMember(passwordEncoder);
		return MemberResponseDto.of(memberRepository.save(member));
	}
	
	/**
	 * 로그인
	 * @param memberRequestDto
	 * @return
	 */
	@Transactional
	public TokenDto signin(MemberRequestDto memberRequestDto) {
		// 1. login ID/PW 기반으로 AuthenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
		
		// 2. PW로 검증 시작
		// 	  authenticate method 실행 시 CustomUserDetailService에서 생성한 loadUserByUsername method 실행
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		// 3. 인증정보 기반으로 JWT 생성
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
		
		// 4. RefreshToken 저장
		RefreshToken refreshToken = RefreshToken.builder()
												.key(authentication.getName())
												.value(tokenDto.getRefreshToken())
												.build();
		
		refreshTokenRepository.save(refreshToken);
		
		// 5. Token 발급
		return tokenDto;
	}
	
	/**
	 * 토큰 재발급
	 * @param tokenRequestDto
	 * @return
	 */
	@Transactional
	public TokenDto reissue(TokenRequestDto tokenRequestDto) {
		// 1. refresh token 검증
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
		}
		
		// 2. Access Token 에서 User ID 추출
		Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
		
		// 3. DB에서 User ID 기반으로 RefreshToken 가져옴
		RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
										.orElseThrow(() -> new RuntimeException("로그아웃 된 사용자 입니다."));
		
		// 4. RefreshToken 일치여부 확인
		if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("토큰의 유저정보가 일치하지 않습니다.");
		}
		
		// 5. new Token 생성
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
		
		// 6. DB 정보 업데이트
		RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);
		
		// 7. Token 발급
		return tokenDto;
	}

	// test
//	@Override
//	public UserDetails loadUserByUsername(String usrId) throws UsernameNotFoundException {
////		return memberRepository.findByUsrId(usrId)
//		return memberRepository.findOneWithAuthoritiesUsrId(usrId)
//				.map(this::createUserDetails)
//				.orElseThrow(() -> new UsernameNotFoundException(usrId + "-> 데이터베이스에서 찾을 수 없습니다."));
//	}
//	
//	private UserDetails createUserDetails(Member member) {
//		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRoleCd().toString());
//		
//		Member rstMember = new Member(member);
//		
//		return rstMember;
//	}

}
