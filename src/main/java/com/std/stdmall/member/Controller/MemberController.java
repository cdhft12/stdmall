package com.std.stdmall.member.Controller;

import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity signUp(@Validated MemberSignUpReqDTO reqDTO) {
        MemberSignUpResDTO memberSignUpRes = memberService.signUpMember(reqDTO);
        System.out.println("test");
        return new ResponseEntity<>("사용자 생성 성공: " + reqDTO.getLoginId(), HttpStatus.CREATED);
    }

}
