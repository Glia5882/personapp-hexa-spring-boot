package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;

@Port
public interface StudyInputPort {
    
    public void setPersistence(StudyOutputPort studyPersistence);
    
    public Study create(Study study);

    public Study edit(StudyId studyId, Study study) throws NoExistException;

    public Boolean drop(StudyId studyId) throws NoExistException;

    public List<Study> findAll();

    public List<Study> findAllStudies();

    public Study findOne(StudyId studyId) throws NoExistException;


    public Integer count();
}
