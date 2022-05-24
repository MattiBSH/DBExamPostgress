package com.example.dto;

import java.util.List;

public class ArrangementDTO {
    long id;
    String name;
    String type;
    List<Long> teamIds;

    public ArrangementDTO(Long id,String name, String type, List<Long> teamIds) {
        this.name = name;
        this.type = type;
        this.teamIds = teamIds;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setUserIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
