package co.edu.javeriana.as.personapp.mongo.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class ProfesionMapperMongo {

	@Autowired
	private EstudiosMapperMongo estudiosMapperMongo;

	public ProfesionDocument fromDomainToAdapter(@NonNull Profesion profesion) {
		ProfesionDocument profesionDocument = new ProfesionDocument();
		profesionDocument.setId(profesion.getId());
		profesionDocument.setNom(profesion.getNom());
		profesionDocument.setDes(validateDes(profesion.getDes()));
		profesionDocument.setEstudios(validateEstudios(profesion.getStudies()));
		return profesionDocument;
	}

	private String validateDes(String description) {
		return description != null ? description : "";
	}

	private List<EstudiosDocument> validateEstudios(List<Study> studies) {
		return studies != null && !studies.isEmpty() ? studies.stream()
				.map(study -> estudiosMapperMongo.fromDomainToAdapter(study)).collect(Collectors.toList())
				: new ArrayList<EstudiosDocument>();
	}

	public Profesion fromAdapterToDomain(ProfesionDocument profesionDocument) {
		Profesion profession = new Profesion();
		profession.setId(profesionDocument.getId());
		profession.setNom(profesionDocument.getNom());
		profession.setDes(validateDescription(profesionDocument.getDes()));
		return profession;
	}

	private String validateDescription(String des) {
		return des != null ? des : "";
	}


}
