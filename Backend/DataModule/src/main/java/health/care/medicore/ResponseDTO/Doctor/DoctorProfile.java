package health.care.medicore.ResponseDTO.Doctor;

import health.care.medicore.ResponseDTO.Patient.PatientProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfile extends PatientProfile {

    private LocalTime workingHourStart;

    private LocalTime workingHourEnd;

    private long consultationRates;

    public DoctorProfile(String email, String fullName, LocalTime workingHourStart, LocalTime workingHourEnd, long consultationRates) {
        super(email, fullName);
        this.workingHourStart = workingHourStart;
        this.workingHourEnd = workingHourEnd;
        this.consultationRates = consultationRates;
    }
}
