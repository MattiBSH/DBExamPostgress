package com.example.security.services;

import com.example.dto.ArrangementDTO;
import com.example.dto.TeamDTO;
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
import java.util.Set;

@Service
public class TeamDetailsServiceImpl implements TeamDetailsService{

    @Autowired
    private ArrangementRepository arrangementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public String createTeam(TeamDTO teamDTO) {
        Team team = new Team();
        team.setName(teamDTO.getName());
        List<User> users=  userRepository.getAllByIdIn((ArrayList<Long>) teamDTO.getUsers());
        System.out.println(users);
        team.setMembers((ArrayList<User>) users);
        NeoFacade neoFacade = new NeoFacade();
        neoFacade.addTeam(team);
        return teamRepository.save(team).getName();

    }



}