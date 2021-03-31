package reports.query.model;

public class StatsByDayQuery implements Query{
    int userId;

    public StatsByDayQuery(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
