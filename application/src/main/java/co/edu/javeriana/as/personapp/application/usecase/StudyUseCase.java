package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        log.debug("Into create on Application Domain");
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(StudyId studyid, Study study) throws NoExistException {
        Study oldStudy = studyPersistence.findById(studyid);
        if (oldStudy != null)
            return studyPersistence.save(study);
        throw new NoExistException(
                "The study with idProf " + studyid.getPersonId() + " and idPer " + studyid.getProfessionId()
                        + " does not exist into db, cannot be edited");
    }

    @Override
    public Boolean drop(StudyId studyid) throws NoExistException {
        Study oldStudy = studyPersistence.findById(studyid);
        if (oldStudy != null)
            return studyPersistence.delete(studyid);
        throw new NoExistException(
                "The study with idProf " + studyid.getPersonId() + " and idPer " + studyid.getProfessionId()
                        + " does not exist into db, cannot be deleted");
    }

    @Override
    public List<Study> findAll() {
        log.info("Output: " + studyPersistence.getClass());
        return studyPersistence.find();
    }

    @Override
    public Study findOne(StudyId studyId) throws NoExistException {
        Study oldStudy = studyPersistence.findById(studyId);
        if (oldStudy != null)
            return oldStudy;
        throw new NoExistException(
                "The study with idProf " + studyId.getPersonId() + " and idPer " + studyId.getProfessionId()
                        + " does not exist into db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> findAllStudies() {
        throw new UnsupportedOperationException("Unimplemented method 'findAllStudies'");
    }

}
