package co.edu.uptc.cornschool.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventControllerTest {

    @Test
    void removeStudent() {
        EventController controller = new EventController();
        controller.removeStudent("123");
    }
}