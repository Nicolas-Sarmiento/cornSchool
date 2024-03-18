package co.edu.uptc.cornschool.servlets;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import co.edu.uptc.cornschool.controller.EventController;
import co.edu.uptc.cornschool.model.Event;
import co.edu.uptc.cornschool.model.Participant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "EventServlet", value = "/event-servlet")
public class EventServlet extends HttpServlet {

    public void init() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

        EventController controller = new EventController();

        ArrayList<Event> events = (ArrayList<Event>) controller.read();
        JsonObject result = new JsonObject();
        if ( events == null ){
            result.addProperty("status", false);
            response.getWriter().write(result.toString());
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        JsonArray body = new JsonArray();
        for (Event event : events ){
            JsonObject aux = new JsonObject();

            aux.addProperty("id", event.getId());
            aux.addProperty("name", event.getName());
            aux.addProperty("description", event.getDescription());
            aux.addProperty("date", format.format(event.getDate()));
            aux.addProperty("discipline", event.getDiscipline().getName());
            aux.addProperty("leaderboard", gson.toJson(this.getParticipants(event.getLeaderboard())));
            body.add(aux);
        }
        result.addProperty("status", true);
        result.addProperty("content", gson.toJson(body));
        response.getWriter().write(result.toString());
    }

    private JsonArray getParticipants(Map<Participant, Integer> participants){
        JsonArray array = new JsonArray();
        for (Participant p : participants.keySet()){
            JsonObject aux = new JsonObject();
            aux.addProperty("id", p.getId());
            aux.addProperty("name", p.getName());
            aux.addProperty("position", participants.get(p));
            array.add(aux);
        }
        return array;
    }

    public void destroy() {
    }
}