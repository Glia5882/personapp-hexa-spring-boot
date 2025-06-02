package co.edu.javeriana.as.personapp.mariadb.mapper;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

@Mapper
public class TelefonoMapperMaria {

    public TelefonoEntity fromDomainToEntity(Phone phone, boolean loadRelations) {
        TelefonoEntity telefonoEntity = new TelefonoEntity();
        telefonoEntity.setNum(phone.getNumber());
        telefonoEntity.setOper(phone.getCompany());
        if (loadRelations && phone.getOwner() != null) {
            PersonaEntity ownerEntity = new PersonaEntity();
            ownerEntity.setCc(phone.getOwner().getIdentification());
            telefonoEntity.setDuenio(ownerEntity);
        }
        return telefonoEntity;
    }

    public Phone fromEntityToDomain(TelefonoEntity telefonoEntity, boolean loadRelations) {
        Phone phone = new Phone();
        phone.setNumber(telefonoEntity.getNum());
        phone.setCompany(telefonoEntity.getOper());
        if (loadRelations && telefonoEntity.getDuenio() != null) {
            Person owner = new Person();
            owner.setIdentification(telefonoEntity.getDuenio().getCc());
            phone.setOwner(owner);
        }
        return phone;
    }


}
