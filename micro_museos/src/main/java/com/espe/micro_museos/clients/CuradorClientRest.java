package com.espe.micro_museos.clients;

import com.espe.micro_museos.models.Curador;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "micro-curadores", url = "http://localhost:8004/api/curadores")
public interface CuradorClientRest {

    /**
     * Busca un curador por su ID.
     * @param id Identificador del curador.
     * @return Curador encontrado.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    Curador findById(@PathVariable Long id);

    /**
     * Crea un nuevo curador en el sistema.
     * @param curador Información del curador a crear.
     * @return Curador creado.
     */
    @PostMapping(produces = "application/json", consumes = "application/json")
    Curador createCurador(@RequestBody Curador curador);
}
