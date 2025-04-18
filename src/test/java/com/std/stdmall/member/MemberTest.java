package com.std.stdmall.member;

import com.std.stdmall.common.Gender;
import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    // 회원 정보 생성
    @Test
    void saveMember() {
        //given
        LocalDateTime now = LocalDateTime.now();
        memberRepository.save(Member.builder()
                .loginId("tester3")
                .password("1234")
                .name("tester2")
                .gender(Gender.M)
                .birthday(LocalDate.of(1994, 3, 17))
                .deleteYn(false)
                .build());
        //when
        List<Member> memberList = memberRepository.findAll();
        //then
        Member member = memberList.get(0);
        assertThat(member.getLoginId()).isEqualTo("tester3");

    }

    // 전체 회원 조회
    @Test
    void findAllMember() {
        memberRepository.findAll();
    }

    // 회원 상세정보 조회 ->현재 테스트 구조상 조회안됨
    @Test
    void findMemberById() {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException());
        Assertions.assertEquals(member.getLoginId(), "tester2");
    }

    // 회원 정보 삭제
    @Test
    void deleteMemberById() {
        memberRepository.deleteById(1L);
    }
}
