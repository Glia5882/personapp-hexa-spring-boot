package co.edu.javeriana.as.personapp.mariadb.adapter;


import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfesionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;

@Adapter("profesionOutputAdapterMaria")
@Transactional
public class ProfesionOutputAdapterMaria implements ProfesionOutputPort {

    @Autowired
    private ProfesionRepositoryMaria profesionRepositoryMaria;
    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Override
    public Profesion save(Profesion profesion) {
        ProfesionEntity persistedProfesion = profesionRepositoryMaria.save(profesionMapperMaria.fromDomainToAdapter(profesion, false)); 
        return profesionMapperMaria.fromAdapterToDomain(persistedProfesion, false);  
    }

    @Override
    public Boolean delete(Integer id) {
        profesionRepositoryMaria.deleteById(id);
        return profesionRepositoryMaria.findById(id).isEmpty();
    }

    @Override
    public List<Profesion> find() {
        return profesionRepositoryMaria.findAll().stream()
                .map(profesionEntity -> profesionMapperMaria.fromAdapterToDomain(profesionEntity, false)) 
                .collect(Collectors.toList());
    }

    @Override
    public Profesion findById(Integer id) {
        return profesionRepositoryMaria.findById(id)
                .map(profesionEntity -> profesionMapperMaria.fromAdapterToDomain(profesionEntity, false))  
                .orElse(null);
    }
}
