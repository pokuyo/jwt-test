package kr.co.datarse.auth.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.datarse.auth.entity.Member;
import kr.co.datarse.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String usrId) throws UsernameNotFoundException {
		return memberRepository.findByUsrId(usrId)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException(usrId + "-> 데이터베이스에서 찾을 수 없습니다."));
	}
	
	
	private UserDetails createUserDetails(Member member) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRoleCd().toString());
		
		Member rebuildMember = new Member(member);
		
//		return rebuildMember;
		
		return new User(
				String.valueOf(member.getUsrId()),
				member.getUsrPw(),
				Collections.singleton(grantedAuthority)
		);
	}

}
