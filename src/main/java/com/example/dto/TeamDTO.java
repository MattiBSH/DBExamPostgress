package com.example.dto;

import com.example.models.Arrangement;
import com.example.models.User;
import java.util.List;
import java.util.Set;

public class TeamDTO {
    long id;
    String name;
    List<Long> users;
    List<Long> arrangements;

    public TeamDTO(Long id,String name,List<Long>users) {
        this.name = name;
        this.id=id;
        this.users=users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public List<Long> getArrangements() {
        return arrangements;
    }

    public void setArrangements(List<Long> arrangements) {
        this.arrangements = arrangements;
    }


}

