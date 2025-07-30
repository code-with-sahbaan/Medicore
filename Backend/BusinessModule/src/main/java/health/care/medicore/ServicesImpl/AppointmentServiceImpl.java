package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppointmentsRepository;
import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllTimeSlots;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointments> implements AppointmentService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UserService userService;

    public AppointmentServiceImpl() {
        super(Appointments.class);
    }


    @Override
    public List<PatientAppointment> getTop1AppointmentsByPatientId(long patientId) {
        Pageable pageable = PageRequest.of(0,1);
        Page<PatientAppointment> patientAppointments = appointmentsRepository.getTop1AppointmentsByPatientId(patientId, LocalDate.now(), LocalTime.now(), pageable);
        return patientAppointments.getContent();
    }

    @Override
    public long totalNumberOfAppointmentsByPatientId(long patientId) {
        return appointmentsRepository.totalNumberOfAppointmentsByPatientId(patientId);
    }

    @Override
    public BaseResponse<List<GetAllTimeSlots>> getAvailableTimeSlots(GetAvailableTimeSlots getAvailableTimeSlots) throws Exception {
        try{
            Users doctor = userService.getUserByEmail(getAvailableTimeSlots.getDoctorEmail()).get();
            Set<LocalTime> bookedSlots = appointmentsRepository.getAppointmentTimesByDoctorId(doctor, getAvailableTimeSlots.getAppointmentDate());
            LocalTime startTime = doctor.getWorkingHourStart();
            LocalTime endTime = doctor.getWorkingHourEnd();
            List<GetAllTimeSlots> availableSlots = new ArrayList<>();

            while (!startTime.plusMinutes(30).isAfter(endTime)) {
                if (!bookedSlots.contains(startTime)) {
                    availableSlots.add(
                            new GetAllTimeSlots(startTime, startTime)
                    );
                }
                startTime = startTime.plusMinutes(30);
            }
            return new BaseResponse<>("Time Slots Fetched", availableSlots);
        }catch (Exception e){
            throw new  Exception("Failed to get Available Time Slots");
        }
    }
}
