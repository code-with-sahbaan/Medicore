package health.care.medicore.ServicesImpl.Patient;

import health.care.medicore.Entities.Patient.ChatMessages;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.Patient.ChatMessageRepository;
import health.care.medicore.RequestDTO.Patient.AiUserMessage;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.AiAssistantResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllChatMessages;
import health.care.medicore.Services.Patient.ChatService;
import health.care.medicore.Services.UserService;
import health.care.medicore.ServicesImpl.GenericServiceImpl;
import health.care.medicore.Utils.Constants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl extends GenericServiceImpl<ChatMessages> implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    public static final int MESSAGE_LIMIT = 10;

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClient) {
        super(ChatMessages.class);
        this.chatClient = chatClient.defaultSystem("You are a AI Doctor for checking all of the symptoms and suggest medicines or treatment").build();
    }

    @Override
    public BaseResponse<AiAssistantResponse> getAnswer(AiUserMessage aiUserMessage) throws Exception {

        try{
            Users users = userService.getCurrentUser();
            // Saving new message to DB
            ChatMessages chatMessages = new ChatMessages();
            chatMessages.setContent(aiUserMessage.getMessage());
            chatMessages.setCreatedAt(LocalDateTime.now());
            chatMessages.setRole(Constants.CHAT_USER_ROLE);
            chatMessages.setUserId(users.getUserId());
            chatMessageRepository.save(chatMessages);

            // fetching last 10 user and assistant messages
            List<ChatMessages> chatMessagesList = chatMessageRepository.getLastUserChatMessages(users.getUserId(), MESSAGE_LIMIT * 2);

            // converting messages to AI messages
            List<Message> messages = new ArrayList<>(chatMessagesList.stream().map(chatMessages1 ->
                    chatMessages1.getRole().equals(Constants.CHAT_USER_ROLE) ? new UserMessage(chatMessages1.getContent()) : new AssistantMessage(chatMessages1.getContent())
            ).toList());

            // Getting the response from AI
            String reply = chatClient.prompt().messages(messages).user(aiUserMessage.getMessage()).call().content();

            // Saving reply to DB
            ChatMessages assistantMessage = new ChatMessages();
            assistantMessage.setContent(reply);
            assistantMessage.setCreatedAt(LocalDateTime.now());
            assistantMessage.setRole(Constants.CHAT_ASSISTANT_ROLE);
            assistantMessage.setUserId(users.getUserId());
            chatMessageRepository.save(assistantMessage);

            AiAssistantResponse aiAssistantResponse = new AiAssistantResponse();
            aiAssistantResponse.setReply(reply);
            return new BaseResponse<>("Response Get Successfully", aiAssistantResponse);
        } catch (Exception e) {
            throw new Exception("Failed to get response from Symptom Checker");
        }
    }

    @Override
    public BaseResponse<List<GetAllChatMessages>> getAllChatMessages() throws Exception {
        Users users = userService.getCurrentUser();
        List<GetAllChatMessages> getAllChatMessagesList = chatMessageRepository.getAllChatMessages(users.getUserId());
        return new BaseResponse<>("Messages fetched successfully", getAllChatMessagesList);
    }
}
