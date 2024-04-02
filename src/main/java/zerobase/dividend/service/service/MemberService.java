package zerobase.dividend.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.dividend.service.entity.MemberEntity;
import zerobase.dividend.service.exception.NoCompanyException;
import zerobase.dividend.service.model.Auth;
import zerobase.dividend.service.repository.MemberRepository;

import java.rmi.AlreadyBoundException;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
    }

    public MemberEntity register(Auth.SignUp member) throws AlreadyBoundException {
        boolean exists = memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyBoundException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(Auth.SignIn member) {
        MemberEntity memberEntity = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID입니다."));

        if (!passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())) {
            throw new RuntimeException("비밀번호 일치 x");
        }

        return memberEntity;
    }
}
