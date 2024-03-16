package co.edu.uptc.cornschool.persistence;

import co.edu.uptc.cornschool.model.Discipline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DisciplineDAOTest {
    private DisciplineDAO dao;
    @BeforeEach
    void setup(){
        this.dao = new DisciplineDAO();
    }

    @Test
    void save() {
        Discipline sampleExistent = new Discipline("747721403", "takwondo", "Learn Takwondo art", false);
        assertFalse( this.dao.save(sampleExistent) );

        Discipline sampleNew = new Discipline("123","Basketball", "Play basketball", true);
        assertTrue(this.dao.save(sampleNew));

        this.dao.delete(sampleNew.getId());
    }

    @Test
    void read() {
        ArrayList<Discipline> disciplines = dao.read();
        for (Discipline d : disciplines ){
            assertNotNull(d);
        }
    }

    @Test
    void update() {
        Discipline sample1 = new Discipline("2312", "Artes marciales","Pela como Lee sin", false);
        Discipline sample1Edit = new Discipline("2312", "Artes marciales","Pela como Bruce Lee", false);
        Discipline sample2 = new Discipline("2347", "Volley","Haikuu", true);
        Discipline sample2Edit = new Discipline("2347", "Volley Arena","Haikuu !!", false);

        dao.save(sample1);
        dao.save(sample2);


        assertTrue( dao.update( sample1Edit) );
        assertTrue( dao.update( sample2Edit) );
        assertEquals(dao.findById(sample1.getId()).getDescription(), sample1Edit.getDescription());
        assertEquals(dao.findById(sample1.getId()).getInGroup(), sample1Edit.getInGroup());
        assertEquals(dao.findById(sample1.getId()).getName(), sample1Edit.getName());

        assertEquals(dao.findById(sample2.getId()).getName(), sample2Edit.getName());
        assertEquals(dao.findById(sample2.getId()).getDescription(), sample2Edit.getDescription());
        assertEquals(dao.findById(sample2.getId()).getInGroup(), sample2Edit.getInGroup());


        dao.delete(sample1.getId());
        dao.delete(sample2.getId());

    }

    @Test
    void delete() {
        Discipline sample1 = new Discipline("2312", "Artes marciales","Pela como Lee sin", false);
        Discipline sample2 = new Discipline("2347", "Volley","Haikuu", true);

        dao.save(sample1);
        dao.save(sample2);

        assertTrue(dao.delete(sample1.getId()));
        assertTrue(dao.delete(sample2.getId()));
        assertFalse(dao.delete("000000"));
        assertFalse(dao.delete("sgdfhsgdf"));
        assertFalse(dao.delete(""));
        assertFalse(dao.delete("fdhystd"));
    }

    @Test
    void findById() {

        Discipline sample1 = new Discipline("2312", "Artes marciales","Pela como Lee sin", false);
        Discipline sample2 = new Discipline("2347", "Volley","Haikuu", true);

        dao.save(sample1);
        dao.save(sample2);

        assertNotNull(dao.findById(sample1.getId()));
        assertNotNull(dao.findById(sample2.getId()));
        assertNull(dao.findById("adgfjhagsd"));
        assertNull(dao.findById(""));
        assertNull(dao.findById("23671627"));
        assertNull(dao.findById("00000000"));

        dao.delete(sample1.getId());
        dao.delete(sample2.getId());
    }
}