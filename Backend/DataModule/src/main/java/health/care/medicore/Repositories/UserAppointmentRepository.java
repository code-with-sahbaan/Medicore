package health.care.medicore.Repositories;

import health.care.medicore.Entities.UserAppointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAppointmentRepository extends JpaRepository<UserAppointments, Long> {
}
