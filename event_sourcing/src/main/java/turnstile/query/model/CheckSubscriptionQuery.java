package turnstile.query.model;

public class CheckSubscriptionQuery implements Query {
    int userId;

    public CheckSubscriptionQuery(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
