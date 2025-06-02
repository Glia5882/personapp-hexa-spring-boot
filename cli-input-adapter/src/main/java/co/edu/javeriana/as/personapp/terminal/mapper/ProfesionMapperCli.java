package co.edu.javeriana.as.personapp.terminal.mapper;

import java.util.Collections;
import java.util.Objects;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;

@Mapper
public class ProfesionMapperCli {

    public ProfesionModelCli fromDomainToAdapterCli(Profesion profesion) {
        ProfesionModelCli profesionModelCli = new ProfesionModelCli();
        profesionModelCli.setId(profesion.getId());
        profesionModelCli.setNom(profesion.getNom());
        profesionModelCli.setDes(profesion.getDes());
        return profesionModelCli;
    }

     public Profesion fromAdapterToDomain(ProfesionModelCli profesionModelCli) {
        Objects.requireNonNull(profesionModelCli, "ProfesionModelCli cannot be null");

        return new Profesion(
            profesionModelCli.getId(),
            profesionModelCli.getNom(),
            profesionModelCli.getDes(),
            Collections.emptyList()
        );
    }
}
