package com.example.dto;

import java.util.List;

public class ArrangementDTO {
    long id;
    String name;
    String type;
    List<Long> teamIds;
    Long winner;
    Long second;
    Long third;
    public ArrangementDTO(Long id,String name, String type, List<Long> teamIds, Long winner, Long second, Long third) {
        this.name = name;
        this.type = type;
        this.teamIds = teamIds;
        this.id=id;
        this.winner=winner;
        this.second=second;
        this.third=third;
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

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }

    public Long getSecond() {
        return second;
    }

    public void setSecond(Long second) {
        this.second = second;
    }

    public Long getThird() {
        return third;
    }

    public void setThird(Long third) {
        this.third = third;
    }
}
