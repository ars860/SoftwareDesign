package repository;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import domain.Product;
import domain.User;
import org.bson.Document;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;

public class ProductRepository {
    private MongoClient client = createMongoClient();
    private MongoCollection<Document> collection = client.getDatabase("ppo").getCollection("product");

    private static void getPrintln(User user) {
        System.out.println(user);
    }

    public Observable<Product> getAllProducts() {
        return collection.find().toObservable().map(Product::new).defaultIfEmpty(null);
    }

//    public Observable<User> getUserById(long id) {
//        return collection.find(eq("id", id)).toObservable().map(User::new);
//    }

    public Observable<Success> addProduct(Product product) {
        return collection.insertOne(product.toDocument()).defaultIfEmpty(null).onErrorReturn(e -> null);
    }

    private MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}