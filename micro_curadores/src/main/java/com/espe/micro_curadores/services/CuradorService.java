package com.espe.micro_curadores.services;

import com.espe.micro_curadores.models.entities.Curador;

import java.util.List;
import java.util.Optional;

public interface CuradorService {
    List<Curador> findAll();
    Optional<Curador> findById(Long id);
    Curador save(Curador curador);
    void deleteById(Long id);
}
