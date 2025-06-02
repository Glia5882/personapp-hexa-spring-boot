package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudios")
public class EstudioControllerV1 {

    @Autowired
    private EstudioInputAdapterRest estudioInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EstudioResponse> estudios(@PathVariable String database) {
        log.info("Into estudios REST API");
        return estudioInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse crearEstudios(@RequestBody EstudioRequest request) {
        return estudioInputAdapterRest.crearEstudios(request);
    }

    @ResponseBody
    @GetMapping(path = "/{database}/{ccPer}/{idProf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse obtenerEstudios(@PathVariable String database, @PathVariable String idProf,
            @PathVariable String ccPer) {
        StudyId studyid = new StudyId(Integer.parseInt(ccPer), Integer.parseInt(idProf));
        return estudioInputAdapterRest.obtenerEstudios(database.toUpperCase(), studyid);
    }

    @ResponseBody
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EstudioResponse editarEstudios(@RequestBody EstudioRequest request) {
        return estudioInputAdapterRest.editarEstudios(request);
    }

    @ResponseBody
    @DeleteMapping(path = "/{database}/{ccPer}/{idProf}")
    public Boolean eliminarEstudios(@PathVariable String database, @PathVariable String idProf,
            @PathVariable String ccPer) {
        StudyId studyid = new StudyId(Integer.parseInt(ccPer), Integer.parseInt(idProf));
        return estudioInputAdapterRest.eliminarEstudios(database, studyid);
    }

}
