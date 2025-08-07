package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppointmentsRepository;
import health.care.medicore.RequestDTO.Patient.BookAppointment;
import health.care.medicore.RequestDTO.Patient.CancelAppointment;
import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Patient.GetAllTimeSlots;
import health.care.medicore.ResponseDTO.Patient.MyAppointments;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    @Transactional
    public void bookAppointment(BookAppointment bookAppointment) throws Exception {
        try{
            Users patient = userService.getCurrentUser();
            Users doctor = userService.getUserByEmail(bookAppointment.getDoctorEmail()).get();
            for (int i = 0; i < bookAppointment.getAppointmentTimes().size(); i++) {
                // Checking if appointment is already booked
                if (appointmentsRepository.isAppointmentAvailable(bookAppointment.getAppointmentDate(), doctor, bookAppointment.getAppointmentTimes().get(i).getValue()).isPresent()){
                    throw new Exception("Appointment does not available on " + bookAppointment.getAppointmentTimes().get(i).getValue() + ". Please try different time");
                }
                Appointments appointments = new  Appointments();
                appointments.setAppointmentDate(bookAppointment.getAppointmentDate());
                appointments.setAppointmentDuration(30);
                appointments.setAppointmentStartTime(bookAppointment.getAppointmentTimes().get(i).getValue());
                appointments.setDoctor(doctor);
                appointments.setPatient(patient);
                appointmentsRepository.save(appointments);
            }
        }catch (Exception e){
            throw new  Exception("Failed to book Appointment. May be the appointment is already booked on that time slot. Try refreshing page!");
        }
    }

    @Override
    public BaseResponse<List<MyAppointments>> getAllAppointments() throws Exception {
        try{
            Users user = userService.getCurrentUser();
            List<MyAppointments> myAppointments = appointmentsRepository.getAllAppointments(user);
            return new BaseResponse<>("Appointments Fetched", myAppointments);
        }catch (Exception e){
            throw new  Exception("Failed to get All Appointments");
        }
    }

    @Override
    public BaseResponse<List<MyAppointments>> cancelAppointment(CancelAppointment cancelAppointment) throws Exception {
        Appointments appointments = appointmentsRepository.findById(cancelAppointment.getAppointmentId()).get();
        boolean isCancellable = LocalDate.now().isBefore(appointments.getAppointmentDate()) && LocalDateTime.now().plusHours(24).isBefore(LocalDateTime.of(appointments.getAppointmentDate(), appointments.getAppointmentStartTime()));
        if (!isCancellable) {
            throw new Exception("Appointment cannot be cancelled as either it is passed or within 24 hours");
        }
        appointmentsRepository.delete(appointments);
        BaseResponse<List<MyAppointments>> listBaseResponse = getAllAppointments();
        listBaseResponse.setResponseMessage("Appointment Cancelled Successfully");
        return listBaseResponse;
    }
}
