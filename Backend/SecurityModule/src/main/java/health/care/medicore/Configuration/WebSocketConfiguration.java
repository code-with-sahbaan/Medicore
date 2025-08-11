package health.care.medicore.Configuration;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Autowired
    private AppointmentService appointmentService;

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SignalingHandler(rooms, appointmentService), "/ws/signal")
                .setAllowedOrigins(allowedOrigins); // configure
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredCalls() throws IOException {
        List<Appointments> appointments = appointmentService.getAllTodayAppointments();
        for (int i = 0; i < appointments.size(); i++) {
            String roomId = String.valueOf(appointments.get(i).getAppointmentId());
            Set<WebSocketSession> webSocketSessions = rooms.get(roomId);
            if (webSocketSessions != null) {
                for (int j = 0; j < webSocketSessions.size(); j++) {
                    // Removing each participant from the call
                    for (WebSocketSession s : webSocketSessions) {
                        if (s.isOpen()) {
                            s.sendMessage(new TextMessage("{\"type\":\"endCall\"}"));
                            s.close(CloseStatus.NORMAL);
                            leaveRoom(s, roomId);
                        }
                    }
                }
            }
            // Ending the call and killing web socket session
            rooms.remove(String.valueOf(appointments.get(i)));
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
}
