package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfesionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profesion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class ProfesionUseCase implements ProfesionInputPort {

    private ProfesionOutputPort profesionPersistence;

    public ProfesionUseCase(@Qualifier("profesionOutputAdapterMaria") ProfesionOutputPort profesionPersistence) {
        this.profesionPersistence = profesionPersistence;
    }

    @Override
    public void setPersistence(ProfesionOutputPort profesionPersistence) {
        this.profesionPersistence = profesionPersistence;
    }

    @Override
    public Profesion create(Profesion profesion) {
        log.debug("Into create on Application Domain");
        return profesionPersistence.save(profesion);
    }

    @Override
    public Profesion edit(Integer id, Profesion profesion) throws NoExistException {
        Profesion oldProfesion = profesionPersistence.findById(id);
        if (oldProfesion != null)
            return profesionPersistence.save(profesion);
        throw new NoExistException("The profesion with id " + id + " does not exist in db, cannot be edited");
    }

    @Override
    public Boolean drop(Integer id) throws NoExistException {
        Profesion oldProfesion = profesionPersistence.findById(id);
        if(oldProfesion != null){
            return profesionPersistence.delete(id);
        }
        throw new NoExistException("The profession with id " + id + " does not exist into db, cannot be dropped");

    }

    @Override
    public List<Profesion> findAll() {
        return profesionPersistence.find();
    }

    @Override
    public Profesion findOne(Integer id) throws NoExistException {
        Profesion oldProfesion = profesionPersistence.findById(id);
        if (oldProfesion != null)
            return oldProfesion;
        throw new NoExistException("The profesion with id " + id + " does not exist in db, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

  

   
}
