package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.PersonaMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("personOutputAdapterMaria")
@Transactional
public class PersonOutputAdapterMaria implements PersonOutputPort {

    @Autowired
    private PersonaRepositoryMaria personaRepositoryMaria;

    @Autowired
    private PersonaMapperMaria personaMapperMaria;

    @Override
    public Person save(Person person) {
        PersonaEntity personaEntity = personaMapperMaria.fromDomainToAdapter(person, false);
        if (personaEntity.getCc() == null) {
            log.error("ID is null before saving, which should not happen.");
            throw new IllegalStateException("ID must be set before saving.");
        }
        log.info("Saving person with ID: {}", personaEntity.getCc());
        PersonaEntity persistedPersona = personaRepositoryMaria.save(personaEntity);
        return personaMapperMaria.fromAdapterToDomain(persistedPersona, false);
    }
    

    @Override
    public Boolean delete(Integer identification) {
        log.debug("Into delete on Adapter MariaDB");
        personaRepositoryMaria.deleteById(identification);
        return personaRepositoryMaria.findById(identification).isEmpty();
    }

    @Override
    public List<Person> find() {
        log.debug("Into find on Adapter MariaDB");
        return personaRepositoryMaria.findAll().stream()
                .map(entity -> personaMapperMaria.fromAdapterToDomain(entity, false))
                .collect(Collectors.toList());
    }

    @Override
    public Person findById(Integer identification) {
        log.debug("Into findById on Adapter MariaDB");
        return personaRepositoryMaria.findById(identification)
                .map(entity -> personaMapperMaria.fromAdapterToDomain(entity, false))
                .orElse(null);
    }
}
