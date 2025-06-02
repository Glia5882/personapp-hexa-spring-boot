package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {

    public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
        return new EstudioResponse(
                study.getPerson().getIdentification() + "",
                study.getProfession().getId() + "",
                study.getGraduationDate() + "",
                study.getUniversityName(),
                database,
                "OK");
    }

    public Study fromAdapterToDomain(EstudioRequest request, Person person, Profesion profesion) {
        if (person == null || profesion == null) {
            throw new IllegalArgumentException("Person and Profession must not be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new Study(
            person,
            profesion,
            LocalDate.parse(request.getGraduationDate(), formatter),
            request.getUniversityName()
        );
    }

}
