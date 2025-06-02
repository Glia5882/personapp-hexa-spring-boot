package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;
import com.mongodb.lang.NonNull;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudioRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudioRepositoryMongo estudioRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        try {
            EstudiosDocument persistedPersona = estudioRepositoryMongo
                    .save(estudiosMapperMongo.fromDomainToAdapter(study));
            return estudiosMapperMongo.fromAdapterToDomain(persistedPersona);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return study;
        }
    }

    @Override
    public Boolean delete(StudyId studyId) {
        log.debug("Into delete on Adapter MongoDB");
        estudioRepositoryMongo.deleteById(validateId(studyId.getPersonId(),studyId.getProfessionId()));
        return estudioRepositoryMongo.findById(validateId(studyId.getPersonId(),studyId.getProfessionId())).isEmpty();

    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return estudioRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(StudyId studyId) {
        log.debug("Into findById on Adapter MongoDB");
        if (estudioRepositoryMongo.findById(validateId(studyId.getPersonId(),studyId.getProfessionId())).isEmpty()) {
            return null;
        } else {
            return estudiosMapperMongo.fromAdapterToDomain(
                    estudioRepositoryMongo.findById(validateId(studyId.getPersonId(),studyId.getProfessionId())).get());
        }
    }

    private String validateId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
        return identificationPerson + "-" + identificationProfession;
    }

  
    @Override
    public List<Study> findAllStudies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllStudies'");
    }

}
