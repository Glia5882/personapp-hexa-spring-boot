package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
@Mapper
public class EstudiosMapperMaria {

    @Autowired
    private PersonaMapperMaria personaMapperMaria;
    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    public EstudiosEntity fromDomainToAdapter(Study study, boolean loadDeep) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getId());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);		

        estudio.setFecha(validateFecha(study.getGraduationDate()));
        estudio.setUniver(study.getUniversityName());

        if (loadDeep) {
            estudio.setPersona(personaMapperMaria.fromDomainToAdapter(study.getPerson(), true));
            estudio.setProfesion(profesionMapperMaria.fromDomainToAdapter(study.getProfession(), true));
        }

        return estudio;
    }

    public Study fromAdapterToDomain(EstudiosEntity estudiosEntity, boolean loadDeep) {
        Study study = new Study();
        if (loadDeep) {
            study.setPerson(personaMapperMaria.fromAdapterToDomain(estudiosEntity.getPersona(), true));
            study.setProfession(profesionMapperMaria.fromAdapterToDomain(estudiosEntity.getProfesion(), true));
        }
        study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
        study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));

        return study;
    }

	   public List<EstudiosEntity> validateEstudios(List<Study> studies, boolean loadDeep) {
        return studies.stream()
                      .map(study -> this.fromDomainToAdapter(study, true))
                      .collect(Collectors.toList());
    }

	public List<Study> validateStudies(List<EstudiosEntity> estudiosEntities, boolean loadDeep) {
    if (estudiosEntities == null) return new ArrayList<>();
    return estudiosEntities.stream()
                           .map(estudiosEntity -> this.fromAdapterToDomain(estudiosEntity, false))
                           .collect(Collectors.toList());
}


    private Date validateFecha(LocalDate graduationDate) {
        return Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate validateGraduationDate(Date fecha) {
        if (fecha != null) {
            return new java.sql.Date(fecha.getTime()).toLocalDate();
        }
        return null;
    }

    private String validateUniversityName(String univer) {
        return univer != null ? univer : "";
    }
}
