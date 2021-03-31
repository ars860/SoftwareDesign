package domain;

import org.bson.Document;
import service.CurrencyConverter;

public class Product {
    Integer id;
    String name;
    Double cost;
    Currency currency;
    Integer addedById;

    public Product(Integer id, String name, Double cost, Currency currency, Integer addedById) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.addedById = addedById;
    }

    public Product(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getDouble("cost"), Currency.fromString(doc.getString("currency")), doc.getInteger("user_id"));
    }

    public Document toDocument() {
        return new Document().append("id", id).append("name", name).append("cost", cost).append("user_id", addedById).append("currency", currency.show());
    }

    public String toString(Currency currency) {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + CurrencyConverter.convert(cost, this.currency, currency) + currency.show() +
                ", addedById=" + addedById +
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getAddedById() {
        return addedById;
    }

    public void setAddedById(Integer addedById) {
        this.addedById = addedById;
    }
}
