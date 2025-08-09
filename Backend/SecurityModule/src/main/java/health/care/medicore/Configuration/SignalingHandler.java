package health.care.medicore.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler extends TextWebSocketHandler {
    // roomId -> List<WebSocketSession> (max 2)
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Optionally validate JWT from query param: session.getUri().getQuery()
        // or from headers: session.getHandshakeHeaders()
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = message.getPayload();
        var node = new ObjectMapper().readTree(payload);
        var type = node.get("type").asText();

        switch (type) {
            case "join" -> {
                String room = node.get("roomId").asText();
                rooms.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
                // Optionally respond with current participants count
            }
            case "leave" -> {
                String room = node.get("roomId").asText();
                leaveRoom(session, room);
            }
            case "offer", "answer", "ice" -> {
                // forward to the other participant(s) in same room
                String roomId = null;
                if (node.has("roomId")) {
                    roomId = node.get("roomId").asText();
                } else if (node.has("rid")) {
                    roomId = node.get("rid").asText();
                }
                for (WebSocketSession s : rooms.getOrDefault(roomId, Set.of())) {
                    if (!s.equals(session) && s.isOpen()) {
                        s.sendMessage(message);
                    }
                }
            }
            default -> {
                // ignore/handle
            }
        }
    }

    private void leaveRoom(WebSocketSession session, String room) {
        Set<WebSocketSession> set = rooms.get(room);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) rooms.remove(room);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // cleanup: remove session from any room
        rooms.values().forEach(set -> set.remove(session));
        rooms.entrySet().removeIf(e -> e.getValue().isEmpty());
    }
}

