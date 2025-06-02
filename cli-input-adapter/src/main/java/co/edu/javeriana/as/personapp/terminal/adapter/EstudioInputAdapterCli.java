package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

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
    private EstudioMapperCli estudiosMapperCli;

    PersonInputPort personInputPort;

    ProfesionInputPort profesionInputPort;

    StudyInputPort studyInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMaria);
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMaria);
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            personInputPort = new PersonUseCase(personOutputPortMongo);
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMongo);
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial EstudiosModelCli in Input Adapter");
        studyInputPort.findAll().stream()
                .map(estudiosMapperCli::fromDomainToAdapterCli)
                .forEach(System.out::println);
    }

    public void crearEstudios(EstudioModelCli estudiosModelCli) {
        try {
            Person person = personInputPort.findOne(estudiosModelCli.getPerson());
            Profesion profession = profesionInputPort.findOne(estudiosModelCli.getProfession());
            @SuppressWarnings("unused")
            Study study = studyInputPort
                    .create(estudiosMapperCli.fromAdapterToDomain(estudiosModelCli, person, profession));
            System.out.println("Carrera creada exitosamente");
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser creada");
        }
    }

    public void obtenerEstudios(StudyId studyid) {
        try {
            System.out.println(studyInputPort.findOne(studyid));
        } catch (Exception e) {
            System.out.println("La carrera de la persona con cédula " + studyid.getPersonId() + " y el id de profesión " + studyid.getProfessionId()
                    + "no existe en el sistema");
        }
    }

    public void editarEstudios(EstudioModelCli estudiosModelCli) {
        try {
            StudyId studyId = estudiosModelCli.getStudyId();
            Person person = personInputPort.findOne(estudiosModelCli.getPerson());
            Profesion profession = profesionInputPort.findOne(estudiosModelCli.getProfession());
            Study study = studyInputPort.edit(studyId,
                estudiosMapperCli.fromAdapterToDomain(estudiosModelCli, person, profession));
            System.out.println("Carrera editada exitosamente");
            System.out.println(study);
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser editada");
        }
    }

    public void eliminarEstudios(StudyId studyid) {
        try {
            studyInputPort.drop(studyid);
            System.out.println("Carrera de la persona con cédula " + studyid.getPersonId() + " y el id de profesión " + studyid.getProfessionId()
                    + " ha sido eliminada");
        } catch (Exception e) {
            System.out.println("La carrera no ha podido ser eliminada " + studyid.getPersonId() + studyid.getProfessionId());
        }
    }

}
