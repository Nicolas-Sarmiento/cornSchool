package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Event;
import co.edu.uptc.cornschool.model.Participant;
import co.edu.uptc.cornschool.persistence.EventDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class EventController {
    private EventDAO dao;
    private String returnMessage;
    private Date auxDate;

    public EventController() {
        this.dao = new EventDAO();
        this.returnMessage = "";
        auxDate = new Date();
    }

    public String save(String id, String name, String description, String date, String idDiscipline, List<String> participantsId){
        if ( !validateEvent(id, name,description, date,idDiscipline, participantsId)){
            return "Event can't be saved. " + this.returnMessage;
        }
        if (this.dao.findById(id) != null ) return "Event can't be saved. . Event exists!";

        Event eventToAdd = this.convertEvent(id, name, description,date,idDiscipline, participantsId);
        boolean result = this.dao.save(eventToAdd);
        return result? "Event saved successfully" : "Something went wrong x(. Try again later!";
    }

    public String update(String id, String name, String description, String date, String idDiscipline, List<String> participantsId){
        if ( !validateEvent(id, name,description, date,idDiscipline, participantsId)){
            return "Event can't be updated. " + this.returnMessage;
        }
        if (this.dao.findById(id) == null ) return "Event can't be updated. Event doesn't exist";

        Event eventToAdd = this.convertEvent(id, name, description,date,idDiscipline, participantsId);
        boolean result = this.dao.update(eventToAdd);
        return result? "Event updated successfully" : "Something went wrong x(. Try again later!";
    }

    public String delete(String id){
        if (!validNumbers(id) ) return "Invalid id ( only numbers )";
        if ( dao.findById(id) == null ) return "Event doesn't exist";

        return dao.delete(id) ? "Event deleted succesffully" : "Something went wrong x(. Try again later!";
    }

    private boolean validateInput( String st ) {

        if (st.replaceAll("^[\\s.,;:¿?¡!%$&#@()'\"`´‘’“”«»–—–\\-—•·•0-9]*$","").isBlank()) return false;
        String regex = "[áéíóúüñÑÁÉÍÓÚÜa-zA-Z\\s.,;¿?¡!()0-9]+";
        return Pattern.matches(regex, st);
    }

    private boolean validNumbers( String id ){
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, id);
    }

    private boolean validateEvent(String id, String name, String description, String date, String idDiscipline, List<String> participantsId){
        if (!validNumbers(id)){
            this.returnMessage = "Invalid id ( only numbers )";
            return false;
        }
        if (!validateInput(name)){
            this.returnMessage = "Invalid name";
            return false;
        }
        if (!validateInput(description)){
            this.returnMessage = "Description contains invalid characters";
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            auxDate = format.parse(date);
        }catch (ParseException e) {
            this.returnMessage = "Invalid date";
            return false;
        }
        if (this.dao.getDisciplineDAO().findById(idDiscipline) == null){
            this.returnMessage = "Discpline doesn't exist.";
            return false;
        }
        if (!validateLeaderboard(participantsId)){
            this.returnMessage = "The leaderboard contains usert that doesn't exist or it contains duplicated users";
            return false;
        }
        return true;
    }

    private boolean validateLeaderboard( List<String> participantsId ){
        HashSet<String> set = new HashSet<>(participantsId);
        if (participantsId.size() != set.size()) return false;
        for ( String id : participantsId ){
            if (dao.getParticipants().findById(id) == null) return false;
        }
        return true;
    }

    private Event convertEvent(String id, String name, String description, String date, String idDiscipline, List<String> participantsId){
        Discipline discipline = this.dao.getDisciplineDAO().findById(idDiscipline);
        HashMap<Participant, Integer> leaderboard = new HashMap<>();
        for (int i = 0; i < participantsId.size(); i++ ){
            Participant aux = this.dao.getParticipants().findById(participantsId.get(i));
            leaderboard.put(aux, (i+1));
        }

        return new Event(id, name,description,this.auxDate,discipline,leaderboard);
    }

    public List<Event> read(){
        return dao.read();
    }

    public String getParticipantEvent(String participantId) {
        List<Event> allEvents = dao.read();
        StringBuilder eventsBuilder = new StringBuilder();

        for (Event event : allEvents) {
            for (Map.Entry<Participant, Integer> entry : event.getLeaderboard().entrySet()) {
                Participant participant = entry.getKey();
                Integer position = entry.getValue();
                if (participant.getId().equals(participantId)) {
                    eventsBuilder.append(event.getName()).append(": ").append(position).append("\n");
                    eventsBuilder.append("\n");
                }
            }
        }

        return eventsBuilder.toString();
    }

    public void removeStudent( String id ){
        List<Event> allEvents = dao.read();
        Participant removed = dao.getParticipants().findById(id);

        for (Event event : allEvents ){
            Integer positionToDelete = event.getLeaderboard().remove(removed);

            if (positionToDelete != null) {

                for (Map.Entry<Participant, Integer> entry : event.getLeaderboard().entrySet()) {
                    Integer currentPosition = entry.getValue();
                    if (currentPosition > positionToDelete) {
                        entry.setValue(currentPosition - 1);
                    }
                }

                dao.update(event);
            }

        }
    }

    public void removeDiscipline( String id ){
        Discipline removed = dao.getDisciplineDAO().findById(id);
        if (removed == null){
            return;
        }
        List<Event> allEvents = dao.read();
        Discipline voidDiscipline = new Discipline("void", "No discipline", "No description", false);
        dao.getDisciplineDAO().save(voidDiscipline);

        Discipline disciplineReplacement = dao.getDisciplineDAO().findById("void");
        for (Event event : allEvents ){

            if ( event.getDiscipline().getId().compareTo(id) == 0){
                event.setDiscipline(disciplineReplacement);
                dao.update(event);
            }
        }

        List<Participant> allParticipants = dao.getParticipants().readParticipants();
        for (Participant p : allParticipants ){
            if (p.getDiscipline().getId().compareTo(id) == 0){
                p.setDiscipline(disciplineReplacement);
                dao.getParticipants().editParticipant(p.getId(), p.getName(), p.getAge(), p.getGender(), p.getMail(), p.getWeight(), p.getHeight(), p.getDiscipline());
            }
        }
    }
}
