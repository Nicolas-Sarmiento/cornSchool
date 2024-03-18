package co.edu.uptc.cornschool.servlets;

import co.edu.uptc.cornschool.controller.DisciplineController;
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

@WebServlet(name = "AddDiscipline", value = "/AddDiscipline")
public class AddDiscipline extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body.toString(), JsonObject.class);

        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        boolean inGroup = jsonObject.get("inGroup").getAsBoolean();

        DisciplineController controller = new DisciplineController();
        String result = controller.save(id, name, description, inGroup);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", result);

        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }
}
