package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfesionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfesionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

    @Autowired
    @Qualifier("profesionOutputAdapterMaria")
    private ProfesionOutputPort profesionOutputPortMaria;

    @Autowired
    @Qualifier("profesionOutputAdapterMongo")
    private ProfesionOutputPort profesionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli profesionMapperCli;

    ProfesionInputPort profesionInputPort;

    public void setProfesionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            profesionInputPort = new ProfesionUseCase(profesionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial() {
        log.info("Into historial ProfesionEntity in Input Adapter");
        List<ProfesionModelCli> profesiones = profesionInputPort.findAll().stream()
                .map(profesionMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        profesiones.forEach(p -> System.out.println(p.toString()));
    }

    public boolean create(Profesion profesion) {
        log.info("Creating a new profession");
        Profesion newProfesion = profesionInputPort.create(profesion);

        if (newProfesion != null)
            return true;
        else
            return false;
    }

    public boolean edit(Integer id, Profesion profesion) throws NoExistException {
        log.info("Editing a profession");

        Profesion editedProfesion = profesionInputPort.edit(id, profesion);

        if (editedProfesion != null)
            return true;
        else
            return false;
    }

    public void drop(Integer id) throws NoExistException {
        try {
            System.out.println(id);
            profesionInputPort.drop(id);
            System.out.println("Profesión con la identificación " + id + " ha sido eliminada");
        } catch (Exception e) {;
            System.out.println("La profesión no ha podido ser eliminada");
        }
    }
}
