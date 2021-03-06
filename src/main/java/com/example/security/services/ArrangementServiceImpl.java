package com.example.security.services;

import com.example.dto.ArrangementDTO;
import com.example.facades.NeoFacade;
import com.example.models.Arrangement;
import com.example.models.Team;
import com.example.models.User;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.TeamRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArrangementServiceImpl implements ArrangementService {


    @Override
    public String createArrangement(ArrangementDTO arrangementDTO,TeamRepository teamRepository,ArrangementRepository arrangementRepository) {
        Arrangement arrangement = new Arrangement();
        arrangement.setName(arrangementDTO.getName());
        arrangement.setType(arrangementDTO.getType());
        arrangement.setDate(LocalDateTime.now().toString());
        List<Team> teams=teamRepository.getAllByIdIn(arrangementDTO.getTeamIds());
        arrangement.setTeamsParticipated(teams);
        arrangement.setWinner(teamRepository.getById(arrangementDTO.getWinner()));
        arrangement.setSecond(teamRepository.getById(arrangementDTO.getSecond()));
        arrangement.setThird(teamRepository.getById(arrangementDTO.getThird()));
        NeoFacade neoFacade = new NeoFacade();
        neoFacade.addEvent(arrangement);
        return arrangementRepository.save(arrangement).getName();
    }



}
