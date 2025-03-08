package com.java360.pmanager.domain.applicationservice;

import com.java360.pmanager.domain.entity.Member;
import com.java360.pmanager.domain.exception.MemberNotFoundException;
import com.java360.pmanager.domain.repository.MemberRepository;
import com.java360.pmanager.infrastructure.dto.SaveMemberDataDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(SaveMemberDataDTO saveMemberData) {
        Member member = Member
            .builder()
            .name(saveMemberData.getName())
            .email(saveMemberData.getEmail())
            .secret(UUID.randomUUID().toString())
            .deleted(false)
            .build();

        memberRepository.save(member);

        return member;
    }

    public Member loadMemberById(String memberId) {
        return memberRepository
            .findByIdAndDeleted(memberId, false)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional
    public void deleteMember(String memberId) {
        Member member = loadMemberById(memberId);
        member.setDeleted(true);
    }

}
