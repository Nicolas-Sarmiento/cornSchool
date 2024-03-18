package co.edu.uptc.cornschool.servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.cornschool.controller.EventController;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "DeleteEventServlet", value = "/DeleteEvent-servlet")
public class DeleteEvent extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            JsonObject jsonObject = gson.fromJson(body.toString(), JsonObject.class);

            String id = jsonObject.get("id").getAsString();

            EventController controller = new EventController();
            message = controller.delete(id);

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