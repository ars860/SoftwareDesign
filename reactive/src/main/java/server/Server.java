package server;

import domain.Currency;
import domain.Product;
import domain.User;
import io.reactivex.netty.protocol.http.server.HttpServer;
import repository.ProductRepository;
import repository.UserRepository;
import rx.Observable;

import java.net.URI;

public class Server {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Server(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void startServer(int port) {
        HttpServer
                .newServer(port)
                .start((req, resp) -> {

                    String uriString = req.getUri();
                    Observable<String> response;
                    try {
                        URI uri = new URI(uriString);
                        var params = req.getQueryParameters();

                        switch (uri.getPath()) {
                            case "/add_user" -> {
                                int userId = Integer.parseInt(params.get("id").get(0));
                                String userName = params.get("name").get(0);
                                String userLogin = params.get("login").get(0);
                                Currency userPref = Currency.fromString(params.get("pref").get(0));

                                response = userRepository.addUser(new User(userId, userName, userLogin, userPref)).map(s -> "User added");
                            }
                            case "/get_user" -> {
                                int userId = Integer.parseInt(params.get("id").get(0));

                                response = userRepository.getUserById(userId).map(user -> {
                                    if (user != null) {
                                        return user.toString();
                                    } else {
                                        return "No such user!";
                                    }
                                });
                            }
                            case "/add_product" -> {
                                String productName = params.get("name").get(0);
                                int productId = Integer.parseInt(params.get("id").get(0));
                                double productCost = Double.parseDouble(params.get("cost").get(0));
                                int productAddedBy = Integer.parseInt(params.get("user_id").get(0));
                                Currency currency = Currency.fromString(params.get("curr").get(0));

                                response = userRepository.getUserById(productAddedBy).flatMap(
                                        user -> {
                                            if (user != null) {
                                                return productRepository.addProduct(new Product(productId, productName, productCost, currency, productAddedBy)).map(s -> {
                                                    if (s != null) {
                                                        return "Product added!";
                                                    } else {
                                                        return "Error! This product can not be added!";
                                                    }
                                                });
                                            } else {
                                                return Observable.just("No such user!");
                                            }
                                        }
                                );
                            }
                            case "/get_products" -> {
                                int userId = Integer.parseInt(params.get("user_id").get(0));

                                response = userRepository.getUserById(userId).flatMap(user -> {
                                    if (user != null) {
                                        return productRepository.getAllProducts().map(pr -> pr.toString(user.getCurrency()));
                                    } else {
                                        return Observable.just("No such user!");
                                    }
                                });

                            }
                            default -> response = Observable.just("Unknown command!\n" + help());
                        }
                    } catch (Exception e) {
                        response = Observable.just("bad uri\n" + help());
                    }

                    return resp.writeString(response.onErrorReturn(e -> "Error: " + e.getMessage()));
                })
                .awaitShutdown();
    }

    private static String help() {
        return """
                Commands:
                /add_user {id} {name} {login} {pref}
                /get_user {id}
                /add_product {user_id} {name} {id} {cost} {curr}
                /get_products {user_id}
                """;

    }
}
