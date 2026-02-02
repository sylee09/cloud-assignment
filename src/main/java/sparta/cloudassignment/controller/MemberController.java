package sparta.cloudassignment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sparta.cloudassignment.dto.MemberRequestDto;
import sparta.cloudassignment.dto.MemberResponseDto;
import sparta.cloudassignment.entity.Member;
import sparta.cloudassignment.service.MemberService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto requestDto) {
        log.info("[API-LOG] save member request");
        return memberService.saveMember(requestDto);
    }

    @GetMapping("/api/members/{id}")
    public MemberResponseDto getMember(@PathVariable Long id) {
        log.info("[API-LOG] get member request");
        return memberService.getMemberById(id);
    }
}
