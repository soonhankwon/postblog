package com.assignment.postblog.security;

import com.assignment.postblog.model.Member;
import com.assignment.postblog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
                () -> new UsernameNotFoundException("Can't find " + nickname));

        MemberDetailsImpl userDetails = new MemberDetailsImpl();
        userDetails.setMember(member);
        return userDetails;
    }
}
