package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Mapper
public class ProfesionMapperRest {
    public ProfesionResponse fromDomainToAdapterRestMaria(Profesion profesion) {
        return fromDomainToAdapterRest(profesion, "MariaDB");
    }

    public ProfesionResponse fromDomainToAdapterRestMongo(Profesion profesion) {
        return fromDomainToAdapterRest(profesion, "MongoDB");
    }

    public ProfesionResponse fromDomainToAdapterRest(Profesion profesion, String database) {
        return new ProfesionResponse(
                profesion.getId() + "",
                profesion.getNom(),
                profesion.getDes(),
                database,
                "OK");
    }

    public Profesion fromAdapterToDomain(ProfesionRequest request) {
        Profesion profession = new Profesion(Integer.parseInt(request.getId()), request.getName());
        if (request.getDescription() != null) {
            profession.setDes(request.getDescription());
        }
        return profession;
    }
}
