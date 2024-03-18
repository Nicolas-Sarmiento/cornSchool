package co.edu.uptc.cornschool.servlets;

import co.edu.uptc.cornschool.controller.DisciplineController;
import co.edu.uptc.cornschool.controller.EventController;
import co.edu.uptc.cornschool.controller.ParticipantsController;
import co.edu.uptc.cornschool.model.Discipline;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "DeleteParticipant", value = "/DeleteParticipant")
public class DeleteParticipant extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String line = null;
        StringBuilder body = new StringBuilder();
        while (( line = reader.readLine()) != null ){
            body.append(line);
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body.toString(), JsonObject.class);

        String id = jsonObject.get("id").getAsString();


        EventController eventController = new EventController();
        ParticipantsController controller = new ParticipantsController();
        eventController.removeStudent(id);
        boolean success = controller.deleteParticipant(id);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", success);
        if (success) {
            responseJson.addProperty("message", "Participant deleted successfully");
        } else {
            responseJson.addProperty("message", "Failed to delete participant");
        }

        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }
}