package co.edu.uptc.cornschool.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.edu.uptc.cornschool.controller.EventController;
import co.edu.uptc.cornschool.model.Participant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "AddEventServlet", value = "/AddEvent-servlet")
public class AddEvent extends HttpServlet {

    public void init() {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        String message = "";
        try {
            System.out.println(body);
            JsonObject jsonObject = gson.fromJson(body.toString(), JsonObject.class);

            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            String date = jsonObject.get("date").getAsString();
            String idDiscipline = jsonObject.get("idDiscipline").getAsString();
            List<String> leaderboard = new ArrayList<>();
            JsonArray leaderboardJson = jsonObject.get("leaderboard").getAsJsonArray();
            for (JsonElement element : leaderboardJson ) {
                leaderboard.add(element.getAsString());
            }

            EventController controller = new EventController();
            message = controller.save(id, name, description, date, idDiscipline, leaderboard);

        }catch (Exception e) {
            message = "Something went wrong. Verify your info";
        }
        result.addProperty("message", message);
        response.setContentType("application/json");
        response.getWriter().write(result.toString());

    }

    public void destroy() {
    }
}