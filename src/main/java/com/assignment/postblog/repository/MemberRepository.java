package com.assignment.postblog.repository;

import com.assignment.postblog.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository <Member, Long> {
    Optional<Member> findByNickname(String nickname);
}
