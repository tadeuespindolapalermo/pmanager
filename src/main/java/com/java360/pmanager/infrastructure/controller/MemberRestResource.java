package com.java360.pmanager.infrastructure.controller;

import com.java360.pmanager.domain.applicationservice.MemberService;
import com.java360.pmanager.domain.entity.Member;
import com.java360.pmanager.domain.entity.Project;
import com.java360.pmanager.infrastructure.dto.MemberDTO;
import com.java360.pmanager.infrastructure.dto.ProjectDTO;
import com.java360.pmanager.infrastructure.dto.SaveMemberDataDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.java360.pmanager.infrastructure.controller.RestConstants.PATH_MEMBERS;

@RestController
@RequestMapping(PATH_MEMBERS)
@RequiredArgsConstructor
public class MemberRestResource {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody @Valid SaveMemberDataDTO saveMemberData) {
        Member member = memberService.createMember(saveMemberData);

        return ResponseEntity
            .created(URI.create(PATH_MEMBERS + "/" + member.getId()))
            .body(MemberDTO.create(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> loadMemberById(@PathVariable("id") String memberId) {
        Member member = memberService.loadMemberById(memberId);
        return ResponseEntity.ok(MemberDTO.create(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") String memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

}
