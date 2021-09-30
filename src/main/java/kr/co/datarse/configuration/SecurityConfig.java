package kr.co.datarse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.datarse.auth.config.JwtAccessDeniedHandler;
import kr.co.datarse.auth.config.JwtAuthenticationEntryPoint;
import kr.co.datarse.auth.config.TokenProvider;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable()
			.csrf().disable()
			// [/auth/**] 이외의 다른 모든 요청은 인증처리 (로그인, 회원가입 비인증페이지)
			.authorizeRequests()
			.antMatchers("/api/user/**").permitAll()
			.anyRequest().authenticated()
			// 상태없는 세션 이용, 세션에 사용자 정보 저장안함
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.formLogin().disable()
				.headers().frameOptions().disable()
			.and()
				.headers().frameOptions().sameOrigin()
			// JwtFilter 적용	
			.and()
				.apply(new JwtSecurityConfig(tokenProvider))
			// exception handling
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler);
	}
}