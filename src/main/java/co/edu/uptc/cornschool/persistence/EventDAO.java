package co.edu.uptc.cornschool.persistence;

import co.edu.uptc.cornschool.controller.ParticipantsController;
import co.edu.uptc.cornschool.model.Discipline;
import co.edu.uptc.cornschool.model.Event;
import co.edu.uptc.cornschool.model.Participant;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EventDAO {
    private static final String URL = "mongodb://localhost:27017";
    private static final String COLLECTION = "events";
    private static final String DB = "collegue";
    private ParticipantsController participants;
    private DisciplineDAO disciplineDAO;
    private SimpleDateFormat format;

    public EventDAO() {
        this.participants = new ParticipantsController();
        this.disciplineDAO = new DisciplineDAO();
        this.format = new SimpleDateFormat("dd-MM-yyyy");
    }

    public ArrayList<Event> read(){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            MongoCursor<Document> docs = collection.find().iterator();
            ArrayList<Event> events = new ArrayList<>();

            while ( docs.hasNext() ){
                events.add(this.docToObject(docs.next()));
            }
            docs.close();
            return events;
        }catch (MongoException e){
            return null;
        }
    }

    private Event docToObject( Document doc ){


        String dateValue = doc.getString("date");
        try {
            Date date = format.parse(dateValue);
            HashMap<Participant,Integer> leaderboard = this.getLeaderboard((ArrayList<Document>) doc.get("leaderboard"));

            if (leaderboard == null) return null;

            Discipline discipline = this.disciplineDAO.findById(doc.getString("discipline"));

            if (discipline == null ) return null;

            String id = doc.getString("_id");
            String name = doc.getString("name");
            String description = doc.getString("description");

            return new Event(id, name, description, date, discipline,leaderboard);
        } catch (Exception e) {
            return null;
        }
    }

    private HashMap<Participant, Integer> getLeaderboard( ArrayList<Document> documents ){
        HashMap<Participant, Integer> result = new HashMap<>();
        String participantID = "";
        Integer position = 0;
        for ( Document doc : documents ){
            participantID = doc.getString("participant");
            position = doc.getInteger("position");

            Participant aux = this.participants.findById(participantID);
            if (aux == null ) return null;

            result.put(aux, position);
        }
        return result;
    }

    public Event findById( String id ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            Document doc = collection.find(eq("_id", id)).first();
            if ( doc != null ){
                return this.docToObject(doc);
            }else {
                return null;
            }
        }catch (MongoException e){
            return null;
        }
    }

    public boolean save( Event event ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            ArrayList<Document> leaderboard = setLeaderboard(event);
            InsertOneResult result = collection.insertOne( new Document()
                    .append("_id", event.getId())
                    .append("name", event.getName())
                    .append("description", event.getDescription())
                    .append("date", format.format(event.getDate()))
                    .append("discipline",event.getDiscipline().getId())
                    .append("leaderboard",leaderboard)

            );

            return true;

        }catch (MongoException e){
            return false;
        }
    }

    private ArrayList<Document> setLeaderboard(Event event){
        ArrayList<Document> documents = new ArrayList<>();

        for (Participant p : event.getLeaderboard().keySet()){
            documents.add(new Document()
                    .append("participant",p.getId())
                    .append("postion", event.getLeaderboard().get(p))
            );
        }
        return documents;
    }

    public boolean update( Event event ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            Document query = new Document().append("_id", event.getId());

            ArrayList<Document> leaderboard = setLeaderboard(event);
            Bson updates = Updates.combine(
                    Updates.set("name" , event.getName()),
                    Updates.set("description" , event.getDescription()),
                    Updates.set("date", format.format(event.getDate())),
                    Updates.set("discipline", event.getDiscipline().getId()),
                    Updates.set("leaderboard", leaderboard)
            );

            UpdateOptions options = new UpdateOptions().upsert(false);
            UpdateResult result = collection.updateOne(query, updates, options);
            return result.getModifiedCount() != 0;
        }catch (MongoException e){
            return false;
        }
    }

    public boolean delete( String id ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            Bson query = eq("_id", id);
            DeleteResult result = collection.deleteOne(query);

            return  result.getDeletedCount() != 0;
        }catch (MongoException e){
            return false;
        }
    }

    public ParticipantsController getParticipants() {
        return participants;
    }

    public DisciplineDAO getDisciplineDAO() {
        return disciplineDAO;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }
}
