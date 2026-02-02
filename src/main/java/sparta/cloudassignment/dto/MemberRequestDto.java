package sparta.cloudassignment.dto;

import lombok.Getter;
import sparta.cloudassignment.entity.Member;

@Getter
public class MemberRequestDto {
    private String name;
    private int age;
    private String mbti;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .mbti(mbti)
                .build();
    }
}
