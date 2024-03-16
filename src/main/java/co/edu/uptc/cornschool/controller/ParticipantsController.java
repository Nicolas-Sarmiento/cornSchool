package co.edu.uptc.cornschool.controller;

import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Participant;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mysql.cj.xdevapi.JsonArray;
import org.bson.Document;

import java.util.ArrayList;
import java.util.regex.Pattern;

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
    public boolean validCharacters( String name ) {
        String regex = "^[a-zA-Z\\s]+$";
        return Pattern.matches(regex, name);
    }
    public boolean validNumbers( String id ){
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, id);
    }

    public boolean addParticipant(String id, String name, int age, boolean gender, double weight, double height, Discipline discipline){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            if( !validCharacters(name) ) return false;
            if( name.trim().isEmpty() ) return false;
            if( !validNumbers(id) ) return false;
            if( id.trim().isEmpty() ) return false;

            Document participant = new Document("_id", id)
                    .append("name", name)
                    .append("age", age)
                    .append("gender", gender)
                    .append("weight", weight)
                    .append("height", height)
                    .append("discipline", discipline.getId());
            collection.insertOne(participant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editParticipant(String id, String attribute, String newValue){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            BasicDBObject searchQuery = new BasicDBObject().append("_id", id);
            BasicDBObject updateQuery = new BasicDBObject();


            if (attribute.equals("name")) {
                if (!validCharacters(newValue) || newValue.trim().isEmpty()) return false;
                updateQuery = new BasicDBObject("$set", new BasicDBObject(attribute, newValue));
            }

            if (attribute.equals("_id")) {
                if (!validNumbers(newValue) || newValue.trim().isEmpty()) return false;
                updateQuery = new BasicDBObject("$set", new BasicDBObject(attribute, newValue));
            }

            if (attribute.equals("age")) {
                try {
                    int value = Integer.parseInt(newValue);
                    updateQuery = new BasicDBObject("$set", new BasicDBObject(attribute, value));
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            if (attribute.equals("weight") || attribute.equals("height")) {
                try {
                    double value = Double.parseDouble(newValue);
                    updateQuery = new BasicDBObject("$set", new BasicDBObject(attribute, value));
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            UpdateResult result = collection.updateOne(searchQuery, updateQuery);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteParticipant(String id){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            if( !validNumbers(id) ) return false;
            if( id.trim().isEmpty() ) return false;
            if( this.findById( id ) == null ) return false;

            BasicDBObject query = new BasicDBObject("_id", id);
            DeleteResult result = collection.deleteOne(query);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Participant findById(String idFind){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("collegue");
            MongoCollection<Document> collection = database.getCollection("participants");

            if( !validNumbers(idFind) ) return null;
            if( idFind.trim().isEmpty() ) return null;

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
