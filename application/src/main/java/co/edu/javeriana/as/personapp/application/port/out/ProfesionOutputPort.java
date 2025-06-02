package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Profesion;

@Port
public interface ProfesionOutputPort {
    public Profesion save(Profesion profesion);
    public Boolean delete(Integer id);
    public List<Profesion> find();
    public Profesion findById(Integer id);
}
