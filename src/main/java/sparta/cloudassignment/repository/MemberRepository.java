package sparta.cloudassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.cloudassignment.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
