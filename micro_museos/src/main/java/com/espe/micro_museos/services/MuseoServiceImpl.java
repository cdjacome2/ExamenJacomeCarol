package com.espe.micro_museos.services;

import com.espe.micro_museos.clients.CuradorClientRest;
import com.espe.micro_museos.models.Curador;
import com.espe.micro_museos.models.entities.Museo;
import com.espe.micro_museos.models.entities.MuseoCurador;
import com.espe.micro_museos.repositories.MuseoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MuseoServiceImpl implements MuseoService {

    @Autowired
    private MuseoRepository repository;

    @Autowired
    private CuradorClientRest clientRest;

    @Override
    public List<Museo> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Museo> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Museo save(Museo museo) {
        return repository.save(museo);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Curador> findCuradorById(Long id) {
        try {
            Curador curador = clientRest.findById(id);
            return Optional.ofNullable(curador);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Curador> addCurador(Curador curador, Long museoId) {
        Optional<Museo> optionalMuseo = repository.findById(museoId);
        if (optionalMuseo.isPresent()) {
            Museo museo = optionalMuseo.get();
            MuseoCurador museoCurador = new MuseoCurador();
            museoCurador.setCuradorId(curador.getId()); // Asigna solo el ID del curador
            museoCurador.setMuseo(museo);

            museo.getMuseoCuradores().add(museoCurador);
            repository.save(museo);
            return Optional.of(curador);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Curador> addCuradorToMuseo(Long museoId, Long curadorId) {
        Optional<Museo> optionalMuseo = repository.findById(museoId);
        if (optionalMuseo.isPresent()) {
            Museo museo = optionalMuseo.get();

            // Verificar si el curador ya está asignado al museo
            boolean alreadyExists = museo.getMuseoCuradores().stream()
                    .anyMatch(museoCurador -> museoCurador.getCuradorId().equals(curadorId));

            if (alreadyExists) {
                throw new IllegalStateException("El curador con ID " + curadorId + " ya está asignado a este museo.");
            }

            // Buscar el curador en el microservicio de curadores
            Optional<Curador> curadorOptional = findCuradorById(curadorId);
            if (curadorOptional.isEmpty()) {
                return Optional.empty();
            }

            Curador curador = curadorOptional.get();
            MuseoCurador museoCurador = new MuseoCurador();
            museoCurador.setCuradorId(curador.getId()); // Solo asignamos el ID del curador
            museoCurador.setMuseo(museo);

            museo.getMuseoCuradores().add(museoCurador);
            repository.save(museo);

            return Optional.of(curador);
        }
        return Optional.empty();
    }

    @Override
    public void removeCuradorFromMuseo(Long museoId, Long curadorId) {
        Optional<Museo> optionalMuseo = repository.findById(museoId);
        if (optionalMuseo.isPresent()) {
            Museo museo = optionalMuseo.get();
            boolean removed = museo.getMuseoCuradores().removeIf(museoCurador ->
                    curadorId != null && curadorId.equals(museoCurador.getCuradorId()));

            if (removed) {
                repository.save(museo); // Guardar el museo después de eliminar el curador
            }
        }
    }

    @Override
    public Curador addCuradorToSystem(Curador curador) {
        return clientRest.createCurador(curador); // Usamos createCurador, que está disponible en el cliente Feign
    }

    @Override
    @Transactional
    public List<Museo> findMuseosByCuradorId(Long curadorId) {
        return repository.findMuseosByCuradorId(curadorId);
    }
}
