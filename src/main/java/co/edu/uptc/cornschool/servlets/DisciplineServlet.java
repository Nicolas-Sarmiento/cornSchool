package co.edu.uptc.cornschool.servlets;

import java.io.*;
import java.util.ArrayList;

import co.edu.uptc.cornschool.controller.DisciplineController;
import co.edu.uptc.cornschool.controller.ParticipantsController;
import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Participant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "DisciplineServlet", value = "/discipline-servlet")
public class DisciplineServlet extends HttpServlet {


    public void init() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        DisciplineController disciplineController = new DisciplineController();


        Gson gson = new Gson();

        JsonObject result = new JsonObject();
        ArrayList<Discipline> disciplines = disciplineController.read();

        if ( disciplines == null ){
            result.addProperty("status", false);
            response.getWriter().write(result.toString());
            return;
        }

        JsonArray body = new JsonArray();
        for ( Discipline discipline : disciplines ){
            JsonObject aux = new JsonObject();
            aux.addProperty("id", discipline.getId());
            aux.addProperty("name", discipline.getName());
            aux.addProperty("description", discipline.getDescription());
            aux.addProperty("inGroup", discipline.getInGroup());

            ArrayList<Participant> participants = disciplineController.getDisciplineParticipants(discipline.getId());

            aux.addProperty("participants", gson.toJson(getParticipants(participants)));
            body.add(aux);
        }
        result.addProperty("status", true);
        result.addProperty("content", gson.toJson(body));
        response.getWriter().write(result.toString());

    }

    private JsonArray getParticipants( ArrayList<Participant> participants ){
        JsonArray array = new JsonArray();
        for ( Participant participant : participants){
            JsonObject object = new JsonObject();
            object.addProperty("id",participant.getId());
            object.addProperty("name", participant.getName());
            object.addProperty("age", participant.getAge());
            object.addProperty("gender", participant.getGender());
            object.addProperty("discipline", participant.getDiscipline().getName());
            object.addProperty("mail", participant.getMail());
            object.addProperty("height", participant.getHeight());
            object.addProperty("weight", participant.getWeight());
            array.add(object);
        }
        return array;
    }

    public void destroy() {
    }
}