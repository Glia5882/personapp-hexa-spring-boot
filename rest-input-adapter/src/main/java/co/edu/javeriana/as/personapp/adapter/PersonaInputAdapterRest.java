package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			String database = setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
			if(database.equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personaMapperRest.fromDomainToAdapterRestMaria(person);
			}else {
				return personaMapperRest.fromDomainToAdapterRestMongo(person);
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new PersonaResponse("", "", "", "", "", "", "");
		}
	}
	

	public PersonaResponse editarPersona(PersonaRequest request) {
    try {
        setPersonOutputPortInjection(request.getDatabase());
        Person personToUpdate = personaMapperRest.fromAdapterToDomain(request);
        Person updatedPerson = personInputPort.edit(Integer.parseInt(request.getDni()), personToUpdate);
        if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            return personaMapperRest.fromDomainToAdapterRestMaria(updatedPerson);
        } else {
            return personaMapperRest.fromDomainToAdapterRestMongo(updatedPerson);
        }
    } catch (InvalidOptionException | NoExistException | NumberFormatException e) {
        log.warn("Error editing person: " + e.getMessage());
        return new PersonaResponse(request.getDni(), request.getFirstName(), request.getLastName(), request.getAge(), request.getSex(), request.getDatabase(), "Failed: " + e.getMessage());
    }
}


public PersonaResponse eliminarPersona(String dni, String database) {
    try {
        setPersonOutputPortInjection(database);
        boolean isDeleted = personInputPort.drop(Integer.parseInt(dni));
        String status = isDeleted ? "Deleted" : "Error deleting";
        return new PersonaResponse(dni, null, null, null, null, database, status);
    } catch (InvalidOptionException | NoExistException | NumberFormatException e) {
        log.warn("Error deleting person: " + e.getMessage());
        return new PersonaResponse(dni, null, null, null, null, database, "Failed: " + e.getMessage());
    }
}

	
	

}
