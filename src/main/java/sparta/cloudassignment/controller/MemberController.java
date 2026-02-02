package sparta.cloudassignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sparta.cloudassignment.dto.MemberRequestDto;
import sparta.cloudassignment.dto.MemberResponseDto;
import sparta.cloudassignment.entity.Member;
import sparta.cloudassignment.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto requestDto){
        return memberService.saveMember(requestDto);
    }
}
