import repository.ProductRepository;
import repository.UserRepository;
import server.Server;

public class Main {
    public static void main(final String[] args) {
        Server server = new Server(new UserRepository(), new ProductRepository());
        server.startServer(8080);
    }
}
