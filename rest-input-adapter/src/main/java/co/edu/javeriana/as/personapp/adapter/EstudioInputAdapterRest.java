package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfesionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfesionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("profesionOutputAdapterMaria")
    private ProfesionOutputPort profesionOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("profesionOutputAdapterMongo")
    private ProfesionOutputPort profesionOutputPortMongo;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private EstudioMapperRest estudiosMapperRest;

    PersonInputPort personInputPort;

    ProfesionInputPort profesionInputPort;

    StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMaria);
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMongo);
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<EstudioResponse> historial(String database) {
        log.info("Into historial EstudiosEntity in Input Adapter");
        try {
            if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream().map(estudiosMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<EstudioResponse>();
        }
    }

    public EstudioResponse crearEstudios(EstudioRequest request) throws NumberFormatException {
        try {
            String database = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getCcPerson()));
            Profesion profesion = profesionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
    
            if (person == null || profesion == null) {
                throw new IllegalArgumentException("Person or Profession not found");
            }
    
            Study study = studyInputPort.create(estudiosMapperRest.fromAdapterToDomain(request, person, profesion));
    
            if (study == null) {
                throw new IllegalArgumentException("Failed to create study");
            }
    
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException | NoExistException | IllegalArgumentException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "Error: " + e.getMessage());
        }
    }
    

    public EstudioResponse obtenerEstudios(String database, StudyId studyid) {
        log.info("Into obtenerEstudios EstudiosEntity in Input Adapter");
        try {
            if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(
                        studyInputPort.findOne(studyid));
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(
                        studyInputPort.findOne(studyid));
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "");
        }
    }

    public EstudioResponse editarEstudios(EstudioRequest request) {
        log.info("Into editarEstudios EstudiosEntity in Input Adapter");
        StudyId studyid = new StudyId();
        try {
            String database = setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.valueOf(request.getCcPerson()));
            Profesion profesion = profesionInputPort.findOne(Integer.valueOf(request.getProfessionId()));
            Study phone = studyInputPort.edit((studyid),
                    estudiosMapperRest.fromAdapterToDomain(request, person, profesion));
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return estudiosMapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return estudiosMapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "");
        } catch (NumberFormatException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "");
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(null, null, "", "", "", "");
        }
    }

    public Boolean eliminarEstudios(String database, StudyId studyid) {
        log.info("Into eliminarEstudios EstudiosEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(database);
            return studyInputPort.drop(studyid);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return false;
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

}
