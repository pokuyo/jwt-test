package kr.co.datarse.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.datarse.auth.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByUsrId(String usrId);
//	Optional<Member> findOneWithAuthoritiesUsrId(String usrId);
	boolean existsByUsrId(String usrId);
}
