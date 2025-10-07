package src.api;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class WeatherAPI {
    private final String apiKey;
    private final String city;

    public WeatherAPI(String apiKey, String city) {
        this.apiKey = apiKey;
        this.city = city;
    }

    public String getWeather() {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + apiKey + "&units=metric";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder res = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) res.append(line);
            reader.close();

            JSONObject json = new JSONObject(res.toString());
            String temp = json.getJSONObject("main").get("temp").toString();
            String condition = json.getJSONArray("weather").getJSONObject(0).getString("main");

            return "🌤 " + temp + "°C | " + condition;
        } catch (Exception e) {
            return "Weather data unavailable";
        }
    }
}
