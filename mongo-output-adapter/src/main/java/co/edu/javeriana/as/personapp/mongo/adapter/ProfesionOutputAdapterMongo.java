package co.edu.javeriana.as.personapp.mongo.adapter;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("profesionOutputAdapterMongo")
public class ProfesionOutputAdapterMongo implements ProfesionOutputPort {
    
    @Autowired
    private ProfesionRepositoryMongo profesionRepositoryMongo;
    
    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;
    
    @Override
    public Profesion save(Profesion profesion) {
        log.debug("Into save on Adapter MongoDB");
        try {
            ProfesionDocument persistedProfesion = profesionRepositoryMongo.save(profesionMapperMongo.fromDomainToAdapter(profesion));
            return profesionMapperMongo.fromAdapterToDomain(persistedProfesion);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return profesion;
        }       
    }

    @Override
    public Boolean delete(Integer id) {
        log.debug("Into delete on Adapter MongoDB");
        profesionRepositoryMongo.deleteById(id);
        return !profesionRepositoryMongo.findById(id).isPresent();
    }

    @Override
    public List<Profesion> find() {
        log.debug("Into find on Adapter MongoDB");
        return profesionRepositoryMongo.findAll().stream().map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profesion findById(Integer id) {
        log.debug("Into findById on Adapter MongoDB");
        return profesionRepositoryMongo.findById(id)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }

}
