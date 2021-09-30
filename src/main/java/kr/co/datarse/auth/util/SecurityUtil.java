package kr.co.datarse.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import kr.co.datarse.auth.entity.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityUtil {
    
    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Member getCurrentMemberInfo() {
    	final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	// TODO: authentication.getName() == null 확인필요
		if (authentication == null || authentication.getAuthorities() == null) {
    		throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
    	}
    	
    	return (Member) authentication.getPrincipal();
    }
    
    
}
