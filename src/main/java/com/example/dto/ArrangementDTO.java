package com.example.dto;

import java.util.List;

public class ArrangementDTO {
    String name;
    String type;
    List<Long> userIds;

    public ArrangementDTO(String name, String type, List<Long> userIds) {
        this.name = name;
        this.type = type;
        this.userIds = userIds;
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
}
