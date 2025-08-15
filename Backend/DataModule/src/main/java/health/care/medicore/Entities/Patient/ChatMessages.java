package health.care.medicore.Entities.Patient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_MESSAGES")
@Getter
@Setter
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CHAT_MESSAGE_ID")
    private long chatMessageId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "ROLE")
    private String role; // "user" or "assistant"

    @Column(name = "CONTENT", length = 65535)
    private String content;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}
