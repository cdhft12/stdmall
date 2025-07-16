package com.std.stdmall.member.domain;

import com.std.stdmall.common.BaseTimeEntity;
import com.std.stdmall.common.Gender;
import com.std.stdmall.common.MemberRole;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails {
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
    @Column(name = "birth")
    private LocalDate birth;  // 생년월일
    @Column(name = "delete_yn")
    private Boolean deleteYn;    // 삭제 여부
    @Column(name = "role")
    private MemberRole role;

    @Builder
    public Member(Long memberNum, String loginId, String password, String name, Gender gender, LocalDate birth, Boolean deleteYn, MemberRole role) {
        this.memberNum = memberNum;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.deleteYn = deleteYn;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));//일단 단일 권한만리턴 추후 수정
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    /**
     * 계정의 만료 여부를 반환합니다.
     * true를 반환하면 계정이 만료되지 않았음을 나타냅니다. (대부분의 경우 true)
     *
     * @return 계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않음
    }

    /**
     * 계정의 잠금 여부를 반환합니다.
     * true를 반환하면 계정이 잠기지 않았음을 나타냅니다. (대부분의 경우 true)
     *
     * @return 계정 잠금 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않음
    }

    /**
     * 자격 증명(비밀번호)의 만료 여부를 반환합니다.
     * true를 반환하면 자격 증명이 만료되지 않았음을 나타냅니다. (대부분의 경우 true)
     *
     * @return 자격 증명 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않음
    }

    /**
     * 계정의 활성화 여부를 반환합니다.
     * true를 반환하면 계정이 활성화되었음을 나타냅니다.
     * (예: 이메일 인증 후 활성화되는 경우, 여기서는 기본적으로 활성화 상태)
     *
     * @return 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화됨
    }

}
