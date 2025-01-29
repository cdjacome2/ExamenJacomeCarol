package com.espe.micro_museos.services;

import com.espe.micro_museos.models.Curador;
import com.espe.micro_museos.models.entities.Museo;

import java.util.List;
import java.util.Optional;

public interface MuseoService {

    // Métodos para Museos
    List<Museo> findAll();
    Optional<Museo> findById(Long id);
    Museo save(Museo museo);
    void delete(Long id);

    // Métodos para Curadores
    Optional<Curador> findCuradorById(Long id);
    Optional<Curador> addCurador(Curador curador, Long museoId);
    Optional<Curador> addCuradorToMuseo(Long museoId, Long curadorId);
    void removeCuradorFromMuseo(Long museoId, Long curadorId);
    Curador addCuradorToSystem(Curador curador);
    List<Museo> findMuseosByCuradorId(Long curadorId);

}
