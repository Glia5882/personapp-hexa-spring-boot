package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;

@Port
public interface StudyOutputPort {
    public Study save(Study study);
    public Boolean delete(StudyId studyId);
    public List<Study> find();
    public List<Study> findAllStudies();
    public Study findById(StudyId studyId);

}
