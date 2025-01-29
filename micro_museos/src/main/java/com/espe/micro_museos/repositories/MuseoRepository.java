package com.espe.micro_museos.repositories;

import com.espe.micro_museos.models.entities.Museo;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MuseoRepository extends CrudRepository<Museo, Long> {
    @Query("SELECT m FROM Museo m JOIN m.museoCuradores mc WHERE mc.curadorId = :curadorId")
    List<Museo> findMuseosByCuradorId(@Param("curadorId") Long curadorId);

}
