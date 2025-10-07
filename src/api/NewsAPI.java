package src.api;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class NewsAPI {
    private final String apiKey;

    public NewsAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getTopHeadlines() {
        List<String> headlines = new ArrayList<>();
        try {
            String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=" + apiKey;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder res = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) res.append(line);
            reader.close();

            JSONArray articles = new JSONObject(res.toString()).getJSONArray("articles");
            for (int i = 0; i < 3 && i < articles.length(); i++) {
                headlines.add(articles.getJSONObject(i).getString("title"));
            }
        } catch (Exception e) {
            headlines.add("News unavailable");
        }
        return headlines;
    }
}
