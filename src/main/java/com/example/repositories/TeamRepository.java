package com.example.repositories;

import java.util.List;
import java.util.Optional;

import com.example.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.models.User;
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team>deleteTeamById(Long id);
    List<Team>getAllByIdIn(List<Long> teamIds);
    Team getById(Long id);
}