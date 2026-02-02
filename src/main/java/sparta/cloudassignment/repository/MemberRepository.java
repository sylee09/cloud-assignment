package sparta.cloudassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.cloudassignment.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Modifying
    @Query(value = "update Member m set m.fileUrl=:fileUrl where m.id=:id")
    void updateFileUrl(@Param("fileUrl") String fileUrl, @Param("id") Long id);

    @Query("select m.fileUrl from Member m where m.id=:id")
    String getFileUrl(@Param("id") Long id);
}
