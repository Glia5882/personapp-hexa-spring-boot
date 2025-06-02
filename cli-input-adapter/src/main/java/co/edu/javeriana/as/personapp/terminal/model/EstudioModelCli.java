package co.edu.javeriana.as.personapp.terminal.model;

import java.time.LocalDate;


import co.edu.javeriana.as.personapp.domain.StudyId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioModelCli {
    private Integer person;
    private Integer profession;
    private LocalDate graduationDate;
    private String universityName;

      public StudyId getStudyId() {
        return new StudyId(person, profession);
    }
}
