package co.edu.uptc.cornschool.servlets;

import co.edu.uptc.cornschool.controller.DisciplineController;
import co.edu.uptc.cornschool.controller.ParticipantsController;
import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Participant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

@WebServlet(name = "AddParticipant", value = "/AddParticipant")
public class AddParticipant extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String line = null;
        StringBuilder body = new StringBuilder();
        while (( line = reader.readLine()) != null ){
            body.append(line);
        }

        System.out.println( body.toString() );

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body.toString(), JsonObject.class);

        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        int age = jsonObject.get("age").getAsInt();
        boolean gender = jsonObject.get("gender").getAsBoolean();
        String mail = jsonObject.get("mail").getAsString();
        double weight = jsonObject.get("weight").getAsDouble();
        double height = jsonObject.get("height").getAsDouble();
        String disciplineId = jsonObject.get("discipline").getAsString();

        DisciplineController disciplineController = new DisciplineController();
        Discipline discipline = disciplineController.findById(disciplineId);


        ParticipantsController controller = new ParticipantsController();
        boolean success = controller.addParticipant(id, name, age, gender, mail, weight, height, discipline);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", success);
        if (success) {
            responseJson.addProperty("message", "Participant added successfully");
        } else {
            responseJson.addProperty("message", "Failed to add participant");
        }

        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }
}