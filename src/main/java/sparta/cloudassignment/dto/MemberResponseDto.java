package sparta.cloudassignment.dto;

import lombok.Builder;
import lombok.Getter;
import sparta.cloudassignment.entity.Member;

@Getter
public class MemberResponseDto {
    private Long id;
    private int age;
    private String name;
    private String mbti;

    private MemberResponseDto(Long id, int age, String name, String mbti) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.mbti = mbti;
    }

    public static MemberResponseDto toDto(Member member){
        return new MemberResponseDto(member.getId(),member.getAge(),member.getName(),member.getMbti());
    }

}
