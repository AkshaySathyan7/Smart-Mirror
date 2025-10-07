package src;

import javafx.application.Application;
import javafx.stage.Stage;
import src.ui.MirrorUI;
import src.camera.FaceDetection;

public class Main extends Application {

    private FaceDetection faceDetection;

    @Override
    public void start(Stage stage) throws Exception {
        faceDetection = new FaceDetection();
        MirrorUI ui = new MirrorUI(stage, faceDetection);
        ui.show();
    }

    @Override
    public void stop() {
        if (faceDetection != null)
            faceDetection.stopCamera();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
