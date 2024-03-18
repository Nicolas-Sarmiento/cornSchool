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
            return "No se pudo guardar el evento. " + this.returnMessage;
        }
        if (this.dao.findById(id) != null ) return "No se pudo guardar el evento. Evento existente";

        Event eventToAdd = this.convertEvent(id, name, description,date,idDiscipline, participantsId);
        boolean result = this.dao.save(eventToAdd);
        return result? "Evento guardado exitosamente" : "Algo salió mal. Intentalo más tarde";
    }

    public String update(String id, String name, String description, String date, String idDiscipline, List<String> participantsId){
        if ( !validateEvent(id, name,description, date,idDiscipline, participantsId)){
            return "No se pudo guardar el evento. " + this.returnMessage;
        }
        if (this.dao.findById(id) == null ) return "No se pudo actualizar el evento. Evento no existente";

        Event eventToAdd = this.convertEvent(id, name, description,date,idDiscipline, participantsId);
        boolean result = this.dao.update(eventToAdd);
        return result? "Evento actualizado exitosamente" : "No se actualizó ningun evento";
    }

    public String delete(String id){
        if (!validNumbers(id) ) return "El id no es válido ( Solo números )";
        if ( dao.findById(id) == null ) return "Evento no existente";

        return dao.delete(id) ? "Evento eliminado exitosamente" : "Algo salió mal. Intentalo más tarde";
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
            this.returnMessage = "El id no cumple con los estandares ( Solo números )";
            return false;
        }
        if (!validateInput(name)){
            this.returnMessage = "El nombre contiene caracteres no válidos";
            return false;
        }
        if (!validateInput(description)){
            this.returnMessage = "La descripción contiene caracteres no válidos";
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            auxDate = format.parse(date);
        }catch (ParseException e) {
            this.returnMessage = "La fecha del evento ingresada no es válida.";
            return false;
        }
        if (this.dao.getDisciplineDAO().findById(idDiscipline) == null){
            this.returnMessage = "La disciplina ingresada no existe.";
            return false;
        }
        if (!validateLeaderboard(participantsId)){
            this.returnMessage = "La lista de participantes contiene repetidos o participantes no existentes";
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
}
