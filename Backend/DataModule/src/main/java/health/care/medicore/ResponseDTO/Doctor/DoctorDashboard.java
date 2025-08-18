package health.care.medicore.ResponseDTO.Doctor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorDashboard {

    List<DoctorAppointment> doctorAppointments;
    long totalAppointments;
    long credits;
}
