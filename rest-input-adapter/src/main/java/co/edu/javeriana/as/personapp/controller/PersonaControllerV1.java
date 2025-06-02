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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

    @Autowired
    private PersonaInputAdapterRest personaInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonaResponse> personas(@PathVariable String database) {
        log.info("Fetching all persons from database: {}", database);
        return personaInputAdapterRest.historial(database.toUpperCase());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse crearPersona(@RequestBody PersonaRequest request) {
    log.info("Creating person with data: {}", request);
    return personaInputAdapterRest.crearPersona(request);
}

    @ResponseBody
    @PutMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonaResponse editarPersona(@RequestBody PersonaRequest request, @PathVariable String database) {
        log.info("Editing person with ID: {} in database: {}", request.getDni(), database);
        request.setDatabase(database.toUpperCase());
        return personaInputAdapterRest.editarPersona(request);
    }

    @ResponseBody
    @DeleteMapping(path = "/{dni}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaResponse eliminarPersona(@PathVariable String dni, @PathVariable String database) {
        log.info("Deleting person with ID: {} from database: {}", dni, database);
        return personaInputAdapterRest.eliminarPersona(dni, database.toUpperCase());
    }
}
