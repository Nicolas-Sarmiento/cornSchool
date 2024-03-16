package co.edu.uptc.cornschool.persistence;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.nin;

import co.edu.uptc.cornschool.model.Discipline;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class DisciplineDAO {

    private static final String URL ="mongodb+srv://amongus4:mongus444@conrschool.evfn33h.mongodb.net/?retryWrites=true&w=majority&appName=ConrSchool";
    private static final String DB = "collegue";
    private static final String COLLECTION = "disciplines";
    public DisciplineDAO() {
    }

    public boolean save(Discipline discipline){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            InsertOneResult result = collection.insertOne( new Document()
                    .append("_id", discipline.getId())
                    .append("name", discipline.getName())
                    .append("description", discipline.getDescription())
                    .append("inGroup", discipline.getInGroup())
            );

            return true;

        }catch (MongoException e){
            return false;
        }
    }

    public ArrayList<Discipline> read(){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            MongoCursor<Document> docs = collection.find().iterator();
            ArrayList<Discipline> disciplines = new ArrayList<>();
            try {
                while (docs.hasNext()){
                    disciplines.add(this.parseJson(docs.next()));
                }
            }finally {
                docs.close();
            }
            return disciplines;
        }catch (MongoException e){
            return null;
        }
    }

    private Discipline parseJson( Document doc ){
        String id = doc.getString("_id");
        String name = doc.getString("name");
        String description = doc.getString("description");
        Boolean inGroup = doc.getBoolean("inGroup");
        return new Discipline(id, name, description, inGroup);
    }

    public boolean update( Discipline discipline ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            Document query = new Document().append("_id", discipline.getId());

            Bson updates = Updates.combine(
                    Updates.set("name" , discipline.getName()),
                    Updates.set("description" , discipline.getDescription()),
                    Updates.set("inGroup", discipline.getInGroup())
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

    public Discipline findById( String id ){
        try (MongoClient mongoClient = MongoClients.create(URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            Document doc = collection.find(eq("_id", id)).first();
            if ( doc != null ){
                return this.parseJson(doc);
            }else {
                return null;
            }
        }catch (MongoException e){
            return null;
        }
    }
}
