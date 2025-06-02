package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

    public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
        telefonoModelCli.setNumber(phone.getNumber());
        telefonoModelCli.setPersonId(phone.getOwner().getIdentification());
        telefonoModelCli.setCompany(phone.getCompany());
		return telefonoModelCli;
	}

    public Phone fromAdapterToDomain(TelefonoModelCli telefonoModelCli, Person owner) {
        Phone phone = new Phone();
        phone.setNumber(telefonoModelCli.getNumber());
        phone.setOwner(owner);
        phone.setCompany(telefonoModelCli.getCompany());
        return phone;
    }
    
}
