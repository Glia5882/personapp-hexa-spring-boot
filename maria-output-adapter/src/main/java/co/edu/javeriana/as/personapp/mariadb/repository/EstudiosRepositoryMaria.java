package co.edu.javeriana.as.personapp.mariadb.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
public interface EstudiosRepositoryMaria extends JpaRepository<EstudiosEntity, EstudiosEntityPK> {
    
}
