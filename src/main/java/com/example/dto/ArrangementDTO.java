package com.example.dto;

import java.util.List;

public class ArrangementDTO {
    long id;
    String name;
    String type;
    List<Long> userIds;

    public ArrangementDTO(Long id,String name, String type, List<Long> userIds) {
        this.name = name;
        this.type = type;
        this.userIds = userIds;
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

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
