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

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "DisciplinesServlet", value = "/DisciplinesServlet")
public class DisciplinesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        DisciplineController controller = new DisciplineController();
        Gson gson = new Gson();

        ArrayList<Discipline> disciplines = new ArrayList<>();
        JsonArray array = new JsonArray();

        try{
            disciplines = controller.read();
            if( disciplines != null ){
                for ( Discipline discipline : disciplines){
                    JsonObject object = new JsonObject();
                    object.addProperty("id",discipline.getId());
                    object.addProperty("name", discipline.getName());
                    object.addProperty("description", discipline.getDescription() );
                    object.addProperty("InGroup", discipline.getInGroup());
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