package repository;


import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import domain.User;
import org.bson.Document;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;


public class UserRepository {
    private MongoClient client = createMongoClient();
    private MongoCollection<Document> collection = client.getDatabase("ppo").getCollection("user");

    private static void getPrintln(User user) {
        System.out.println(user);
    }

    public Observable<User> getAllUsers() {
        return collection.find().toObservable().map(User::new).defaultIfEmpty(null);
    }

    public Observable<User> getUserById(long id) {
        return collection.find(eq("id", id)).toObservable().map(User::new).defaultIfEmpty(null);
    }

    public Observable<Success> addUser(User user) {
        return collection.insertOne(user.toDocument()).defaultIfEmpty(null).onErrorReturn(null);
    }

    private MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}