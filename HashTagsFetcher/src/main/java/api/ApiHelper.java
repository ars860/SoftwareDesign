package api;

import api.gson.Response;
import api.gson.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ApiHelper {
    private final Gson gson = new Gson();
    private final String authToken;

    public ApiHelper(String authToken) {
        this.authToken = authToken;
    }

    private String requestString(String q, long start_time, long end_time) {
        return "https://api.vk.com/method/newsfeed.search?" +
                "q=" + q + "&" +
                "start_time=" + start_time + "&" +
                "end_time=" + end_time + "&" +
                "count=" + 0 + "&" +
                "&access_token=" + authToken + "&" +
                "v=5.21";
    }

    public Integer getPostsAmountInHourByTag(String tag, int hoursAgo) throws ApiException {
        if (!tag.startsWith("#")) {
            tag = "#" + tag;
        }
        tag = URLEncoder.encode(tag, StandardCharsets.UTF_8);

        long fromTime = Instant.now().minus(hoursAgo, ChronoUnit.HOURS).getEpochSecond();
        long toTime = Instant.now().minus(hoursAgo - 1, ChronoUnit.HOURS).getEpochSecond();

        URL url;
        try {
            url = new URL(requestString(tag, fromTime, toTime));
        } catch (MalformedURLException e) {
            throw new ApiException("Bad tag: " + tag, e);
        }

        Response response;
        try (Reader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            ResponseWrapper responseWrapper = gson.fromJson(reader, ResponseWrapper.class);

            if (responseWrapper.getError() != null) {
                throw new ApiException(responseWrapper.getError().getError_msg());
            }

            if (responseWrapper.getResponse() == null) {
                throw new ApiException("Can't parse response from VK.");
            }

            response = responseWrapper.getResponse();
        } catch (IOException e) {
            throw new ApiException("IO error", e);
        } catch (JsonParseException e) {
            throw new ApiException("Can't parse response from VK.", e);
        }

        return response.getTotal_count();
    }
}
