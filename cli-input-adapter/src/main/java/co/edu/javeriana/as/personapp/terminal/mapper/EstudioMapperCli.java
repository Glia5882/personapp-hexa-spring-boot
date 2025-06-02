package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

    public EstudioModelCli fromDomainToAdapterCli(Study study) {
        EstudioModelCli estudiosModelCli = new EstudioModelCli();
        estudiosModelCli.setPerson(study.getPerson().getIdentification());
        estudiosModelCli.setProfession(study.getProfession().getId());
        estudiosModelCli.setGraduationDate(study.getGraduationDate());
        estudiosModelCli.setUniversityName(study.getUniversityName());
        return estudiosModelCli;
    }

    public Study fromAdapterToDomain(EstudioModelCli estudiosModelCli, Person person, Profesion profession) {
        Study study = new Study();
        study.setPerson(person);
        study.setProfession(profession);
        study.setGraduationDate(estudiosModelCli.getGraduationDate());
        study.setUniversityName(estudiosModelCli.getUniversityName());
        return study;
    }

}
