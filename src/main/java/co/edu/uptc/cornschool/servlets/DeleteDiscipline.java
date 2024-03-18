package co.edu.uptc.cornschool.servlets;

import co.edu.uptc.cornschool.controller.DisciplineController;
import co.edu.uptc.cornschool.controller.ParticipantsController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "DeleteDiscipline", value = "/DeleteDiscipline")
public class DeleteDiscipline extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

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
        System.out.println(id);
        DisciplineController controller = new DisciplineController();

        String result = controller.delete(id);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", result);

        response.setContentType("application/json");
        response.getWriter().write(responseJson.toString());
    }
}