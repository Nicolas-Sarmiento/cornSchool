package co.edu.uptc.cornschool.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Event {
    private String id;
    private String name;
    private String description;
    private Date date;
    private HashMap<Integer, Participant> leaderboard;

    public Event(String id, String name, String description, Date date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.leaderboard = new HashMap<>();
    }

    public Event(String id, String name, String description, Date date, HashMap<Integer, Participant> leaderboard) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
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

    public HashMap<Integer, Participant> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(HashMap<Integer, Participant> leaderboard) {
        this.leaderboard = leaderboard;
    }
}
