package manage.query.model;

import common.DatabaseUtils;

import java.time.LocalDate;

public class GetSubscriptionQuery implements Query {
    int userId;

    public GetSubscriptionQuery(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public static class Result {
//        Boolean active;
//        LocalDate startDate;
//        LocalDate endDate;
//        int extendedCnt;
//
//        public Result(Boolean active, LocalDate startDate, LocalDate endDate, int extendedCnt) {
//            this.active = active;
//            this.startDate = startDate;
//            this.endDate = endDate;
//            this.extendedCnt = extendedCnt;
//        }
//
//        @Override
//        public String toString() {
//            return "%s subscription. Active from '%s' to '%s'. Extended %d times"
//                    .formatted(
//                            endDate.isBefore(LocalDate.now()) ? "Previous" : "Current",
//                            DatabaseUtils.formatLocalDate(startDate),
//                            DatabaseUtils.formatLocalDate(endDate),
//                            extendedCnt
//                    );
//        }
//    }
}
