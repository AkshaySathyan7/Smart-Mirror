package src.ui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import src.camera.FaceDetection;
import src.api.WeatherAPI;
import src.api.NewsAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MirrorUI {

    private final Stage stage;
    private final FaceDetection faceDetection;
    private final VBox layout;
    private final Text greetingText, timeText, dateText, weatherText, newsText;

    public MirrorUI(Stage stage, FaceDetection faceDetection) {
        this.stage = stage;
        this.faceDetection = faceDetection;

        greetingText = createText("", 36, Color.WHITE);
        timeText = createText("", 48, Color.LIGHTBLUE);
        dateText = createText("", 28, Color.SILVER);
        weatherText = createText("", 28, Color.LIGHTGREEN);
        newsText = createText("", 24, Color.ORANGE);

        layout = new VBox(15, greetingText, timeText, dateText, weatherText, newsText);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        startUpdates();
    }

    private Text createText(String content, int size, Color color) {
        Text t = new Text(content);
        t.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, size));
        t.setFill(color);
        return t;
    }

    private void startUpdates() {
        Thread updateThread = new Thread(() -> {
            WeatherAPI weather = new WeatherAPI("YOUR_API_KEY", "Kochi");
            NewsAPI news = new NewsAPI("YOUR_API_KEY");
            List<String> headlines = news.getTopHeadlines();

            while (true) {
                LocalDateTime now = LocalDateTime.now();
                String formattedTime = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
                String formattedDate = now.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy"));

                Platform.runLater(() -> {
                    if (faceDetection.isFaceDetected()) {
                        greetingText.setText("Hello, Akshay 👋");
                        timeText.setText(formattedTime);
                        dateText.setText(formattedDate);
                        weatherText.setText(weather.getWeather());
                        newsText.setText("📰 " + String.join(" • ", headlines));
                        fadeIn(layout);
                    } else {
                        fadeOut(layout);
                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    private void fadeIn(VBox box) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), box);
        ft.setToValue(1);
        ft.play();
    }

    private void fadeOut(VBox box) {
        FadeTransition ft = new FadeTransition(Duration.seconds(1), box);
        ft.setToValue(0);
        ft.play();
    }

    public void show() {
        stage.show();
    }
}
