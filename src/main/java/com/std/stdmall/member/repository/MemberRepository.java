package com.std.stdmall.member.repository;

    import com.std.stdmall.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔티티타입, 엔티티의 PK타입>
public interface MemberRepository extends JpaRepository<Member, Long> {
}
