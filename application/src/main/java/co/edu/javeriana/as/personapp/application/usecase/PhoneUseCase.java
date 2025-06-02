package co.edu.javeriana.as.personapp.application.usecase;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@UseCase
public class PhoneUseCase implements PhoneInputPort {


    private PhoneOutputPort phonePersistence;

    


    public PhoneUseCase(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersistence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Creating phone record");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(String phoneNumber, Phone phone) throws NoExistException {
        Phone existingPhone = phonePersistence.findByNumber(phoneNumber);
        if (existingPhone != null) {
            return phonePersistence.save(phone);
        }
        throw new NoExistException("The phone with number " + phoneNumber + " does not exist in the database, cannot be edited");
    }

    @Override
    public Boolean drop(String phoneNumber) throws NoExistException {
        Phone existingPhone = phonePersistence.findByNumber(phoneNumber);
        if (existingPhone != null) {
            return phonePersistence.delete(phoneNumber);
        }
        throw new NoExistException("The phone with number " + phoneNumber + " does not exist in the database, cannot be dropped");
    }

    @Override
    public List<Phone> findAll() {
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(String phoneNumber) throws NoExistException {
        Phone phone = phonePersistence.findByNumber(phoneNumber);
        if (phone != null) {
            return phone;
        }
        throw new NoExistException("The phone with number " + phoneNumber + " does not exist in the database, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    
}
