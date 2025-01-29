package com.espe.micro_curadores.controller;

import com.espe.micro_curadores.models.entities.Curador;
import com.espe.micro_curadores.services.CuradorService;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/curadores")
@Tag(name = "Curadores API", description = "Operaciones relacionadas con la gestión de curadores")
public class CuradorController {

    @Autowired
    private CuradorService service;

    @GetMapping
    @Operation(
            summary = "Obtener todos los curadores",
            description = "Devuelve una lista de todos los curadores registrados en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener curador por ID",
            description = "Devuelve un curador específico basado en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curador encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Curador.class))),
                    @ApiResponse(responseCode = "404", description = "Curador no encontrado")
            }
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo curador",
            description = "Recibe los datos de un curador, valida la información y lo guarda en la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Curador creado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Curador.class))),
                    @ApiResponse(responseCode = "400", description = "Errores de validación")
            }
    )
    public ResponseEntity<?> create(@Valid @RequestBody Curador curador, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        Curador curadorCreado = service.save(curador);
        return ResponseEntity.status(HttpStatus.CREATED).body(curadorCreado);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un curador existente",
            description = "Recibe el ID de un curador y los datos actualizados, valida la información y realiza los cambios en la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curador actualizado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Curador.class))),
                    @ApiResponse(responseCode = "400", description = "Errores de validación"),
                    @ApiResponse(responseCode = "404", description = "Curador no encontrado")
            }
    )
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Curador curador, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<Curador> curadorOptional = service.findById(id);
        if (curadorOptional.isPresent()) {
            Curador curadorDb = curadorOptional.get();
            curadorDb.setNombre(curador.getNombre());
            curadorDb.setEspecialidad(curador.getEspecialidad());
            curadorDb.setFechaNacimiento(curador.getFechaNacimiento());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(curadorDb));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Curador no encontrado"));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un curador",
            description = "Recibe el ID de un curador y lo elimina de la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curador eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curador no encontrado")
            }
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Curador> curadorOptional = service.findById(id);
        if (curadorOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Curador eliminado correctamente"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Curador no encontrado"));
    }
}
