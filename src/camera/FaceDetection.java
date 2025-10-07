package src.camera;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection extends Thread {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private volatile boolean faceDetected = false;
    private volatile boolean running = true;
    private VideoCapture camera;

    public FaceDetection() {
        camera = new VideoCapture(0);
        start();
    }

    @Override
    public void run() {
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
        Mat frame = new Mat();

        while (running) {
            if (camera.read(frame)) {
                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(frame, faceDetections);

                faceDetected = faceDetections.toArray().length > 0;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        camera.release();
    }

    public boolean isFaceDetected() {
        return faceDetected;
    }

    public void stopCamera() {
        running = false;
    }
}
