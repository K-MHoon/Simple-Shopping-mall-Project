package com.shop.mall.service;

import com.shop.mall.entity.Member;
import com.shop.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
        log.info("findMember = {}", findMember);
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
