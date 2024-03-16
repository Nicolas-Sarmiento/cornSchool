package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Participant;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.cj.xdevapi.JsonArray;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class ParticipantsController {

    String uri = "<mongodb+srv://amongus4:amongus4 @conrschool.evfn33h.mongodb.net/?retryWrites=true&w=majority&appName=ConrSchool>";

    public ArrayList<Participant> readParticipants() {
        ArrayList<Participant> participants = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            for (Document doc : collection.find()) {
                String id = doc.getString("_id");
                String name = doc.getString("name");
                int age = doc.getInteger("age");
                boolean gender = doc.getBoolean("gender");
                String discipline = doc.getString("discipline");
                String mail = doc.getString("mail");
                double height = doc.getDouble("height");
                double weight = doc.getDouble("weight");

                Discipline discipline1 = new Discipline("","","",false);

                Participant participant = new Participant(id,name,age,gender,mail,weight,height,discipline1);

                participants.add(participant);
            }
            return participants;

        } catch (Exception e) {
            return null;
        }
    }

    public Participant findById(String idFind){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            for (Document doc : collection.find()) {
                String id = doc.getString("_id");
                if( idFind.equals(id) ){
                    String name = doc.getString("name");
                    int age = doc.getInteger("age");
                    boolean gender = doc.getBoolean("gender");
                    String discipline = doc.getString("discipline");
                    String mail = doc.getString("mail");
                    double height = doc.getDouble("height");
                    double weight = doc.getDouble("weight");

                    Discipline discipline1 = new Discipline("","","",false);

                    return new Participant(id,name,age,gender,mail,weight,height,discipline1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
