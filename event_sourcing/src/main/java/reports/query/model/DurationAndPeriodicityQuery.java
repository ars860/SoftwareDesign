package reports.query.model;

public class DurationAndPeriodicityQuery implements Query{
    int userId;

    public DurationAndPeriodicityQuery(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
