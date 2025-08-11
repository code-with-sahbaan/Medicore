package health.care.medicore.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import health.care.medicore.Entities.Appointments;
import health.care.medicore.Services.AppointmentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler extends TextWebSocketHandler {
    // roomId -> List<WebSocketSession> (max 2)
    private final Map<String, Set<WebSocketSession>> rooms;

    private final AppointmentService appointmentService;

    public SignalingHandler(Map<String, Set<WebSocketSession>> rooms, AppointmentService appointmentService) {
        this.rooms = rooms;
        this.appointmentService = appointmentService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = message.getPayload();
        var node = new ObjectMapper().readTree(payload);
        var type = node.get("type").asText();

        switch (type) {
            case "join" -> {
                String room = node.get("rid").asText();
                rooms.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
                // Optionally respond with current participants count
            }
            case "leave" -> {
                String room = node.get("rid").asText();
                leaveRoom(session, room);
            }
            case "offer", "answer", "ice", "prescription" -> {
                // forward to the other participant(s) in same room
                String room = node.get("rid").asText();
                for (WebSocketSession s : rooms.getOrDefault(room, Set.of())) {
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
            if (set.isEmpty()) {
                rooms.remove(room);
                // TODO: Add PDF Generation Logic for Prescription
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // cleanup: remove session from any room
        rooms.values().forEach(set -> set.remove(session));
        rooms.entrySet().removeIf(e -> e.getValue().isEmpty());
    }
}

