package health.care.medicore.Services;

import health.care.medicore.RequestDTO.Patient.BookAppointment;
import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllTimeSlots;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;

import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    List<PatientAppointment> getTop1AppointmentsByPatientId(long patientId);

    long totalNumberOfAppointmentsByPatientId(long patientId);

    BaseResponse<List<GetAllTimeSlots>> getAvailableTimeSlots(GetAvailableTimeSlots getAvailableTimeSlots) throws Exception;

    void bookAppointment(BookAppointment  bookAppointment) throws Exception;
}
