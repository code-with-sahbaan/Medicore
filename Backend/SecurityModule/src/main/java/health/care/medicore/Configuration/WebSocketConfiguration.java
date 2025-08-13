package health.care.medicore.Configuration;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Users;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.UserService;
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

    @Autowired
    private UserService userService;

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SignalingHandler(rooms, appointmentService), "/ws/signal")
                .setAllowedOrigins(allowedOrigins); // configure
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredCalls() throws Exception {
        List<Appointments> appointments = appointmentService.getAllTodayAppointments();
        for (int i = 0; i < appointments.size(); i++) {
            String roomId = String.valueOf(appointments.get(i).getAppointmentId());
            Appointments appointment = appointments.get(i);
            Set<WebSocketSession> webSocketSessions = rooms.get(roomId);
            // Adding Amount to Doctor if the consultation is un-paid
            resolveUnpaidAppointments(appointment);
            // closing web sockets
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

    @Scheduled(cron = "0 10 0 * * *")
    public void resolvedLateUnpaidAppointments() throws Exception {
        List<Appointments> appointments = appointmentService.getAllPreviousDayAppointments();
        for(Appointments appointment : appointments) {
            resolveUnpaidAppointments(appointment);
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

    private void resolveUnpaidAppointments(Appointments appointment) throws Exception {
        if (!appointment.getIsPaid()){
            appointment.setIsPaid(true);
            appointmentService.updateAppointment(appointment);
            Users doctor = appointment.getDoctor();
            long updatedCredits = doctor.getCredits() + appointment.getAppointmentCharges();
            doctor.setCredits(updatedCredits);
            userService.updateUser(doctor);
        }
    }
}
