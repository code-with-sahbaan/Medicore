package health.care.medicore.Services;

import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;

import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    List<PatientAppointment> getTop1AppointmentsByPatientId(long patientId);

    long totalNumberOfAppointmentsByPatientId(long patientId);

    BaseResponse<List<LocalTime>> getAvailableTimeSlots(GetAvailableTimeSlots getAvailableTimeSlots) throws Exception;
}
