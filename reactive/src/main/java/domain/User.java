package domain;

import org.bson.Document;

public class User {
    Integer id;
    String name;
    String login;
    Currency currency;

    public User(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getString("login"), Currency.fromString(doc.getString("pref")));
    }

    public User(Integer id, String name, String login, Currency currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = currency;
    }

    public Document toDocument() {
        return new Document().append("id", id).append("name", name).append("login", login).append("pref", currency.show());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", currencyPref=" + currency.show() +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
