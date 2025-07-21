package health.care.medicore.Repositories;

import health.care.medicore.Entities.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {

}
