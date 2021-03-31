package stats;

import api.ApiException;
import api.ApiHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PostsStats {
    private final ApiHelper apiHelper;

    @Getter
    @Setter
    private String authToken;

    public PostsStats(String authToken) {
        this.authToken = authToken;
        apiHelper = new ApiHelper(authToken);
    }

    public List<Integer> getStatForLast24Hours(String tag) throws ApiException {
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            res.add(apiHelper.getPostsAmountInHourByTag(tag, i + 1));
        }

        return res;
    }
}
