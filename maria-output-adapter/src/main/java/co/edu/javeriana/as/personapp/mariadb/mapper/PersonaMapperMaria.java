package co.edu.javeriana.as.personapp.mariadb.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import lombok.NonNull;
@Mapper
public class PersonaMapperMaria {

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    public PersonaEntity fromDomainToAdapter(@NonNull Person person, boolean loadRelations) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setCc(person.getIdentification());
        personaEntity.setNombre(person.getFirstName());
        personaEntity.setApellido(person.getLastName());
        personaEntity.setGenero(validateGenero(person.getGender()));
        personaEntity.setEdad(validateEdad(person.getAge()));

        if (loadRelations) {
            personaEntity.setEstudios(estudiosMapperMaria.validateEstudios(person.getStudies(), true));
        }

        return personaEntity;
    }

    public Person fromAdapterToDomain(PersonaEntity personaEntity, boolean loadRelations) {
        Person person = new Person();
        person.setIdentification(personaEntity.getCc());
        System.out.println(person.getIdentification());
        person.setFirstName(personaEntity.getNombre());
        person.setLastName(personaEntity.getApellido());
        person.setGender(validateGender(personaEntity.getGenero()));
        person.setAge(personaEntity.getEdad());

        if (loadRelations) {
            person.setStudies(estudiosMapperMaria.validateStudies(personaEntity.getEstudios(), true));
        }

        return person;
    }

    private Character validateGenero(Gender gender) {
        return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : 'O';
    }

    private Integer validateEdad(Integer age) {
        return age;
    }

    private Gender validateGender(Character genero) {
        switch (genero) {
            case 'F': return Gender.FEMALE;
            case 'M': return Gender.MALE;
            default: return Gender.OTHER;
        }
    }
}