//package kr.co.datarse.auth.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor
//@Table(name = "CMM_USER_ROLE")
//@Entity
//public class MemberRole {
//
//	@Id
//	private Long Id;
//	
//	@Column(name = "USR_NO")
//	private String usrNo;
//	@Column(name = "ROLE_CD")
//	private String roleCd;
//	@Column(name = "REGISTER")
//	private String register;
//	@Column(name = "RGSDE")
//	private Date rgsde;
//	@Column(name = "UPDUSR")
//	private String updusr;
//	@Column(name = "UPDDE")
//	private Date updde;
//	
//	@ManyToOne(optional = false)
//	@JoinTable(name = "CMM_USER",
//			joinColumns = @JoinColumn(name = "USR_NO"),
//			inverseJoinColumns = @JoinColumn(name ="USR_NO")
//	)
//	private 
//	
//
//}
