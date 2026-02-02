package sparta.cloudassignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sparta.cloudassignment.dto.MemberRequestDto;
import sparta.cloudassignment.dto.MemberResponseDto;
import sparta.cloudassignment.entity.Member;
import sparta.cloudassignment.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.saveMember(requestDto);
    }

    @GetMapping("/api/members/{id}")
    public MemberResponseDto getMember(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }
}
