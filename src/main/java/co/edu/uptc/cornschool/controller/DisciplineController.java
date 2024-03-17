package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.persistence.DisciplineDAO;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DisciplineController {
    private DisciplineDAO dao;

    public DisciplineController() {
        this.dao = new DisciplineDAO();
    }

    public String save(String id, String name, String description, boolean inGroup ){
        if ( !validNumbers(id) )return "No se pudo añadir disciplina. El id no cumple con los estandares ( Solo números )";
        if (!validateName(name.replaceAll(" ", ""))) return "No se pudo añadir disciplina. El nombre no cumple con los estandares ( Solo letras )";
        if (!validateInput(description)) return "No se pudo añadir disciplina. La descripción contiene caracteres no válidos";

        if ( dao.findById( id ) != null ) return "La disciplina ya existe. Cree una diferente";

        name = name.replaceAll("\\s{2,}", " ");
        description = description.replaceAll("\\s{2,}", " ");

        Discipline discipline = new Discipline(id, name, description, inGroup);

        if ( isRepeated( discipline ) ) return "La disciplina ya existe. Cree una diferente";

        boolean result = dao.save( discipline );

        return  result ? "Disciplina guardada exitosamente" : "Algo salió mal. Intentalo más tarde";
    }

    public ArrayList<Discipline> read(){
        return dao.read();
    }

    public String update(String id, String name, String description, boolean inGroup ){
        if ( !validNumbers(id) )return "No se pudo actualizar disciplina. El id no cumple con los estandares ( Solo números )";
        if (!validateName(name.replaceAll(" ", ""))) return "No se pudo actualizar disciplina. El nombre no cumple con los estandares ( Solo letras )";
        if (!validateInput(description)) return "No se pudo actualizar disciplina. La descripción contiene caracteres no válidos";

        if ( dao.findById( id ) == null ) return "La disciplina no existe. Cree una";

        name = name.replaceAll("\\s{2,}", " ");
        description = description.replaceAll("\\s{2,}", " ");

        Discipline discipline = new Discipline(id, name, description, inGroup);

        if ( isRepeated( discipline ) ) return "No se actualizó ninguna disciplina";

        boolean result = dao.update( discipline );

        return  result ? "Disciplina actualizada exitosamente" : "No se actualizó ninguna disciplina";
    }

    public String delete( String id ){
        if ( !validNumbers(id) ) return "El id no es válido ( Solo números )";
        if ( dao.findById(id) == null ) return "Disciplina no existente";

        return dao.delete(id) ? "Disciplina eliminada exitosamente" : "Algo salió mal. Intentalo más tarde";
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
