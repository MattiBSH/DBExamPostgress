package com.example.security.services;

import com.example.dto.ArrangementDTO;
import com.example.models.Arrangement;
import com.example.models.User;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArrangementServiceImpl implements ArrangementService {

    @Autowired
    private ArrangementRepository arrangementRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public String createArrangement(ArrangementDTO arrangementDTO) {
        Arrangement arrangement = new Arrangement();
        arrangement.setName(arrangementDTO.getName());
        arrangement.setType(arrangementDTO.getType());
        arrangement.setDate(LocalDateTime.now());
        List<User> users=userRepository.getAllByIdIn((ArrayList<Long>) arrangementDTO.getUserIds());
        arrangement.setUsersParticipated(users);
        return arrangementRepository.save(arrangement).getName();
    }


}
