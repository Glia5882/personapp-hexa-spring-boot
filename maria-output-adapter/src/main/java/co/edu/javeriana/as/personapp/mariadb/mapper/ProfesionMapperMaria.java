package co.edu.javeriana.as.personapp.mariadb.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import lombok.NonNull;

@Mapper
public class ProfesionMapperMaria {

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    public ProfesionEntity fromDomainToAdapter(@NonNull Profesion profesion, boolean loadRelations) {
        ProfesionEntity profesionEntity = new ProfesionEntity();
        profesionEntity.setId(profesion.getId());
        profesionEntity.setNom(profesion.getNom());
        profesionEntity.setDes(profesion.getDes());

        if (loadRelations) {
            profesionEntity.setEstudios(estudiosMapperMaria.validateEstudios(profesion.getStudies(), true));
        }

        return profesionEntity;
    }

    public Profesion fromAdapterToDomain(ProfesionEntity profesionEntity, boolean loadRelations) {
        Profesion profession = new Profesion();
        profession.setId(profesionEntity.getId());
        profession.setNom(profesionEntity.getNom());
        profession.setDes(profesionEntity.getDes());

        if (loadRelations) {
            profession.setStudies(estudiosMapperMaria.validateStudies(profesionEntity.getEstudios(), true));
        }

        return profession;
    }

 
}
