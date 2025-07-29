package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Appointments;
import health.care.medicore.Repositories.AppointmentsRepository;
import health.care.medicore.ResponseDTO.Patient.PatientAppointment;
import health.care.medicore.Services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointments> implements AppointmentService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

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
}
