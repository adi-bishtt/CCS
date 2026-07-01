import java.awt.MouseInfo;
import java.awt.Point;

public class Main {
    private static final double FLIP_THRESHOLD = 2;
    private static final double RESIGN_THRESHOLD = 4;

    public static void main(String[] args) {
        System.out.println("--- Chess Slam Coordinate Setup ---");
        System.out.println("Move your mouse and hover it over the Chess.com Resign button...");

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Capturing in " + i + "...");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        int xCoord = mouseLocation.x;
        int yCoord = mouseLocation.y;

        System.out.println("\nCaptured Coordinates! X: " + xCoord + ", Y: " + yCoord);
        System.out.println("Initializing Chess Slam Module...");

        KeyClicker clicker = new KeyClicker(xCoord, yCoord);
        AudioListener listener = new AudioListener(FLIP_THRESHOLD, RESIGN_THRESHOLD, clicker);

        listener.startListening();
    }
}