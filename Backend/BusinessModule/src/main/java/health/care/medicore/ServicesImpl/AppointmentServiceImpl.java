package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Entities.Users;
import health.care.medicore.Repositories.AppointmentsRepository;
import health.care.medicore.RequestDTO.Patient.BookAppointment;
import health.care.medicore.RequestDTO.Patient.CancelAppointment;
import health.care.medicore.RequestDTO.Patient.GetAvailableTimeSlots;
import health.care.medicore.RequestDTO.SendEmail;
import health.care.medicore.ResponseDTO.BaseResponse;
import health.care.medicore.ResponseDTO.Doctor.DoctorAppointment;
import health.care.medicore.ResponseDTO.Patient.GetAllTimeSlots;
import health.care.medicore.ResponseDTO.Patient.MyAppointments;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import health.care.medicore.Services.AppointmentService;
import health.care.medicore.Services.QueueService;
import health.care.medicore.Services.UserService;
import health.care.medicore.Utils.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointments> implements AppointmentService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QueueService queueService;

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
    public List<DoctorAppointment> getTop1AppointmentsByDoctorId(long doctorId) {
        Pageable pageable = PageRequest.of(0,1);
        Page<DoctorAppointment> doctorAppointments = appointmentsRepository.getTop1AppointmentsByDoctorId(doctorId, LocalDate.now(), LocalTime.now(), pageable);
        return doctorAppointments.getContent();
    }

    @Override
    public long totalNumberOfAppointmentsByPatientId(long patientId) {
        return appointmentsRepository.totalNumberOfAppointmentsByPatientId(patientId);
    }

    @Override
    public long totalNumberOfAppointmentsByDoctorId(long patientId) {
        return appointmentsRepository.totalNumberOfAppointmentsByDoctorId(patientId);
    }

    @Override
    public BaseResponse<List<GetAllTimeSlots>> getAvailableTimeSlots(GetAvailableTimeSlots getAvailableTimeSlots) throws Exception {
        try{
            Users doctor = userService.getUserByEmail(getAvailableTimeSlots.getDoctorEmail()).get();
            Set<LocalTime> bookedSlots = appointmentsRepository.getAppointmentTimesByDoctorId(doctor, getAvailableTimeSlots.getAppointmentDate());
            LocalTime startTime = doctor.getWorkingHourStart();
            LocalTime endTime = doctor.getWorkingHourEnd();
            List<GetAllTimeSlots> availableSlots = new ArrayList<>();

            if (getAvailableTimeSlots.getAppointmentDate().isBefore(LocalDate.now())){
                return new BaseResponse<>("Time Slots Fetched", availableSlots);
            }

            while (!startTime.plusMinutes(30).isAfter(endTime)) {

                // If the Day the is Today then check if the start time has already passed or not
                if (getAvailableTimeSlots.getAppointmentDate().equals(LocalDate.now())) {
                    if (startTime.isBefore(LocalTime.now())) {
                        startTime = startTime.plusMinutes(30);
                        continue;
                    }
                }

                // Else check for other dates
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
        Users patient = userService.getCurrentUser();
        Users doctor = userService.getUserByEmail(bookAppointment.getDoctorEmail()).get();
        int successfulAppointments = 0;
        for (int i = 0; i < bookAppointment.getAppointmentTimes().size(); i++) {
            // Checking if appointment is already booked
            if (appointmentsRepository.isAppointmentAvailable(bookAppointment.getAppointmentDate(), doctor, bookAppointment.getAppointmentTimes().get(i).getValue()).isPresent()){
                throw new Exception("Appointment does not available on " + bookAppointment.getAppointmentTimes().get(i).getValue() + ". Please try different time");
            }
            if (patient.getCredits() < doctor.getConsultationRates()){
                String message = successfulAppointments + " Appointments has been added successfully, but others did not. Reason: Insufficient funds to book appointment";
                throw new Exception(message);
            }
            Appointments appointments = new  Appointments();
            appointments.setAppointmentDate(bookAppointment.getAppointmentDate());
            appointments.setAppointmentDuration(30);
            appointments.setAppointmentStartTime(bookAppointment.getAppointmentTimes().get(i).getValue());
            appointments.setDoctor(doctor);
            appointments.setPatient(patient);
            appointments.setAppointmentCharges(doctor.getConsultationRates());
            appointmentsRepository.save(appointments);
            long remainingCredits = patient.getCredits() - doctor.getConsultationRates();
            patient.setCredits(remainingCredits);
            userService.updateUser(patient);
            successfulAppointments += 1;

            // Sending Emails to Doctor and Patient
            Map<String, String> content = new HashMap<>();
            content.put("doctor", doctor.getFullName());
            content.put("patient", patient.getFullName());
            content.put("dateTime", bookAppointment.getAppointmentDate().toString() + " - " + appointments.getAppointmentStartTime().toString());
            sendEmailForConfirmingOrCancellingAppointment(doctor, patient, "Appointment Confirmation", Constants.APPOINTMENT_CONFIRM_TEMPLATE_NAME, content);
        }
    }

    @Override
    public BaseResponse<List<MyAppointments>> getAllAppointments() throws Exception {
        try{
            Users user = userService.getCurrentUser();
            List<MyAppointments> myAppointments;
            if (user.getRole().getRole().equals(Constants.DOCTOR)){
                myAppointments = appointmentsRepository.getAllAppointmentsForDoctor(user);
            }else{
                myAppointments = appointmentsRepository.getAllAppointments(user);
            }
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

        Users users = appointments.getPatient();
        // Returning credits back to Users
        long updatedCredits = users.getCredits() + appointments.getAppointmentCharges();
        users.setCredits(updatedCredits);
        userService.updateUser(users);
        // Sending Emails to Doctor and Patient
        Users doctor = appointments.getDoctor();
        Users patient = appointments.getPatient();
        Map<String, String> content = new HashMap<>();
        content.put("doctor", doctor.getFullName());
        content.put("patient", patient.getFullName());
        content.put("dateTime", appointments.getAppointmentDate().toString() + " - " + appointments.getAppointmentStartTime().toString());
        sendEmailForConfirmingOrCancellingAppointment(doctor, patient, "Appointment Cancellation", Constants.APPOINTMENT_CANCEL_TEMPLATE_NAME, content);
        // Deleting the Appointment
        appointmentsRepository.delete(appointments);
        BaseResponse<List<MyAppointments>> listBaseResponse = getAllAppointments();
        listBaseResponse.setResponseMessage("Appointment Cancelled Successfully");
        return listBaseResponse;
    }

    @Override
    public List<Appointments> getAllTodayAppointments() {
        return appointmentsRepository.getAllTodayAppointments(LocalDate.now(), LocalTime.now());
    }

    @Override
    public void updateAppointment(Appointments appointments) throws Exception {
        appointmentsRepository.save(appointments);
    }

    @Override
    public List<Appointments> getAllPreviousDayAppointments() throws Exception {
        return appointmentsRepository.getAllPreviousDayAppointments(LocalDate.now().minusDays(1));
    }

    private void sendEmailForConfirmingOrCancellingAppointment(Users doctor, Users patient, String subject, String templateName, Map<String, String> content) throws Exception {
        for (int j = 0; j < 2; j++){
            // Sending Emails to Patient and Doctor both
            boolean isDoctor = j == 0;
            content.put("name", isDoctor ? doctor.getFullName() : patient.getFullName());
            SendEmail sendEmail = new SendEmail(
                    isDoctor ? doctor.getEmail() : patient.getEmail(),
                    subject,
                    templateName,
                    content
            );
            queueService.sendEmailToQueue(sendEmail);
        }
    }
}
