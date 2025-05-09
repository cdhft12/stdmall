package com.std.stdmall.member.service;

import com.std.stdmall.member.dto.CustomUserDetail;
import com.std.stdmall.member.dto.MemberSignInResDTO;
import com.std.stdmall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberSignInResDTO resDTO = memberRepository.findByLoginId(username);
        if (resDTO != null) {
            return new CustomUserDetail(resDTO);
        }
        return null;
    }
}
