package co.edu.uptc.cornschool.persistence;

import co.edu.uptc.cornschool.model.Event;
import co.edu.uptc.cornschool.model.Participant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventDAOTest {

    @Test
    void read() {
        EventDAO dao = new EventDAO();
        ArrayList<Event> events = dao.read();
        for (Event e: events ){
            System.out.println(e.getId());
            System.out.println(e.getName());
            System.out.println(e.getDescription());
            System.out.println(e.getDate().toString());
            System.out.println(e.getDiscipline());
            for (Participant p : e.getLeaderboard().keySet()){
                System.out.println(p);
                System.out.println(" position "  + e.getLeaderboard().get(p));
            }
        }
    }

    @Test
    void findById() {
        EventDAO dao = new EventDAO();
        assertNotNull( dao.findById("1"));
        assertNull( dao.findById("4"));
        assertNotNull( dao.findById("2"));
        System.out.println(dao.findById("2"));
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}