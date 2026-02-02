package sparta.cloudassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.cloudassignment.dto.MemberRequestDto;
import sparta.cloudassignment.dto.MemberResponseDto;
import sparta.cloudassignment.entity.Member;
import sparta.cloudassignment.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto dto) {
        Member member = dto.toEntity();
        Member save = memberRepository.save(member);
        return MemberResponseDto.toDto(save);
    }

}
