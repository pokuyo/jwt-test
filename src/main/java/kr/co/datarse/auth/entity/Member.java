package kr.co.datarse.auth.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "CMM_USER")
@Entity
//public class Member implements UserDetails {
public class Member {

	@Id
	@Column(name = "USR_NO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long usrNo;
	@Column(name = "USR_ID")
	private String	usrId;
	@Column(name = "USR_PW")
	private String	usrPw;
	@Column(name = "USR_NM")
	private String	usrNm;
	@Column(name = "PW_INITL_YN")
	private String	pwInitlYn;
	@Column(name = "PW_INITL_DT")
	private Date	pwInitlDt;
	@Column(name = "PW_FAILURE_COUNT")
	private int		pwFailureCount;
	@Column(name = "CNT_FRS")
	private String	cntFrs;
	@Column(name = "CNT_MDL")
	private String	cntMdl;
	@Column(name = "CNT_END")
	private String	cntEnd;
//	@Column(name = "ROLE_CD")
//	private String	roleCd;
	@Column(name = "GP_CD")
	private String	gpCd;
	@Column(name = "REGISTER")
	private Number	register;
	@Column(name = "RGSDE")
	private Date	rgsde;
	@Column(name = "UPDUSR")
	private String	updusr;
	@Column(name = "UPDDE")
	private Date	updde;
	@Column(name = "LOCK_YN")
	private String	lockYn;
	@Column(name = "APPROVAL_YN")
	private String	approvalYn;
	@Column(name = "USR_EMAIL")
	private String	usrEmail;
	@Column(name = "OFFM_TELNO")
	private String	offmTelno; 
	@Column(name = "USR_RSPOFC")
	private String	usrRspofc;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE_CD")
	private Authority roleCd;
	
	@Builder
	public Member(String usrId, String usrPw, Authority roleCd) {
		this.usrId = usrId;
		this.usrPw = usrPw;
		this.roleCd = roleCd;
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Builder
	public Member(Member member) {
		this.usrNo = member.usrNo;
		this.usrId = member.usrId;
		this.usrPw = member.usrPw;
		this.usrNm = member.usrNm;
		this.pwInitlYn = member.pwInitlYn;
		this.pwInitlDt = member.pwInitlDt;
		this.pwFailureCount = member.pwFailureCount;
		this.cntFrs = member.cntFrs;
		this.cntMdl = member.cntMdl;
		this.cntEnd = member.cntEnd;
		this.gpCd = member.gpCd;
		this.register = member.register;
		this.rgsde = member.rgsde;
		this.updusr = member.updusr;
		this.updde = member.updde;
		this.lockYn = member.lockYn;
		this.approvalYn = member.approvalYn;
		this.usrEmail = member.usrEmail;
		this.offmTelno = member.offmTelno; 
		this.usrRspofc = member.usrRspofc;
	}
}
