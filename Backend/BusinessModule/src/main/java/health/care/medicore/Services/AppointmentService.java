package health.care.medicore.Services;

import health.care.medicore.ResponseDTO.Patient.PatientAppointment;

import java.util.List;

public interface AppointmentService {

    List<PatientAppointment> getTop1AppointmentsByPatientId(long patientId);

    long totalNumberOfAppointmentsByPatientId(long patientId);

}
