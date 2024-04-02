package zerobase.dividend.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zerobase.dividend.service.entity.MemberEntity;
import zerobase.dividend.service.model.Auth;
import zerobase.dividend.service.security.TokenProvider;
import zerobase.dividend.service.service.MemberService;

import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp (@RequestBody Auth.SignUp request) throws AlreadyBoundException {
        MemberEntity register = memberService.register(request);

        return ResponseEntity.ok(register);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn (@RequestBody Auth.SignIn request) {
        MemberEntity authenticate = memberService.authenticate(request);
        String str = tokenProvider.generateToken(authenticate.getUsername(), Collections.singletonList(authenticate.getRole()));
        return ResponseEntity.ok(str);
    }
}
