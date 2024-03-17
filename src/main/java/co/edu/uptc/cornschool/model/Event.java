package co.edu.uptc.cornschool.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Event {
    private String id;
    private String name;
    private String description;
    private Date date;
    private Discipline discipline;
    private HashMap<Participant, Integer> leaderboard;

    public Event(String id, String name, String description, Date date, Discipline discipline, HashMap<Participant, Integer> leaderboard) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.discipline = discipline;
        this.leaderboard = leaderboard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public HashMap<Participant, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(HashMap<Participant, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", discipline=" + discipline +
                ", leaderboard=" + leaderboard +
                '}';
    }
}
