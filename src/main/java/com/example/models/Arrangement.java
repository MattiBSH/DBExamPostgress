package com.example.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(	name = "arrangements",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "type")
        })
public class Arrangement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    @ManyToMany(fetch = FetchType.LAZY)
    List<Team>teamsParticipated;
    LocalDateTime date;

    public Arrangement() {
    }

    public Arrangement(String username, String type,List<Team>teamsParticipated,LocalDateTime date) {
        this.name = username;
        this.type = type;
        this.teamsParticipated=teamsParticipated;
        this.date=date;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public List<Team> getUsersParticipated() {
        return teamsParticipated;
    }

    public void setTeamsParticipated(List<Team> teamsParticipated) {
        this.teamsParticipated = teamsParticipated;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
