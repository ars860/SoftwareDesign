package reports.query;

import common.Repository;
import common.Utils;
import common.domain.Attendance;
import common.domain.User;
import reports.query.model.DurationAndPeriodicityQuery;
import reports.query.model.Query;
import reports.query.model.StatsByDayQuery;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryExecutor {
    private final QueryDAO queryDAO;
    private final Repository repository;

    public QueryExecutor(QueryDAO queryDAO, Repository repository) {
        this.queryDAO = queryDAO;
        this.repository = repository;
    }

    public String executeQuery(Query query) throws SQLException {
        if (query instanceof DurationAndPeriodicityQuery) {
            DurationAndPeriodicityQuery durationAndPeriodicityQuery = (DurationAndPeriodicityQuery) query;

            User user = repository.getUser(durationAndPeriodicityQuery.getUserId());

            if (user == null) {
                return "No such user!";
            }

            List<Attendance> attendances = queryDAO.getAllAttendancesByUser(durationAndPeriodicityQuery.getUserId());

            List<Duration> durations = new ArrayList<>();
            List<Duration> entersIntervals = new ArrayList<>();
            for (int i = 0; i < attendances.size(); i += 2) {
                Attendance enter = attendances.get(i);

                if (i + 1 >= attendances.size()) {
                    continue;
                }

                Attendance exit = attendances.get(i + 1);

                if (i + 2 < attendances.size()) {
                    Attendance nextEnter = attendances.get(i + 2);

                    entersIntervals.add(Duration.between(enter.getTime(), nextEnter.getTime()));
                }
//                assert enter.getKind() == Attendance.AttendanceType.ENTER && exit.getKind() == Attendance.AttendanceType.EXIT;

                durations.add(Duration.between(enter.getTime(), exit.getTime()));
            }

            Duration mean = durations.stream().reduce(Duration.ZERO, Duration::plus).dividedBy(durations.size());
            Duration meanInterval = entersIntervals.stream().reduce(Duration.ZERO, Duration::plus).dividedBy(entersIntervals.size());

            return "Mean duration: '%s', mean interval between visits: '%s'.".formatted(
                    Utils.formatDuration(mean),
                    Utils.formatDuration(meanInterval)
            );
        }

        if (query instanceof StatsByDayQuery) {
            StatsByDayQuery statsByDayQuery = (StatsByDayQuery) query;

            User user = repository.getUser(statsByDayQuery.getUserId());

            if (user == null) {
                return "No such user!";
            }

            List<Attendance> attendances = queryDAO.getAllEnterAttendancesByUser(statsByDayQuery.getUserId());

            Map<DayOfWeek, Integer> day2attendances = new HashMap<>();
            for (Attendance attendance : attendances) {
                day2attendances.merge(attendance.getTime().getDayOfWeek(), 1, Integer::sum);
            }

            StringBuilder res = new StringBuilder();
            for (Map.Entry<DayOfWeek, Integer> entry : day2attendances.entrySet()) {
                res.append("%s: %d visits, %.2f percents\n".formatted(entry.getKey(), entry.getValue(), (double) entry.getValue() / attendances.size() * 100.0));
            }

            return "Stats by days of the week:\n" + res.toString();
        }

        return "Unknown query";
    }


}
