package com.espe.micro_museos.controller;

import com.espe.micro_museos.models.Curador;
import com.espe.micro_museos.models.entities.Museo;
import com.espe.micro_museos.services.MuseoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@RestController
@RequestMapping("/api/museos")
@Tag(name = "Museos API", description = "Operaciones relacionadas con la gestión de museos y asignación de curadores")
public class MuseoController {

    @Autowired
    private MuseoService service;

    @PostMapping
    @Operation(
            summary = "Crear un nuevo museo",
            description = "Recibe los datos de un museo y lo guarda en la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Museo creado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Museo.class))),
                    @ApiResponse(responseCode = "400", description = "Errores de validación")
            }
    )
    public ResponseEntity<?> create(@Valid @RequestBody Museo museo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        Museo museoDb = service.save(museo);
        return ResponseEntity.status(HttpStatus.CREATED).body(museoDb);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los museos",
            description = "Devuelve una lista de todos los museos registrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
            }
    )
    public ResponseEntity<List<Museo>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener museo por ID",
            description = "Devuelve los datos de un museo basado en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Museo encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Museo.class))),
                    @ApiResponse(responseCode = "404", description = "Museo no encontrado")
            }
    )
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Museo> museoOptional = service.findById(id);
        if (museoOptional.isPresent()) {
            return ResponseEntity.ok(museoOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Museo no encontrado"));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un museo existente",
            description = "Recibe los datos de un museo y actualiza la información en la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Museo actualizado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Museo.class))),
                    @ApiResponse(responseCode = "400", description = "Errores de validación"),
                    @ApiResponse(responseCode = "404", description = "Museo no encontrado")
            }
    )
    public ResponseEntity<?> update(@Valid @RequestBody Museo museo, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<Museo> museoOptional = service.findById(id);
        if (museoOptional.isPresent()) {
            Museo museoDb = museoOptional.get();
            museoDb.setNombre(museo.getNombre());
            museoDb.setUbicacion(museo.getUbicacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(museoDb));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Museo no encontrado"));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un museo",
            description = "Elimina un museo basado en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Museo eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Museo no encontrado")
            }
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Museo> museo = service.findById(id);
        if (museo.isPresent()) {
            service.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Museo eliminado correctamente"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Museo no encontrado"));
    }

    @PostMapping("/{id}/curadores")
    @Operation(
            summary = "Asignar un curador a un museo",
            description = "Asigna un curador existente al museo especificado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Curador asignado correctamente al museo"),
                    @ApiResponse(responseCode = "404", description = "Curador o museo no encontrado"),
                    @ApiResponse(responseCode = "409", description = "El curador ya está asignado a este museo"),
                    @ApiResponse(responseCode = "500", description = "Error en la comunicación con el servicio de curadores")
            }
    )
    public ResponseEntity<?> assignCurador(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        Long curadorId = request.get("id");
        if (curadorId == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "El ID del curador es obligatorio"));
        }

        try {
            Optional<Curador> curadorAsignado = service.addCuradorToMuseo(id, curadorId);

            if (curadorAsignado.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Museo o curador no encontrado"));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Curador asignado correctamente"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", e.getMessage()));
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error en la comunicación con el servicio de curadores"));
        }
    }

    @DeleteMapping("/{id}/curadores/{curadorId}")
    @Operation(
            summary = "Desasignar un curador de un museo",
            description = "Elimina la asignación de un curador de un museo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curador desasignado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Museo o curador no encontrado")
            }
    )
    public ResponseEntity<?> unassignCurador(@PathVariable Long id, @PathVariable Long curadorId) {
        Optional<Museo> museoOptional = service.findById(id);
        if (museoOptional.isPresent()) {
            service.removeCuradorFromMuseo(id, curadorId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Curador desasignado correctamente"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Museo o curador no encontrado"));
    }
    @GetMapping("/curadores/{curadorId}/museos")
    @Operation(
            summary = "Listar museos donde trabaja un curador",
            description = "Devuelve una lista de museos donde trabaja el curador especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de museos obtenida correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curador no encontrado")
            }
    )
    public ResponseEntity<?> listarMuseosDeCurador(@PathVariable Long curadorId) {
        List<Museo> museos = service.findMuseosByCuradorId(curadorId);
        if (museos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "El curador no trabaja en ningún museo"));
        }
        return ResponseEntity.ok(museos);
    }

}
