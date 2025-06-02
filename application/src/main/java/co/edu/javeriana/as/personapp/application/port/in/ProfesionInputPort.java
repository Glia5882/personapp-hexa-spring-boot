package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profesion;

@Port
public interface ProfesionInputPort {
    
    public void setPersistence(ProfesionOutputPort profesionPersistence);
    
    public Profesion create(Profesion profesion);

    public Profesion edit(Integer id, Profesion profesion) throws NoExistException;

    public Boolean drop(Integer id) throws NoExistException;

    public List<Profesion> findAll();

    public Profesion findOne(Integer id) throws NoExistException;

    public Integer count();
}