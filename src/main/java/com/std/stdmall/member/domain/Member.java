package com.std.stdmall.member.domain;

import com.std.stdmall.common.BaseTimeEntity;
import com.std.stdmall.common.Gender;
import com.std.stdmall.common.MemberRole;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_num")
    private Long memberNum;             // 회원 번호 (PK)
    @Column(name = "login_id")
    private String loginId;      // 로그인 ID
    @Column(name = "password")
    private String password;     // 비밀번호
    @Column(name = "name")
    private String name;         // 이름
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;       // 성별
    @Column(name = "birthday")
    private LocalDate birthday;  // 생년월일
    @Column(name = "delete_yn")
    private Boolean deleteYn;    // 삭제 여부
    @Column(name = "role")
    private MemberRole role;

    @Builder
    public Member(Long memberNum, String loginId, String password, String name, Gender gender, LocalDate birthday, Boolean deleteYn, MemberRole role) {
        this.memberNum = memberNum;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.deleteYn = deleteYn;
        this.role = role;
    }
}
