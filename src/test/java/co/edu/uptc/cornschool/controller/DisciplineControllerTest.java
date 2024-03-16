package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.persistence.DisciplineDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplineControllerTest {
    private DisciplineController controller;
    private DisciplineDAO dao;
    @BeforeEach
    void setup(){
        this.controller = new DisciplineController();
        this.dao = new DisciplineDAO();
    }
    @Test
    void save() {
        controller.save("2352","Dota", "Enjoy Dota2 with your school parthners. Practice for 4 hour everyday !!", true);

        assertEquals("Dota", controller.findById("2352").getName());

        assertNotEquals("Disciplina guardada exitosamente",controller.save("2352","Dota2", "Enjoy Dota2 with your school parthners. Practice for 4 hour everyday !!", true));
        assertNotEquals("Disciplina guardada exitosamente",controller.save("2352","Dota", "Enjoy Dota2 with your school parthners. Practice for 4 hour everyday !!", true));
        assertNotEquals("Disciplina guardada exitosamente", controller.save("ywtrsf", "ajsdfkj","asdjfjk", false));
        assertNotEquals("Disciplina guardada exitosamente", controller.save("1234", "ajsdfk^&^#@j","asdjfjk", false));
        assertNotEquals("Disciplina guardada exitosamente", controller.save("1234", "abcjd","asdjfjk^@#%^@%#", false));
        assertNotEquals("Disciplina guardada exitosamente", controller.save("1234", "abcjd68989","asdjfjk^@#%^@%#", false));
        dao.delete("2352");

    }

    @Test
    void update() {
        Discipline sample = new Discipline("2367","Football", "Practicing football yujuu!!", true);
        controller.save(sample.getId(), sample.getName(), sample.getDescription(), sample.getInGroup());
        controller.save("123", "abc", "dfe", true);

        assertNotEquals("Disciplina actualizada exitosamente", controller.update("123","Football", "Practicing football yujuu!!", true));

        controller.update(sample.getId(), sample.getName(), "Football edit", false);
        assertEquals("Football edit", controller.findById(sample.getId()).getDescription());
        assertFalse( controller.findById(sample.getId()).getInGroup());

        controller.update(sample.getId(), sample.getName(), "Football edit yujuuuu", false);

        assertEquals("Football edit yujuuuu", controller.findById(sample.getId()).getDescription());

        dao.delete(sample.getId());
        dao.delete("123");
    }

    @Test
    void delete() {
        Discipline sample = new Discipline("2367","Football", "Practicing football yujuu!!", true);
        controller.save(sample.getId(), sample.getName(), sample.getDescription(), sample.getInGroup());

        assertEquals("Disciplina eliminada exitosamente", controller.delete(sample.getId()));
        assertNotEquals("Disciplina eliminada exitosamente", controller.delete(""));
        assertNotEquals("Disciplina eliminada exitosamente", controller.delete("sdfsdf"));
        assertNotEquals("Disciplina eliminada exitosamente", controller.delete("sdfsdf^&^@#&"));
        assertNotEquals("Disciplina eliminada exitosamente", controller.delete("000"));
        assertNotEquals("Disciplina eliminada exitosamente", controller.delete("a4"));
    }
}