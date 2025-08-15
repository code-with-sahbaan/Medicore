package health.care.medicore.Repositories.Patient;

import health.care.medicore.Entities.Patient.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long> {

    @Query("SELECT cm FROM ChatMessages cm WHERE cm.userId = :userId ORDER BY cm.createdAt DESC LIMIT :limit")
    List<ChatMessages> getLastUserChatMessages(@Param("userId") long userId, @Param("limit") long limit);
}
