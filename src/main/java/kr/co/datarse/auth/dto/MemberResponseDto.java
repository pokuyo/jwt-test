package kr.co.datarse.auth.dto;

import kr.co.datarse.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

	private String usrId;
	
	public static MemberResponseDto of(Member member) {
		return new MemberResponseDto(member.getUsrId());
	}
}
