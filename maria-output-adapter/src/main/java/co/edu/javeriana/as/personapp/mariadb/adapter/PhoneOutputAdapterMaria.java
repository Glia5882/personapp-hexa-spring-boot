package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Saving phone record to MariaDB");
        TelefonoEntity persistedTelefono = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToEntity(phone, true));
        return telefonoMapperMaria.fromEntityToDomain(persistedTelefono, true); 
    }

    @Override
    public Boolean delete(String phoneNumber) {
        log.debug("Deleting phone record from MariaDB");
        telefonoRepositoryMaria.deleteById(phoneNumber);
        return telefonoRepositoryMaria.findById(phoneNumber).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Fetching all phone records from MariaDB");
        return telefonoRepositoryMaria.findAll().stream()
                .map(entity -> telefonoMapperMaria.fromEntityToDomain(entity, true)) 
                .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String phoneNumber) {
        log.debug("Fetching phone record by number from MariaDB");
        return telefonoRepositoryMaria.findById(phoneNumber)
                .map(entity -> telefonoMapperMaria.fromEntityToDomain(entity, true)) 
                .orElse(null);
    }
}
