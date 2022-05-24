package com.example.repositories;

import java.util.List;
import java.util.Optional;

import com.example.models.Arrangement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.models.User;
@Repository
public interface ArrangementRepository extends JpaRepository<Arrangement, Long> {
    Optional<Arrangement> findByName(String username);
    Boolean existsByName(String username);
    List<Arrangement> findAll();
    Optional<Arrangement>deleteArrangementById(Long id);
    List<Arrangement> getAllByIdIn(List<Long> arrangements);
}
