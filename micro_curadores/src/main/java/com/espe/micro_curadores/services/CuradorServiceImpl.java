package com.espe.micro_curadores.services;

import com.espe.micro_curadores.models.entities.Curador;
import com.espe.micro_curadores.repositories.CuradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuradorServiceImpl implements CuradorService {
    @Autowired
    private CuradorRepository repository;

    @Override
    public List<Curador> findAll() {
        return (List<Curador>) repository.findAll();
    }

    @Override
    public Optional<Curador> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Curador save(Curador curador) {
        return repository.save(curador);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
