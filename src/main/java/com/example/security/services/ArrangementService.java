package com.example.security.services;

import com.example.dto.ArrangementDTO;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.TeamRepository;

import java.util.UUID;

public interface ArrangementService {

    public String createArrangement(ArrangementDTO arrangementDTO, TeamRepository teamRepository, ArrangementRepository arrangementRepository);

}
