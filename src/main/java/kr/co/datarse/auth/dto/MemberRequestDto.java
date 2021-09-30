package kr.co.datarse.auth.dto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.datarse.auth.entity.Authority;
import kr.co.datarse.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

	private String usrNo;
	private String usrId;
	private String usrPw;
	private String roleCd;
	
	public Member toMember(PasswordEncoder passwordEncoder) {
		return Member.builder()
//					.usrNo(usrNo)
					.usrId(usrId)
					.usrPw(passwordEncoder.encode(usrPw))
					.roleCd(Authority.ROLE_USER)
					.build();
	}
	
	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(usrId, usrPw);
	}
}
