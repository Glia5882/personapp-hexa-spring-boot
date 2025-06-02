package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMaria estudiosRepositoryMaria;
    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;
    @Autowired
    private PersonaRepositoryMaria personaRepository;
    @Autowired
    private ProfesionRepositoryMaria profesionRepository;

    @Override
    public Study save(Study study) {
        log.debug("Saving study with person ID: {} and profession ID: {}", study.getPerson().getIdentification(), study.getProfession().getId());
        if (!personaRepository.existsById(study.getPerson().getIdentification()) || !profesionRepository.existsById(study.getProfession().getId())) {
            log.error("Person or Profession does not exist, cannot save study");
            throw new IllegalStateException("Referenced Person or Profession does not exist");
        }        EstudiosEntity estudioEntity = estudiosMapperMaria.fromDomainToAdapter(study, false); 
        EstudiosEntity persistedEstudio = estudiosRepositoryMaria.save(estudioEntity);
        return estudiosMapperMaria.fromAdapterToDomain(persistedEstudio, false);  
    }

    @Override
    public Boolean delete(StudyId studyId) {
        EstudiosEntityPK pk = new EstudiosEntityPK(studyId.getPersonId(), studyId.getProfessionId());
        estudiosRepositoryMaria.deleteById(pk);
        return !estudiosRepositoryMaria.existsById(pk);
    }

    @Override
    public List<Study> find() {
        return estudiosRepositoryMaria.findAll().stream()
                .map(estudiosEntity -> estudiosMapperMaria.fromAdapterToDomain(estudiosEntity, true))  
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(StudyId studyId) {
        EstudiosEntityPK pk = new EstudiosEntityPK(studyId.getPersonId(), studyId.getProfessionId());
        log.info("Looking for study with persona ID: {} and profession ID: {}", studyId.getPersonId(), studyId.getProfessionId());

        return estudiosRepositoryMaria.findById(pk)
                .map(estudiosEntity -> estudiosMapperMaria.fromAdapterToDomain(estudiosEntity, true))  
                .orElse(null);
    }

    @Override
    public List<Study> findAllStudies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllStudies'");
    }
}
