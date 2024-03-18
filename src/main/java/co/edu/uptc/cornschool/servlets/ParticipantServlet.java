package co.edu.uptc.cornschool.servlets;

import co.edu.uptc.cornschool.controller.EventController;
import co.edu.uptc.cornschool.controller.ParticipantsController;
import co.edu.uptc.cornschool.model.Event;
import co.edu.uptc.cornschool.model.Participant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ParticipantServlet", value = "/ParticipantServlet")
public class ParticipantServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        ParticipantsController controller = new ParticipantsController();
        EventController eventController = new EventController();
        Gson gson = new Gson();

        ArrayList<Participant> participants = new ArrayList<>();
        JsonArray array = new JsonArray();

        try{
            participants = controller.readParticipants();
            if( participants != null ){
                for ( Participant participant : participants){
                    JsonObject object = new JsonObject();
                    object.addProperty("id",participant.getId());
                    object.addProperty("name", participant.getName());
                    object.addProperty("age", participant.getAge());
                    object.addProperty("gender", participant.getGender());
                    object.addProperty("discipline", participant.getDiscipline().getName());
                    object.addProperty("disciplineId", participant.getDiscipline().getId());
                    object.addProperty("mail", participant.getMail());
                    object.addProperty("height", participant.getHeight());
                    object.addProperty("weight", participant.getWeight());

                    String events = eventController.getParticipantEvent(participant.getId());
                    object.addProperty("events", events);

                    array.add(object);
                }
                response.getWriter().write( gson.toJson(array) );
            }else {
                response.getWriter().write( gson.toJson("null"));
            }
        }catch ( Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}