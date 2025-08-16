package health.care.medicore.ResponseDTO.Doctor;

import health.care.medicore.ResponseDTO.Patient.PatientProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProfile extends PatientProfile {

    private LocalDateTime workingHourStart;

    private LocalDateTime workingHourEnd;

    private long consultationRates;

    public DoctorProfile(String email, String fullName, LocalTime workingHourStart, LocalTime workingHourEnd, long consultationRates) {
        super(email, fullName);
        this.workingHourStart = LocalDateTime.of(LocalDate.now(), workingHourStart);
        this.workingHourEnd = LocalDateTime.of(LocalDate.now(), workingHourEnd);
        this.consultationRates = consultationRates;
    }
}
