package com.std.stdmall.member.repository;

import com.std.stdmall.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JpaRepository<엔티티타입, 엔티티의 PK타입>
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
