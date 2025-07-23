package health.care.medicore.ResponseDTO.Patient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Dashboard {

    List<PatientAppointment> patientAppointments;
    long totalAppointments;
    long credits;

}
