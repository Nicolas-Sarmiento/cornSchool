package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Participant;
import co.edu.uptc.cornschool.persistence.DisciplineDAO;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DisciplineController {
    private DisciplineDAO dao;
    private ParticipantsController participantsController;

    public DisciplineController() {
        this.dao = new DisciplineDAO();
        this.participantsController = new ParticipantsController();
    }

    public String save(String id, String name, String description, boolean inGroup ){
        if ( !validNumbers(id) )return "The discipline can't be added. Invalid Id( Only numbers )";
        if (!validateName(name.replaceAll(" ", ""))) return "The discipline can't be added. Invalid name ( Only letters )";
        if (!validateInput(description)) return "The discipline can't be added. Description contains invalid characters";

        if ( dao.findById( id ) != null ) return "Discipline exists. Create a new one!";

        name = name.replaceAll("\\s{2,}", " ");
        description = description.replaceAll("\\s{2,}", " ");

        Discipline discipline = new Discipline(id, name, description, inGroup);

        if ( isRepeated( discipline ) ) return "Discipline exists. Create a new one!";

        boolean result = dao.save( discipline );

        return  result ? "Discipline saved successfully" : "Something went wrong x(. Try again later!";
    }

    public ArrayList<Discipline> read(){
        return dao.read();
    }

    public String update(String id, String name, String description, boolean inGroup ){
        if ( !validNumbers(id) )return "The discipline can't be updated. Invalid Id( Only numbers )";
        if (!validateName(name.replaceAll(" ", ""))) return "The discipline can't be updated. Invalid name ( Only letters )";
        if (!validateInput(description)) return "The discipline can't be updated. Description contains invalid characters";

        if ( dao.findById( id ) == null ) return  "Discipline doesn't exist. Create a new one!";

        name = name.replaceAll("\\s{2,}", " ");
        description = description.replaceAll("\\s{2,}", " ");

        Discipline discipline = new Discipline(id, name, description, inGroup);

        if ( isRepeated( discipline ) ) return "Any discipline was updated!";

        boolean result = dao.update( discipline );

        return  result ? "Discipline updated successfully" : "Any discipline was updated!";
    }

    public String delete( String id ){
        if ( !validNumbers(id) ) return " Invalid Id( Only numbers )";
        if ( dao.findById(id) == null ) return "Discipline doesn't exists";

        return dao.delete(id) ? "Discipline deleted successfully" : "Something went wrong x(. Try again later!";
    }


    public ArrayList<Participant> getDisciplineParticipants( String disciplineId ){
        ArrayList<Participant> participants = this.participantsController.readParticipants();
        ArrayList<Participant> inDiscipline = new ArrayList<>();
        for (Participant p : participants ){
            if (p.getDiscipline().getId().compareTo(disciplineId) == 0 ){
                inDiscipline.add(p);
            }
        }
        return inDiscipline;
    }

    public Discipline findById( String id ){
        return dao.findById(id);
    }

    private boolean validateInput( String st ) {

        if (st.replaceAll("^[\\s.,;:¿?¡!%$&#@()'\"`´‘’“”«»–—–\\-—•·•0-9]*$","").isBlank()) return false;
        String regex = "[áéíóúüñÑÁÉÍÓÚÜa-zA-Z\\s.,;¿?¡!()0-9]+";
        return Pattern.matches(regex, st);
    }

    private boolean validateName( String st ){
        String regex = "^[áéíóúüñÑÁÉÍÓÚÜa-zA-Z]+$";
        return Pattern.matches(regex, st);
    }

    private boolean validNumbers( String id ){
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, id);
    }

    private boolean isRepeated( Discipline discipline ){
        ArrayList<Discipline> disciplines = dao.read();
        for ( Discipline d : disciplines ){
            if (d.equals(discipline)) return true;
        }
        return false;
    }
}
